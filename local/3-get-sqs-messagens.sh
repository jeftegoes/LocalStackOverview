source ./1-params.sh

aws sqs receive-message --queue-url http://localhost:4566/000000000000/USER_QUEUE --max-number-of-messages 10 --visibility-timeout 10