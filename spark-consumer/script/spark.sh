#!/usr/bin/env bash
### Script which submit Spark application into Spark container
## Author: Sergey Petrovsky


#Switch off HDFS safe mode
hdfs dfsadmin -safemode leave

#Submit our Spark application into Spark container
spark-submit --master $MASTER --conf spark.cassandra.connection.host=cassandra app/spark-consumer-0.0.1.jar kafka:9092 Hello-Kafka