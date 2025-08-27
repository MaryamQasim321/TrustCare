package com.example.trustcare.security;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Collections;

public class AuthorizationHandler implements RequestHandler<CustomAuthorizerRequest, CustomAuthorizerResponse> {

    private static final String HMAC_SECRET = System.getenv("HMAC_SECRET");
    private static final Key SIGNING_KEY = Keys.hmacShaKeyFor(HMAC_SECRET.getBytes());

    @Override
    public CustomAuthorizerResponse handleRequest(CustomAuthorizerRequest event, Context context) {
        String token = event.getAuthorizationToken();
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                Jwts.parserBuilder()
                        .setSigningKey(SIGNING_KEY)
                        .build()
                        .parseClaimsJws(token);

                return generatePolicy("user", "Allow", event.getMethodArn());
            } catch (Exception e) {
                return generatePolicy("user", "Deny", event.getMethodArn());
            }
        }
        return generatePolicy("user", "Deny", event.getMethodArn());
    }

    private CustomAuthorizerResponse generatePolicy(String principalId, String effect, String resource) {
        CustomAuthorizerResponse.PolicyDocument.PolicyStatement statement =
                new CustomAuthorizerResponse.PolicyDocument.PolicyStatement();
        statement.setAction("execute-api:Invoke");
        statement.setEffect(effect);
        statement.setResource(Collections.singletonList(resource));

        CustomAuthorizerResponse.PolicyDocument policyDocument = new CustomAuthorizerResponse.PolicyDocument();
        policyDocument.setVersion("2012-10-17");
        policyDocument.setStatement(Collections.singletonList(statement));

        return new CustomAuthorizerResponse(principalId, policyDocument, null);
    }
}
