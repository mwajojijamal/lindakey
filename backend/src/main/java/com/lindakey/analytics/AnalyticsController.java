package com.lindakey.analytics;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final ApiUsageLogRepository repository;

    public AnalyticsController(ApiUsageLogRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/usage")
    public ResponseEntity<?> getUsageLogs() {
        java.util.List<ApiUsageLog> rawLogs = repository.findAll();
        
        // Using raw ArrayList to completely bypass the < > bracket errors
        java.util.ArrayList safeLogs = new java.util.ArrayList();
        
        for (ApiUsageLog log : rawLogs) {
            java.util.HashMap map = new java.util.HashMap();
            map.put("id", log.getId());
            map.put("ip", log.getClientIp());
            map.put("method", log.getHttpMethod());
            map.put("path", log.getRequestPath());
            map.put("status", log.getResponseStatus());
            map.put("latency", log.getUpstreamLatencyMs());
            
            safeLogs.add(map);
        }
        
        return ResponseEntity.ok(safeLogs);
    }
}