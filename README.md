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
kafka-topics.sh --create --zookeeper $ZOOKEEPER --replication-factor 1 --partitions 2 --topic Hello-Kafka
```

Check topic in kafka container:
```
kafka-topics.sh --list --zookeeper $ZOOKEEPER
kafka-topics.sh --describe --zookeeper $ZOOKEEPER --topic Hello-Kafka
```

Open Spark container with command:
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
app/spark-consumer-0.0.1.jar kafka:9092 Hello-Kafka
```

Run kafka-producer application in kafka container:
```
java -jar ../app/kafka-producer-0.0.1.jar
```

If you want delete locked builds directories, then run:
```
sudo rm -rf spark-consumer/build kafka-producer/build model-common/build web-api/build
```