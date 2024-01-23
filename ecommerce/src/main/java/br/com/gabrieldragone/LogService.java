package br.com.gabrieldragone;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Map;
import java.util.regex.Pattern;

public class LogService {

    public static void main(String[] args) {
        System.out.println("Logging Service...");

        var logService = new LogService();
        try (var service = new KafkaConsumerMessage(
                LogService.class.getSimpleName(),
                Pattern.compile("ECOMMERCE.*"), // .* = Regex para pegar todos os tópicos que começam com ECOMMERCE
                logService::parse,
                String.class,
                Map.of(
                        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()
                        //, Poderiamos colocar mais configurações aqui, adicionando virgula.
                ))) { // Configuração para o GsonDeserializer saber qual classe ele vai deserializar de forma dinamica.
            service.run();
        }
    }

    private void parse(ConsumerRecord<String, String> record) {
        System.out.println("Logging...");
        System.out.println("Key: " + record.key());
        System.out.println("Value: " + record.value());
        System.out.println("Partition: " + record.partition());
        System.out.println("Offset: " + record.offset());
        System.out.println("Timestamp: " + record.timestamp());
        System.out.println("--------------------------------------------------");
    }

}
