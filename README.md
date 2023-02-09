# Subscription application using ZIO

This is a simple application that manages subscriptions requests:
- Saves the subscription state in a database. [WIP]
- Notify about subscription changes in Pulsar/Kafka. [TBD]

My idea is just to learn more about ZIO, I'm using as a guideline [zio-petclinic](https://github.com/zio/zio-petclinic)

This app uses:
  [ZIO Http](https://github.com/zio/zio-http) for the HTTP server 
  [Tapir ZIO](https://github.com/softwaremill/tapir) for endpoints and endpoint documentation
  [ZIO JSON](https://github.com/zio/zio-json) for JSON serialization
  [ZIO Quill](https://github.com/zio/zio-quill) for SQL queries
  [ZIO Config](https://github.com/zio/zio-config) for app configuration and documentation
## Run the application

1. Start the services 
    ```shell
    docker compose -f docker/docker-compose.yaml up -d
    ```
2. Start sbt
    ```shell
    sbt
    ```
3. Inside sbt, start the application
    ```shell
    reStart
    ```
4. Visit the swagger page to list the endpoints 
    ```shell
    http://localhost:8080/docs
    ```
5. Inside sbt, stop the application
    ```shell
    reStop
    ```
