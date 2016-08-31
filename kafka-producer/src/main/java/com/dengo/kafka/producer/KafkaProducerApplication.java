package com.dengo.kafka.producer;

import com.google.gson.Gson;

import com.dengo.model.common.Event;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Random;
import java.util.UUID;

/**
 * @author Sem Babenko
 */
public class KafkaProducerApplication {

    public static void main(String[] args) {

        String[] words = new String[]{"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"};


        Random ran = new Random(System.currentTimeMillis());

        //Assign topicName to string variable
        String topicName = "Hello-Kafka";

        // create instance for properties to access producer configs
        Properties props = new Properties();

        //Assign localhost id
        props.put("bootstrap.servers", "localhost:9092");

        //Set acknowledgements for producer requests.
        props.put("acks", "all");

        //If the request fails, the producer can automatically retry,
        props.put("retries", 0);

        //Specify buffer size in config
        props.put("batch.size", 16384);

        //Reduce the no of requests less than 0
        props.put("linger.ms", 1);

        //The buffer.memory controls the total amount of memory available to the producer for buffering.
        props.put("buffer.memory", 33554432);

        props.put("key.serializer", StringSerializer.class);

        props.put("value.serializer", StringSerializer.class);

        Producer<String, String> producer = new KafkaProducer<>(props);
        Gson gson = new Gson();
        try {

            while (true) {

                producer.send(new ProducerRecord<>(topicName, gson.toJson(initNewEvent(words[ran.nextInt(words.length)]))));
                Thread.sleep(500);
            }
        } catch (Exception ignored) {

        } finally {
            producer.close();
        }
    }

    private static Event initNewEvent(String eventName){
        Event event = new Event(UUID.randomUUID().toString(), eventName);
        return event;
    }
}
