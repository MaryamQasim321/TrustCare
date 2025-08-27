
resource "aws_api_gateway_rest_api" "trustcare_api" {
  name = "TrustCareApi"
  endpoint_configuration {
    types = ["REGIONAL"]
  }
}

resource "aws_api_gateway_resource" "proxy" {
  rest_api_id = aws_api_gateway_rest_api.trustcare_api.id
  parent_id   = aws_api_gateway_rest_api.trustcare_api.root_resource_id
  path_part   = "{proxy+}"
}

resource "aws_api_gateway_method" "proxy_method" {
  rest_api_id   = aws_api_gateway_rest_api.trustcare_api.id
  resource_id   = aws_api_gateway_resource.proxy.id
  http_method   = "ANY"
  authorization = "CUSTOM"
  authorizer_id = aws_api_gateway_authorizer.jwt_auth.id

  request_parameters = {
    "method.request.path.proxy" = true
  }
}

# Allow API Gateway to invoke Lambda
resource "aws_lambda_permission" "apigw_invoke" {
  statement_id  = "AllowExecutionFromAPIGateway"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.jwt_auth.function_name
  principal     = "apigateway.amazonaws.com"
  source_arn    = "${aws_api_gateway_rest_api.trustcare_api.execution_arn}/*/*"
}

resource "aws_api_gateway_integration" "proxy_integration" {
  rest_api_id             = aws_api_gateway_rest_api.trustcare_api.id
  resource_id             = aws_api_gateway_resource.proxy.id
  http_method             = aws_api_gateway_method.proxy_method.http_method
  integration_http_method = "ANY"
  type                    = "HTTP_PROXY"
  uri                     = "http://host.docker.internal:8080/{proxy}"
  request_parameters = {
    "integration.request.path.proxy" = "method.request.path.proxy"
  }
}

resource "aws_api_gateway_deployment" "proxy_deployment" {
  depends_on  = [aws_api_gateway_integration.proxy_integration]
  rest_api_id = aws_api_gateway_rest_api.trustcare_api.id
  //deployment updates auto whenever api definition change
  triggers = {
    redeploy = sha1(jsonencode({
      resource    = aws_api_gateway_resource.proxy.id
      method      = aws_api_gateway_method.proxy_method.id
      integration = aws_api_gateway_integration.proxy_integration.id
    }))
  }
}

resource "aws_api_gateway_stage" "proxy_stage" {
  deployment_id = aws_api_gateway_deployment.proxy_deployment.id
  rest_api_id   = aws_api_gateway_rest_api.trustcare_api.id
  stage_name    = "dev"
  depends_on = [aws_api_gateway_deployment.proxy_deployment]
}

