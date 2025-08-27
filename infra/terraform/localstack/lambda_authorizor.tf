resource "aws_lambda_function" "jwt_auth" {
  function_name = "jwtauth_lambda"
  memory_size = 128
  timeout     = 10
  role = aws_iam_role.lambda_exec.arn
  runtime       = "java11"
  filename = "C:/Users/laptop collection/Desktop/HotelKey/TrustCare/lambda-authorizer/target/lambda-authorizer-1.0-SNAPSHOT.jar"
  handler = "com.example.trustcare.security.AuthorizationHandler::handleRequest"
  environment {
    variables = {
      HMAC_SECRET = "1234567890"
      AUTH_ALG    = "HS256" //same key for signing and verufying
    }
  }
}

variable "region" {
  description = "AWS region"
  default     = "us-east-1"
}

resource "aws_api_gateway_authorizer" "jwt_auth" {
  name                   = "jwt-auth"
  rest_api_id            = aws_api_gateway_rest_api.trustcare_api.id
  authorizer_uri         = "arn:aws:apigateway:${var.region}:lambda:path/2015-03-31/functions/${aws_lambda_function.jwt_auth.arn}/invocations"
  authorizer_result_ttl_in_seconds = 0
  type                   = "TOKEN"
  identity_source        = "method.request.header.Authorization"
}
