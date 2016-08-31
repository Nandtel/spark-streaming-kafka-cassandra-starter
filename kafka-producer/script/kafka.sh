#!/usr/bin/env bash
### Script which run Kafka application
## Author: Sergey Petrovsky


#Create topic
kafka-topics.sh --create --zookeeper $ZOOKEEPER --replication-factor 1 --partitions 2 --topic Hello-Kafka

#Run Kafka application
java -jar ../app/kafka-producer-0.0.1.jar