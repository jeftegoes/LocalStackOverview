#!/bin/sh

set -e

# LocalStack Endpoint
export AWS_ENDPOINT_URL=http://localhost:4566

INSTANCE_NAME="my-ec2"
KEY_NAME="my-ec2-key"
SECURITY_GROUP_NAME="my-ec2-sg"
INSTANCE_TYPE="t3.micro"

echo "Using region: ${AWS_DEFAULT_REGION:-not configured}"

#
# Create Key Pair
#
if ! aws ec2 describe-key-pairs \
    --endpoint-url $AWS_ENDPOINT_URL \
    --key-names "$KEY_NAME" >/dev/null 2>&1; then

    echo "Creating key pair..."

    aws ec2 create-key-pair \
        --key-name "$KEY_NAME" \
        --query "KeyMaterial" \
        --endpoint-url $AWS_ENDPOINT_URL \
        --output text > "${KEY_NAME}.pem"

    chmod 400 "${KEY_NAME}.pem"
else
    echo "Key pair already exists."
fi

#
# Default VPC
#
VPC_ID=$(aws ec2 describe-vpcs \
    --endpoint-url $AWS_ENDPOINT_URL \
    --filters Name=isDefault,Values=true \
    --query "Vpcs[0].VpcId" \
    --output text)

echo "VPC: $VPC_ID"

#
# Default Subnet
#
SUBNET_ID=$(aws ec2 describe-subnets \
    --endpoint-url $AWS_ENDPOINT_URL \
    --filters Name=default-for-az,Values=true Name=vpc-id,Values="$VPC_ID" \
    --query "Subnets[0].SubnetId" \
    --output text)

echo "Subnet: $SUBNET_ID"

#
# Security Group
#
SG_ID=$(aws ec2 describe-security-groups \
    --endpoint-url $AWS_ENDPOINT_URL \
    --filters Name=group-name,Values="$SECURITY_GROUP_NAME" \
    --query "SecurityGroups[0].GroupId" \
    --output text)

if [ "$SG_ID" = "None" ]; then

    echo "Creating Security Group..."

    SG_ID=$(aws ec2 create-security-group \
        --group-name "$SECURITY_GROUP_NAME" \
        --endpoint-url $AWS_ENDPOINT_URL \
        --description "Security group for EC2 SSH access" \
        --vpc-id "$VPC_ID" \
        --query "GroupId" \
        --output text)

    aws ec2 authorize-security-group-ingress \
        --endpoint-url $AWS_ENDPOINT_URL \
        --group-id "$SG_ID" \
        --protocol tcp \
        --port 22 \
        --cidr 0.0.0.0/0

fi

echo "Security Group: $SG_ID"

#
# Latest Amazon Linux 2023 AMI
#
AMI_ID=$(aws ec2 describe-images \
    --owners amazon \
    --endpoint-url $AWS_ENDPOINT_URL \
    --filters "Name=name,Values=al2023-ami-*" \
    --query "sort_by(Images,&CreationDate)[-1].ImageId" \
    --output text)

echo "AMI: $AMI_ID"

#
# Launch Instance
#
INSTANCE_ID=$(aws ec2 run-instances \
    --image-id "$AMI_ID" \
    --endpoint-url $AWS_ENDPOINT_URL \
    --instance-type "$INSTANCE_TYPE" \
    --key-name "$KEY_NAME" \
    --security-group-ids "$SG_ID" \
    --subnet-id "$SUBNET_ID" \
    --associate-public-ip-address \
    --tag-specifications "ResourceType=instance,Tags=[{Key=Name,Value=$INSTANCE_NAME}]" \
    --query "Instances[0].InstanceId" \
    --output text)

echo "Instance: $INSTANCE_ID"

#
# Wait until running
#
echo "Waiting for instance..."

aws ec2 wait instance-running \
    --endpoint-url $AWS_ENDPOINT_URL \
    --instance-ids "$INSTANCE_ID"

#
# Public IP
#
PUBLIC_IP=$(aws ec2 describe-instances \
    --instance-ids "$INSTANCE_ID" \
    --endpoint-url $AWS_ENDPOINT_URL \
    --query "Reservations[0].Instances[0].PublicIpAddress" \
    --output text)

echo
echo "===================================="
echo "Instance created successfully!"
echo "===================================="
echo
echo "Instance ID : $INSTANCE_ID"
echo "Public IP   : $PUBLIC_IP"
echo
echo "SSH command:"
echo
echo "ssh -i ${KEY_NAME}.pem ec2-user@$PUBLIC_IP"