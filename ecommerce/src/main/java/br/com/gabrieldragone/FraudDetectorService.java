package br.com.gabrieldragone;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class FraudDetectorService {

    public static void main(String[] args) {
        System.out.println("Initializing Fraud Detector...");

        var consumer = new KafkaConsumer<>(properties());
        var topic = "ECOMMERCE_NEW_ORDER";

        while (true) { // Apenas para forçar o sistema a continuar buscando as mensagens.
            consumer.subscribe(Collections.singletonList(topic)); // Daria pra escutar de vários tópicos, mas ficaria muito bagunçado.
            var records = consumer.poll(Duration.ofMillis(100));

            if (records.isEmpty()) {
                System.out.println("No records found");
                continue;
            }

            for (var record : records) {
                System.out.println("Processing new order, checking for fraud...");
                System.out.println("Key: " + record.key());
                System.out.println("Value: " + record.value());
                System.out.println("Partition: " + record.partition());
                System.out.println("Offset: " + record.offset());
                System.out.println("Timestamp: " + record.timestamp());
                System.out.println("--------------------------------------------------");

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Order processed");
            }
        }
    }

    private static Properties properties() {
        var properties = new Properties();

        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        // Deserializa Bytes em Strings:
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, FraudDetectorService.class.getSimpleName());

        return properties;
    }
}
