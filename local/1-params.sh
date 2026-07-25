#!/bin/sh

# Set Localstack credencial: export LOCALSTACK_AUTH_TOKEN={CREDENTIAL_HERE}
source ./0-localstack-credentail.sh

# AWS Credentials (dummy values for LocalStack)
export AWS_ACCESS_KEY_ID=test
export AWS_SECRET_ACCESS_KEY=test
export AWS_SESSION_TOKEN=test
export RANDOM_USER_QUEUE=USER_QUEUE

# AWS Configuration
export AWS_DEFAULT_REGION=us-east-1
export AWS_REGION=us-east-1

# LocalStack Endpoint
export AWS_ENDPOINT_URL=http://localhost:4566

echo "LocalStack environment configured."
echo "Endpoint: $AWS_ENDPOINT_URL"
echo "Region:   $AWS_REGION"
echo "Local Stack Auth Token: $LOCALSTACK_AUTH_TOKEN"