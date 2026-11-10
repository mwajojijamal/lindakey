package com.lindakey.gateway;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RateLimitingService {
    
    // A highly efficient map to track: [Key Hash -> Number of Requests]
    private final ConcurrentHashMap<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();

    public boolean isAllowed(String keyHash, int maxRequestsPerMinute) {
        // Get the current count for this key (start at 0 if it's new), and add 1
        int currentCount = requestCounts.computeIfAbsent(keyHash, k -> new AtomicInteger(0)).incrementAndGet();
        
        return currentCount <= maxRequestsPerMinute;
    }

    // This method automatically runs every 60,000 milliseconds (1 minute) to empty the tracking map
    @Scheduled(fixedRate = 60000)
    public void resetLimits() {
        requestCounts.clear();
        System.out.println("SYSTEM: Rate limits reset for the new minute.");
    }
}