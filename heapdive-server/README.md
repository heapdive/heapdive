# heapdive-server

## Requirements

- Amazon S3 or compatible storage
    - Store the report JSON files
- Running environment to run the Docker image

## How can I run the heapdive-server?

    docker pull tokuhirom/heapdive:snapshot
    docker run -p 8080:8080 \
      -e S3_BUCKET_NAME=heapdive-dev \
      -e S3_ENDPOINT=https://example.com \
      -e S3_ACCESS_KEY=<YOUR_ACCESS_KEY> \
      -e S3_SECRET_KEY=<YOUR_SECRET_KEY> \
      -p 8080:8080 \
      -it tokuhirom/heapdive

## How do I build docker image?

    docker build -t heapdive .

And run the image

    docker run -p 8080:8080 \
      -e S3_BUCKET_NAME=heapdive-dev \
      -e S3_ENDPOINT=https://example.com \
      -e S3_ACCESS_KEY=<YOUR_ACCESS_KEY> \
      -e S3_SECRET_KEY=<YOUR_SECRET_KEY> \
      -p 8080:8080 \
      -it heapdive

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
