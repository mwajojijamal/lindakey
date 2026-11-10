package com.lindakey.webhooks;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
public class WebhookService {

    private final WebhookRepository webhookRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public WebhookService(WebhookRepository webhookRepository) {
        this.webhookRepository = webhookRepository;
    }

    // @Async forces this method to run on a separate background thread
    @Async
    public void fireSecurityEvent(Long applicationId, String eventType, String message) {
        List<Webhook> activeWebhooks = webhookRepository.findByApplicationIdAndIsActiveTrue(applicationId);
        
        for (Webhook webhook : activeWebhooks) {
            try {
                String payload = String.format("{\"event\": \"%s\", \"message\": \"%s\", \"token\": \"%s\"}", 
                                               eventType, message, webhook.getSecretToken());
                
                // Fire the POST request to the developer's registered URL
                restTemplate.postForEntity(webhook.getTargetUrl(), payload, String.class);
                System.out.println("ASYNC ALERT: Webhook fired successfully to " + webhook.getTargetUrl());
            } catch (Exception e) {
                System.err.println("ASYNC ALERT: Failed to fire webhook to " + webhook.getTargetUrl() + " - " + e.getMessage());
            }
        }
    }
}