resource "aws_sqs_queue" "booking_queue" {
  name = "booking-queue"
}
resource "aws_sns_topic" "booking_notifications" {
  name = "booking-notifications"
}
resource "aws_sns_topic_subscription" "queue_sub" {
  topic_arn = aws_sns_topic.booking_notifications.arn
  protocol= "sqs"
  endpoint= aws_sqs_queue.booking_queue.arn
  raw_message_delivery= true
}

data "aws_iam_policy_document" "sqs_queue_policy" {
  statement {
    sid    = "Allow-SNS-SendMessage"
    effect = "Allow"
  principals {
      type        = "Service"
      identifiers = ["sns.amazonaws.com"]
    }
    actions = [
      "SQS:SendMessage"
    ]
    resources = [
      aws_sqs_queue.booking_queue.arn
    ]
     condition {
      test     = "ArnEquals"
      variable = "aws:SourceArn"
      values = [
        aws_sns_topic.booking_notifications.arn
      ]
    }
  }
}

resource "aws_sqs_queue_policy" "allow_sns" {
  queue_url = aws_sqs_queue.booking_queue.id
  policy    = data.aws_iam_policy_document.sqs_queue_policy.json
}

resource "aws_ses_email_identity" "test_sender" {
  email = "test@localstack.com"
}
resource "aws_sns_topic_subscription" "booking_email" {
  topic_arn = aws_sns_topic.booking_notifications.arn
  protocol  = "email"
  endpoint  = "test@localstack.com"
}
