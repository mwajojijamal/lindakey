package com.lindakey.gateway;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProxyController {

    private final ProxyService proxyService;

    public ProxyController(ProxyService proxyService) {
        this.proxyService = proxyService;
    }

    // Catch ALL HTTP methods and ALL paths under the specific application ID
    @RequestMapping(value = "/proxy/{appId}/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> proxyRequest(
            @PathVariable Long appId,
            HttpServletRequest request,
            @RequestBody(required = false) String body) {
        
        return proxyService.processAndProxy(appId, request, body);
    }
}