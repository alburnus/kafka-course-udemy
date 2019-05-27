package com.github.alburnus.kafka.tutorial;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ProducerDemoKeys {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final Logger logger = LoggerFactory.getLogger(ProducerDemoKeys.class);

        // Producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.26:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // Create the Producer <Key, Value>
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);


        // Just for example
        for (int i = 0; i < 10; i++) {
            // create the producer record
            String topic = "first_topic";
            String value = "Hello Java " + Integer.toString(i);
            String key = "id_" + Integer.toString(i);

            // Key make that record always go to the same Partition!
            final ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, key, value);

            logger.info("Key: " + key);
            // Send data
            producer.send(record, new Callback() {
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    // execute every time a recordis sent or exception
                    if (exception == null) {
                        logger.info("Received new metadata. \n" +
                                "Topic: " + metadata.topic() + "\n" +
                                "Partition: " + metadata.partition() + "\n" +
                                "Offset: " + metadata.offset() + "\n" +
                                "Timestamp: " + metadata.timestamp() + "\n"
                        );
                    } else {
                        logger.error("Error: ", exception);
                    }
                }
            }).get(); // block the send() to make it synchronous - don't do this on production!
        }
        producer.close();
    }
}
