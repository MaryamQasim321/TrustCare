output "booking_queue_url" {
  value = aws_sqs_queue.booking_queue.id
}

output "booking_queue_arn" {
  value = aws_sqs_queue.booking_queue.arn
}

output "booking_topic_arn" {
  value = aws_sns_topic.booking_notifications.arn
}



output "api_gateway_invoke_url" {
  value = "http://localhost:4566/restapis/${aws_api_gateway_rest_api.trustcare_api.id}/dev/_user_request_"
}
