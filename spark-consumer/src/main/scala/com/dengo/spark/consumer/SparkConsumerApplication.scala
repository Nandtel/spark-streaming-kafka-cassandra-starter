package com.dengo.spark.consumer

import com.datastax.spark.connector.cql.CassandraConnector
import com.datastax.spark.connector.streaming._
import com.dengo.model.common.Event
import com.google.gson.Gson
import kafka.serializer.StringDecoder
import org.apache.spark.{SparkConf}
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Object SparkConsumerApplication
  *
  * Object reads event objects from Kafka through Spark Streaming
  * and then saves it to Cassandra
  *
  * For starting this application you need to use spark-starter.sh script
  *
  * @author Dmitry Sheremet
  * @since 0.0.1
  **/
object SparkConsumerApplication {

  private val gson = new Gson()

  /**
    * @param args consists of:
    *             - line of brokers, such as: 'broker1:9092,broker2:9092'
    *             - line of topics, such as: 'topic1,topic2'
    */
  def main(args: Array[String]): Unit = {
    if (args.length < 2)
      System.exit(1)

    val Array(brokers, topics) = args

    SparkLogging.setStreamingLogLevels()

    val sparkConf: SparkConf = new SparkConf().setAppName("SparkConsumer")
//      .setMaster("local[*]")
//      .set("spark.cassandra.connection.host", "172.17.0.2")
    val ssc: StreamingContext = new StreamingContext(sparkConf, Seconds(5))
    ssc.checkpoint("./.temp/")

    CassandraConnector(sparkConf).withSessionDo { session =>
      session.execute("DROP KEYSPACE IF EXISTS api")
      session.execute("CREATE KEYSPACE api WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1}")
      session.execute("CREATE TABLE api.event (id TEXT PRIMARY KEY, name TEXT)")
    }

//    val lines: DStream[String] = ssc.socketTextStream("localhost", 9999)

    val topicsSet: Set[String] = topics.split(",").toSet
    val kafkaParams = Map[String, String]("metadata.broker.list" -> brokers)
    val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
      ssc, kafkaParams, topicsSet)
    val lines: DStream[String] = messages.map(_._2)

    val events: DStream[Event] = lines.map(parser(_, gson, classOf[Event]))

    events.saveToCassandra("api", "event")

    ssc.start()
    ssc.awaitTermination()
  }

  private def parser[T](json: String, gson: Gson, clazz: Class[T]): T = {
    gson.fromJson(json, clazz)
  }
}
