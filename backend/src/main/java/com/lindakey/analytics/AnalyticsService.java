package com.lindakey.analytics;

import com.lindakey.apikeys.ApiKey;
import com.lindakey.applications.Application;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsService {
    
    private final ApiUsageLogRepository repository;

    public AnalyticsService(ApiUsageLogRepository repository) {
        this.repository = repository;
    }

    public void logTraffic(Application app, ApiKey apiKey, String ip, String method, String path, int status, long latency) {
        if (app == null) return; // Cannot log if we don't know the destination app
        
        ApiUsageLog log = new ApiUsageLog(app, apiKey, ip, method, path, status, latency);
        repository.save(log);
    }
}