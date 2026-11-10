package com.lindakey.webhooks;

import com.lindakey.applications.Application;
import com.lindakey.applications.ApplicationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/applications/{appId}/webhooks")
public class WebhookController {

    private final WebhookRepository webhookRepository;
    private final ApplicationRepository applicationRepository;

    public WebhookController(WebhookRepository webhookRepository, ApplicationRepository applicationRepository) {
        this.webhookRepository = webhookRepository;
        this.applicationRepository = applicationRepository;
    }

    @PostMapping
    public ResponseEntity<?> registerWebhook(@PathVariable Long appId, @RequestBody Map<String, String> request) {
        Application app = applicationRepository.findById(appId).orElse(null);
        if (app == null) {
            return ResponseEntity.badRequest().body("Application not found");
        }

        Webhook webhook = new Webhook();
        webhook.setApplication(app);
        webhook.setTargetUrl(request.get("targetUrl"));
        webhook.setSecretToken(request.get("secretToken"));
        
        webhookRepository.save(webhook);
        return ResponseEntity.ok("Webhook registered successfully. ID: " + webhook.getId());
    }
}