package com.lindakey.apikeys;

public class ApiKeyRequest {
    private String keyName;
    private String scopes;
    private String[] allowedIps;

    public ApiKeyRequest() {}

    public String getKeyName() { return keyName; }
    public void setKeyName(String keyName) { this.keyName = keyName; }

    public String getScopes() { return scopes; }
    public void setScopes(String scopes) { this.scopes = scopes; }

    public String[] getAllowedIps() { return allowedIps; }
    public void setAllowedIps(String[] allowedIps) { this.allowedIps = allowedIps; }
}