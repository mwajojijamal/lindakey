package com.lindakey.apikeys;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/applications/{appId}/keys")
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    public ApiKeyController(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @PostMapping
    public ResponseEntity<?> generateKey(@PathVariable Long appId, @RequestBody ApiKeyRequest request) {
        try {
            String rawKey = apiKeyService.createApiKey(
                    appId, 
                    request.getKeyName(), 
                    request.getScopes(), 
                    request.getAllowedIps()
            );
            
            java.util.HashMap response = new java.util.HashMap();
            response.put("message", "WARNING: Save this key immediately. It will not be shown again.");
            response.put("apiKey", rawKey);
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listKeys(@PathVariable Long appId) {
        java.util.List<ApiKey> rawKeys = apiKeyService.getApiKeys(appId);
        
        // Using raw ArrayList to completely eliminate the < > generic syntax error
        java.util.ArrayList safeKeys = new java.util.ArrayList();
        
        for (ApiKey key : rawKeys) {
            java.util.HashMap map = new java.util.HashMap();
            map.put("id", key.getId());
            map.put("name", key.getKeyName());
            map.put("prefix", key.getKeyPrefix());
            map.put("isActive", key.getIsActive());
            map.put("scopes", key.getScopes());
            
            if (key.getExpiresAt() != null) {
                map.put("expiresAt", key.getExpiresAt().toString());
            } else {
                map.put("expiresAt", "Never");
            }
            
            safeKeys.add(map);
        }
        
        return ResponseEntity.ok(safeKeys);
    }

    @DeleteMapping("/{keyId}")
    public ResponseEntity<?> revokeKey(@PathVariable Long appId, @PathVariable Long keyId) {
        try {
            apiKeyService.revokeApiKey(appId, keyId);
            return ResponseEntity.ok("API Key successfully revoked and disabled.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}