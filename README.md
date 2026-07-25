# 1. Register

- [SignUP](https://app.localstack.cloud/sign-up)

# 2. Docker compose

```yaml
services:
localstack:
  container_name: localstack_dev
  image: localstack/localstack
  ports:
    - "127.0.0.1:4566:4566"
    - "127.0.0.1:4510-4559:4510-4559"
    - "127.0.0.1:443:443"
  environment:
    - LOCALSTACK_AUTH_TOKEN=${LOCALSTACK_AUTH_TOKEN:?}
    - DEBUG=${DEBUG:-0}
    - PERSISTENCE=${PERSISTENCE:-0}
  volumes:
    - "${LOCALSTACK_VOLUME_DIR:-./volume}:/var/lib/localstack"
    - "/var/run/docker.sock:/var/run/docker.sock"
```

# 3. Health Test

- `http://localhost:4566/_localstack/health`

# 4. Configure AWS CLI

1. aws configure
2. AWS Access Key ID: test
3. AWS Secret Access Key: test
4. Default region: us-east-1
5. Default output format: json

## 4.1. Test

- aws --endpoint-url=http://localhost:4566 s3 ls
