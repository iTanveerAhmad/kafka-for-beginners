package com.github.ita.kafka.tutorial1;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ConsumerDemo {

    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(ConsumerDemo.class);
        String bootstrapServer = "127.0.0.1:9092";
        String groupId = "my-fourth-application";
        String topic = "first_topic";

        // create consumer config
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // create consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // subscribe consumer to our topic(s)
        consumer.subscribe(Collections.singleton(topic));

        // poll for new data
        while(true){

            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

            for(ConsumerRecord<String, String> record : records){

                logger.info("Key: "+record.key() + ", ValueL: "+record.value() );
                logger.info( "partition: "+record.partition() + ", Offset: "+ record.offset() );

            }


        }

    }

}
