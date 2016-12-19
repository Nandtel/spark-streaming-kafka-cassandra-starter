# Pipeline

Demo pipeline application. Pipeline is compact and easy-to-use example of using Kafka, Spark Streaming and Cassandra together.

## Technologies
- Java 8
- Scala 2.10
- Gradle
- Kafka
- Spark
- Cassandra 2.2.6
- Docker

## How to run manually
Build the application:
```
./gradlew shadowJar
```

Replace file log4j.properties to build/libs and then start application via docker-compose:
```
docker-compose run -d spark
```

Open Kafka container with command:
```
docker exec -it $(docker-compose ps -q kafka) bash
```

Create topic in kafka container:
```
kafka-topics.sh --create --zookeeper $ZOOKEEPER --replication-factor 1 --partitions 2 --topic events
```

Check topic in kafka container:
```
kafka-topics.sh --list --zookeeper $ZOOKEEPER
kafka-topics.sh --describe --zookeeper $ZOOKEEPER --topic events
```

Then exit from Kafka container and open Spark container with command:
```
docker exec -it $(docker-compose ps -q spark) bash
```

Start master and worker in spark container:
```
./usr/local/spark/sbin/start-master.sh
./usr/local/spark/sbin/start-slave.sh $MASTER
```

Run spark-consumer application in spark container:
```
spark-submit \
--master $MASTER \
--conf spark.cassandra.connection.host=cassandra \
app/spark-consumer-0.0.1.jar kafka:9092 events
```

Then open Kafka container and run kafka-producer application:
```
docker exec -it $(docker-compose ps -q kafka) java -jar ../app/kafka-producer-0.0.1.jar
```

Open the cassandra container to see the results:
```
docker exec -it $(docker-compose ps -q cassandra) cqlsh -e "SELECT * FROM api.event"
```

If you want delete locked builds directories, then run:
```
sudo rm -rf spark-consumer/build kafka-producer/build model-common/build web-api/build
```