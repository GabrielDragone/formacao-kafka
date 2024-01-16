package br.com.gabrieldragone;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Properties;
import java.util.regex.Pattern;

public class LogService {

    public static void main(String[] args) {
        System.out.println("Logging Service...");

        var consumer = new KafkaConsumer<>(properties());
        var topic = Pattern.compile("ECOMMERCE.*"); // Expressão regular para informar que estamos ouvindo todos os tópicos que tenham ECOMMERCE

        while (true) { // Apenas para forçar o sistema a continuar buscando as mensagens.
            consumer.subscribe(topic); // Daria pra escutar de vários tópicos, mas ficaria muito bagunçado.
            var records = consumer.poll(Duration.ofMillis(100));

            if (records.isEmpty()) {
                System.out.println("No records found");
                continue;
            }

            for (var record : records) {
                System.out.println("LOG:" + record.topic());
                System.out.println("Key: " + record.key());
                System.out.println("Value: " + record.value());
                System.out.println("Partition: " + record.partition());
                System.out.println("Offset: " + record.offset());
                System.out.println("Timestamp: " + record.timestamp());
                System.out.println("--------------------------------------------------");
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
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, LogService.class.getSimpleName());

        return properties;
    }
}
