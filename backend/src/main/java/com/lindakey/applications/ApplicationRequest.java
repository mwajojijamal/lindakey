package com.lindakey.applications;

public class ApplicationRequest {
    
    private Long userId;
    private String name;
    private String environment;
    private String upstreamTargetUrl;

    public ApplicationRequest() {}

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }

    public String getUpstreamTargetUrl() { return upstreamTargetUrl; }
    public void setUpstreamTargetUrl(String upstreamTargetUrl) { this.upstreamTargetUrl = upstreamTargetUrl; }
}