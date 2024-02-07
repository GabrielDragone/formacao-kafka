package br.com.gabrieldragone;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class FraudDetectorService {

    private final KafkaProducerMessage<Order> orderKafkaProducer = new KafkaProducerMessage<>();

    public static void main(String[] args) {
        System.out.println("Initializing Fraud Detector...");

        var fraudDetectorService = new FraudDetectorService();
        try (var kafkaService = new KafkaConsumerMessage<>( // Independente se ocorrer erro ou se der certo, fechará a conexão com recurso na sequencia.
                FraudDetectorService.class.getSimpleName(),
                "ECOMMERCE_NEW_ORDER",
                fraudDetectorService::parse, // Referencia para o método parse.
                Order.class, // O tipo que estamos esperando para deserializar a mensagem
                new HashMap<>())) { // Properiedades extras se quisermos setar
            kafkaService.run();
        }
    }

    private void parse(ConsumerRecord<String, Order> record) throws ExecutionException, InterruptedException { //
        System.out.println("Processing new order, checking for fraud...");
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

        var order = record.value();
        if (isFraud(order)) {
            System.out.println("Fraud detected! Order: " + order);
            orderKafkaProducer.send("ECOMMERCE_ORDER_REJECTED", order.userId(), order);
        } else {
            System.out.println("Approved: " + order);
            orderKafkaProducer.send("ECOMMERCE_ORDER_APPROVED", order.userId(), order);
        }

        System.out.println("Order processed");
    }

    private static boolean isFraud(Order order) {
        return order.amount().compareTo(new BigDecimal("4500")) >= 0;
    }

}
