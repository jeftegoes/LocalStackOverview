#!/bin/sh

source ./1-params.sh

echo "Creating SQS queue..."

QUEUE_URL=$(aws sqs create-queue \
    --queue-name "$RANDOM_USER_QUEUE" \
    --endpoint-url "$AWS_ENDPOINT_URL" \
    --query "QueueUrl" \
    --output text)

echo "Queue URL: $QUEUE_URL"

QUEUE_ARN=$(aws sqs get-queue-attributes \
    --queue-url "$QUEUE_URL" \
    --attribute-names QueueArn \
    --endpoint-url "$AWS_ENDPOINT_URL" \
    --query "Attributes.QueueArn" \
    --output text)

echo "Queue ARN: $QUEUE_ARN"

echo "Creating SNS topic..."

TOPIC_ARN=$(aws sns create-topic \
    --name "$RANDOM_USER_TOPIC" \
    --endpoint-url "$AWS_ENDPOINT_URL" \
    --query "TopicArn" \
    --output text)

echo "Topic ARN: $TOPIC_ARN"

echo "Configuring SQS policy..."

POLICY=$(cat <<EOF
{
  "Version":"2012-10-17",
  "Statement":[
    {
      "Sid":"Allow-SNS-SendMessage",
      "Effect":"Allow",
      "Principal":"*",
      "Action":"sqs:SendMessage",
      "Resource":"$QUEUE_ARN",
      "Condition":{
        "ArnEquals":{
          "aws:SourceArn":"$TOPIC_ARN"
        }
      }
    }
  ]
}
EOF
)

aws sqs set-queue-attributes \
    --queue-url "$QUEUE_URL" \
    --endpoint-url "$AWS_ENDPOINT_URL" \
    --attributes Policy="$POLICY"

echo "Creating subscription..."

SUBSCRIPTION_ARN=$(aws sns subscribe \
    --topic-arn "$TOPIC_ARN" \
    --protocol sqs \
    --notification-endpoint "$QUEUE_ARN" \
    --endpoint-url "$AWS_ENDPOINT_URL" \
    --query "SubscriptionArn" \
    --output text)

echo "Subscription ARN: $SUBSCRIPTION_ARN"

echo
echo "=================================="
echo "Infrastructure created successfully!"
echo "=================================="
echo "Queue URL        : $QUEUE_URL"
echo "Queue ARN        : $QUEUE_ARN"
echo "Topic ARN        : $TOPIC_ARN"
echo "Subscription ARN : $SUBSCRIPTION_ARN"