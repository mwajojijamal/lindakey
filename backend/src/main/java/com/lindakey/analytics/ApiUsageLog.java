package com.lindakey.analytics;

import com.lindakey.apikeys.ApiKey;
import com.lindakey.applications.Application;
import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "api_usage_logs")
public class ApiUsageLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_key_id")
    private ApiKey apiKey;

    @Column(name = "client_ip", nullable = false, length = 45)
    private String clientIp;

    @Column(name = "http_method", nullable = false, length = 10)
    private String httpMethod;

    @Column(name = "request_path", nullable = false, length = 1000)
    private String requestPath;

    @Column(name = "response_status", nullable = false)
    private Integer responseStatus;

    @Column(name = "upstream_latency_ms", nullable = false)
    private Long upstreamLatencyMs;

    @Column(insertable = false, updatable = false)
    private ZonedDateTime timestamp;

    public ApiUsageLog() {}

    public ApiUsageLog(Application application, ApiKey apiKey, String clientIp, String httpMethod, String requestPath, Integer responseStatus, Long upstreamLatencyMs) {
        this.application = application;
        this.apiKey = apiKey;
        this.clientIp = clientIp;
        this.httpMethod = httpMethod;
        this.requestPath = requestPath;
        this.responseStatus = responseStatus;
        this.upstreamLatencyMs = upstreamLatencyMs;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Application getApplication() { return application; }
    public void setApplication(Application application) { this.application = application; }

    public ApiKey getApiKey() { return apiKey; }
    public void setApiKey(ApiKey apiKey) { this.apiKey = apiKey; }

    public String getClientIp() { return clientIp; }
    public void setClientIp(String clientIp) { this.clientIp = clientIp; }

    public String getHttpMethod() { return httpMethod; }
    public void setHttpMethod(String httpMethod) { this.httpMethod = httpMethod; }

    public String getRequestPath() { return requestPath; }
    public void setRequestPath(String requestPath) { this.requestPath = requestPath; }

    public Integer getResponseStatus() { return responseStatus; }
    public void setResponseStatus(Integer responseStatus) { this.responseStatus = responseStatus; }

    public Long getUpstreamLatencyMs() { return upstreamLatencyMs; }
    public void setUpstreamLatencyMs(Long upstreamLatencyMs) { this.upstreamLatencyMs = upstreamLatencyMs; }

    public ZonedDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(ZonedDateTime timestamp) { this.timestamp = timestamp; }
}