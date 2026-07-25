#!/bin/sh

source ./1-params.sh

docker-compose up -d localstack

echo "Starting..."
echo "Waiting for LocalStack..."

sleep 10

echo "Creating SQS Queue..."
aws sqs create-queue --queue-name $RANDOM_USER_QUEUE --endpoint-url $AWS_ENDPOINT_URL