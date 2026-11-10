package com.lindakey.gateway;

import com.lindakey.analytics.AnalyticsService;
import com.lindakey.apikeys.ApiKey;
import com.lindakey.apikeys.ApiKeyRepository;
import com.lindakey.apikeys.ApiKeyGenerator;
import com.lindakey.applications.Application;
import com.lindakey.applications.ApplicationRepository;
import com.lindakey.webhooks.WebhookService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.ZonedDateTime;
import java.util.Arrays;

@Service
public class ProxyService {
    private final ApiKeyRepository apiKeyRepository;
    private final ApplicationRepository applicationRepository;
    private final AnalyticsService analyticsService;
    private final WebhookService webhookService;
    private final RateLimitingService rateLimitingService;
    private final RestTemplate restTemplate = new RestTemplate();

    public ProxyService(ApiKeyRepository apiKeyRepository, ApplicationRepository applicationRepository, 
                        AnalyticsService analyticsService, WebhookService webhookService, 
                        RateLimitingService rateLimitingService) {
        this.apiKeyRepository = apiKeyRepository;
        this.applicationRepository = applicationRepository;
        this.analyticsService = analyticsService;
        this.webhookService = webhookService;
        this.rateLimitingService = rateLimitingService;
    }

    public ResponseEntity<String> processAndProxy(Long appId, HttpServletRequest request, String body) {
        long startTime = System.currentTimeMillis();
        Application app = applicationRepository.findById(appId).orElse(null);
        
        String clientIp = request.getRemoteAddr();
        // Translate IPv6 localhost to IPv4 for local development
        if ("0:0:0:0:0:0:0:1".equals(clientIp)) {
            clientIp = "127.0.0.1";
        }
        
        if (app == null) {
            return ResponseEntity.status(404).body("Gateway Error: Application not found");
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            analyticsService.logTraffic(app, null, clientIp, request.getMethod(), request.getRequestURI(), 401, 0);
            return ResponseEntity.status(401).body("Gateway Error: Missing or invalid Authorization header");
        }
        
        String rawKey = authHeader.substring(7);
        String keyHash = ApiKeyGenerator.hashKey(rawKey);
        ApiKey apiKey = apiKeyRepository.findByKeyHash(keyHash).orElse(null);

        if (apiKey == null || !apiKey.getIsActive()) {
            analyticsService.logTraffic(app, null, clientIp, request.getMethod(), request.getRequestURI(), 401, 0);
            webhookService.fireSecurityEvent(appId, "SECURITY_VIOLATION", "Invalid API Key attempt from IP: " + clientIp);
            return ResponseEntity.status(401).body("Gateway Error: Unauthorized - Invalid or inactive API Key");
        }

        if (!apiKey.getApplication().getId().equals(appId)) {
            analyticsService.logTraffic(app, apiKey, clientIp, request.getMethod(), request.getRequestURI(), 403, 0);
            webhookService.fireSecurityEvent(appId, "SECURITY_VIOLATION", "Key does not belong to this application. IP: " + clientIp);
            return ResponseEntity.status(403).body("Gateway Error: Forbidden - Key does not belong to this application");
        }
        
        if (apiKey.getExpiresAt() != null && ZonedDateTime.now().isAfter(apiKey.getExpiresAt())) {
            analyticsService.logTraffic(app, apiKey, clientIp, request.getMethod(), request.getRequestURI(), 401, 0);
            return ResponseEntity.status(401).body("Gateway Error: Unauthorized - API Key has expired");
        }

        // --- ZERO-TRUST IP ALLOWLISTING ---
        String[] allowedIps = apiKey.getAllowedIps();
        if (allowedIps != null && allowedIps.length > 0) {
            boolean ipAllowed = Arrays.asList(allowedIps).contains(clientIp);
            if (!ipAllowed) {
                analyticsService.logTraffic(app, apiKey, clientIp, request.getMethod(), request.getRequestURI(), 403, 0);
                webhookService.fireSecurityEvent(appId, "SECURITY_VIOLATION", "IP Allowlist blocked access from: " + clientIp);
                return ResponseEntity.status(403).body("Gateway Error: Forbidden - Your IP address (" + clientIp + ") is not authorized for this API key.");
            }
        }

        // --- RATE LIMITING ---
        if (!rateLimitingService.isAllowed(keyHash, apiKey.getRateLimitPerMinute())) {
            analyticsService.logTraffic(app, apiKey, clientIp, request.getMethod(), request.getRequestURI(), 429, 0);
            webhookService.fireSecurityEvent(appId, "RATE_LIMIT_EXCEEDED", "Key exceeded limit of " + apiKey.getRateLimitPerMinute() + "/min. IP: " + clientIp);
            return ResponseEntity.status(429).body("Gateway Error: Too Many Requests - Rate limit exceeded");
        }

        String requestUri = request.getRequestURI();
        String proxyPrefix = "/proxy/" + appId;
        String endpoint = requestUri.substring(proxyPrefix.length());
        
        String targetUrl = app.getUpstreamTargetUrl() + endpoint;
        if (request.getQueryString() != null) {
            targetUrl += "?" + request.getQueryString();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Forwarded-For", clientIp);
        HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);
        HttpMethod method = HttpMethod.valueOf(request.getMethod());

        try {
            ResponseEntity<String> response = restTemplate.exchange(targetUrl, method, httpEntity, String.class);
            long latency = System.currentTimeMillis() - startTime;
            analyticsService.logTraffic(app, apiKey, clientIp, request.getMethod(), request.getRequestURI(), response.getStatusCode().value(), latency);
            return response;
        } catch (Exception e) {
            long latency = System.currentTimeMillis() - startTime;
            analyticsService.logTraffic(app, apiKey, clientIp, request.getMethod(), request.getRequestURI(), 502, latency);
            return ResponseEntity.status(502).body("Gateway Error: Bad Gateway - Upstream service is unreachable.");
        }
    }
}