package br.com.gabrieldragone;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("Initializing Kafka Producer...");
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties());
        var topicName = "ECOMMERCE_NEW_ORDER";
        var key = "GAB123";
        var value = "123,456,789";
        var record = new ProducerRecord<>(topicName, key, value);
        Callback callback = (data, exception) -> {
            if (exception != null) {
                exception.printStackTrace();
                return;
            }
            System.out.println("Sent with success " + data.topic() + ":::partition " + data.partition() + "/ offset " + data.offset() + "/ timestamp " + data.timestamp());
        };
        producer.send(record, callback).get(); // Fazemos o código esperar o retorno do Future.

        // Email:
        var emailTopicName = "ECOMMERCE_SEND_EMAIL";
        var emailRecord = new ProducerRecord<>(emailTopicName, key, value);
        producer.send(emailRecord, callback).get(); // Fazemos o código esperar o retorno do Future.
    }

    private static Properties properties() {
        var properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        // Serializa Strings em Bytes:
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }
}