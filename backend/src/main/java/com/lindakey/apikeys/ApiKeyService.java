package com.lindakey.apikeys;

import com.lindakey.applications.Application;
import com.lindakey.applications.ApplicationRepository;
import org.springframework.stereotype.Service;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final ApplicationRepository applicationRepository;

    public ApiKeyService(ApiKeyRepository apiKeyRepository, ApplicationRepository applicationRepository) {
        this.apiKeyRepository = apiKeyRepository;
        this.applicationRepository = applicationRepository;
    }

    public String createApiKey(Long applicationId, String keyName, String scopes, String[] allowedIps) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application ID not found."));

        ApiKeyGenerator.GeneratedKey keys = ApiKeyGenerator.generate();

        ApiKey apiKey = new ApiKey();
        apiKey.setApplication(app);
        apiKey.setKeyName(keyName);
        apiKey.setKeyPrefix(keys.prefix());
        apiKey.setKeyHash(keys.sha256Hash());
        apiKey.setScopes(scopes != null ? scopes : "READ");
        apiKey.setAllowedIps(allowedIps != null ? allowedIps : new String[]{});
        apiKey.setExpiresAt(ZonedDateTime.now().plusDays(365));

        apiKeyRepository.save(apiKey);
        return keys.rawKey();
    }

    // --- NEW DASHBOARD METHODS ---

    public List<ApiKey> getApiKeys(Long applicationId) {
        return apiKeyRepository.findByApplicationId(applicationId);
    }

    public void revokeApiKey(Long applicationId, Long keyId) {
        ApiKey apiKey = apiKeyRepository.findById(keyId)
                .orElseThrow(() -> new IllegalArgumentException("API Key not found."));
        
        // Security check to ensure a hacker can't revoke a key from a different app
        if (!apiKey.getApplication().getId().equals(applicationId)) {
            throw new IllegalArgumentException("This key does not belong to the specified application.");
        }

        apiKey.setIsActive(false);
        apiKeyRepository.save(apiKey);
    }
}