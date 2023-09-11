# heapdive-server

## Requirements

- Amazon S3 or compatible storage
    - Store the report JSON files
- Running environment to run the Docker image

## Configuration variables

### S3 related

heapdive-server depends on the S3. heapdive-server uses the following environment variables to configure S3:

| Variable        | Description                                                                 |
|-----------------|-----------------------------------------------------------------------------|
| `S3_BUCKET`     | The name of the S3 bucket to use.                                           |
| `S3_ACCESS_KEY` | The access key to use for S3.                                               |
| `S3_SECRET_KEY` | The secret key to use for S3.                                               |
| `S3_ENDPOINT`   | The S3 endpoint to use. This is only required to use S3 compatible storage. |
| `S3_REGION`     | The S3 region to use. Default value is `us-east-1`.                         |
