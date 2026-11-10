package com.lindakey.applications;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    public ResponseEntity<?> registerApp(@RequestBody ApplicationRequest request) {
        try {
            Application savedApp = applicationService.registerApplication(
                    request.getUserId(),
                    request.getName(),
                    request.getEnvironment(),
                    request.getUpstreamTargetUrl()
            );
            return ResponseEntity.ok("Application registered successfully. ID: " + savedApp.getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}