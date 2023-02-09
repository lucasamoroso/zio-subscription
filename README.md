# Subscription application using ZIO

This is a simple application that manages subscriptions requests:
- Saves the subscription state in a database. [WIP]
- Notify about subscription changes in Pulsar/Kafka. [TBD]

My idea is just to learn more about ZIO, I'm using as a guideline [zio-petclinic](https://github.com/zio/zio-petclinic)

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
4. Inside sbt, stop the application
    ```shell
    reStop
    ```
