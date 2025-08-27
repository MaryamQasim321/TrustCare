package com.example.trustcare.security;

import java.util.Map;

public class CustomAuthorizerRequest {

    private String type;
    private String authorizationToken;
    private String methodArn;
    private Map<String, String> headers;

    public String getAuthorizationToken() { return authorizationToken; }
    public void setAuthorizationToken(String authorizationToken) { this.authorizationToken = authorizationToken; }

    public String getMethodArn() { return methodArn; }
    public void setMethodArn(String methodArn) { this.methodArn = methodArn; }

    public Map<String, String> getHeaders() { return headers; }
    public void setHeaders(Map<String, String> headers) { this.headers = headers; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
