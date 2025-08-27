package com.example.trustcare.security;


import java.util.List;

public class CustomAuthorizerResponse {

    private String principalId;
    private PolicyDocument policyDocument;
    private Object context;

    public CustomAuthorizerResponse(String principalId, PolicyDocument policyDocument, Object context) {
        this.principalId = principalId;
        this.policyDocument = policyDocument;
        this.context = context;
    }

    public static class PolicyDocument {
        private String Version;
        private List<PolicyStatement> Statement;

        public PolicyDocument() {}

        public void setVersion(String version) { Version = version; }
        public void setStatement(List<PolicyStatement> statement) { Statement = statement; }

        public static class PolicyStatement {
            private String Action;
            private String Effect;
            private List<String> Resource;

            public PolicyStatement() {}

            public void setAction(String action) { Action = action; }
            public void setEffect(String effect) { Effect = effect; }
            public void setResource(List<String> resource) { Resource = resource; }
        }
    }
}
