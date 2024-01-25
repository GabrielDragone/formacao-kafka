package br.com.gabrieldragone;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;

public class EmailService {

    public static void main(String[] args) {
        System.out.println("Initializing Email Service...");

        var emailService = new EmailService();
        try (var service = new KafkaConsumerMessage( // Independente se ocorrer erro ou se der certo, fechará a conexão com recurso na sequencia.
                EmailService.class.getSimpleName(),
                "ECOMMERCE_SEND_EMAIL",
                emailService::parse, // Referencia para o método parse.
                String.class,
                Map.of())) { // Properiedades extras se quisermos setar
            service.run();
        }
    }

    private void parse(ConsumerRecord<String, String> record) { // Método referenciado acima
        System.out.println("Sending email...");
        System.out.println("Key: " + record.key());
        System.out.println("Value: " + record.value());
        System.out.println("Partition: " + record.partition());
        System.out.println("Offset: " + record.offset());
        System.out.println("Timestamp: " + record.timestamp());
        System.out.println("--------------------------------------------------");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Email sent");
    }
}
