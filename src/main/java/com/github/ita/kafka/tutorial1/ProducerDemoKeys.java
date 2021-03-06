package com.github.ita.kafka.tutorial1;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ProducerDemoKeys {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Logger logger = LoggerFactory.getLogger(ProducerDemoKeys.class);
        String bootstrapServer = "127.0.0.1:9092";

        // create producer property
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());


        // create producer
        Producer<String, String> producer = new KafkaProducer<>(properties);

        for(int i = 0; i < 10; i++) {

            String topic = "first_topic";
            String value = "hello world"+i;
            String key = "id_"+i;
            // create record
            ProducerRecord<String, String> record
                    = new ProducerRecord<>(topic, key, value);

            logger.info("Key "+key);
            // id_0 is going to Partition:1
            // id_1 is going to Partition:0
            // id_2 is going to Partition:2
            // id_3 is going to Partition:0
            // id_4 is going to Partition:2
            // id_5 is going to Partition:2
            // id_6 is going to Partition:0
            // id_7 is going to Partition:2
            // id_8 is going to Partition:1
            // id_9 is going to Partition:2


            // send data - asynchronous
            producer.send(record, (recordMetadata, e) -> {
                if(e == null){
                    logger.info("Received new metadata. \n" +
                            "Topic:" + recordMetadata.topic() + "\n " +
                            "Partition:" + recordMetadata.partition() + "\n" +
                            "Offset:" + recordMetadata.offset() + "\n" +
                            "TimeStamp:" + recordMetadata.timestamp()
                    );
                }else{
                    logger.error("Error while producing", e);
                }
            }).get(); // block the .send() to make it synchronous - don't do this on production
        }
        // for see result on console
        // flush data from producer
        producer.flush();

        // flush and close producer
        producer.close();
    }

}
