package br.com.gabrieldragone;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        System.out.println("Initializing Kafka Producer...");

        try (var orderDispatcher = new KafkaProducerMessage<Order>()) { // Qualquer erro que ocorrer ou se executar com sucesso, o KafkaDispatcher vai fechar a conexão. Método close().
            try (var emailDispatcher = new KafkaProducerMessage<String>()) {
                for (var i = 0; i < 10; i++) {
                    var userId = UUID.randomUUID().toString();
                    var orderId = UUID.randomUUID().toString();
                    var amount = BigDecimal.valueOf(Math.random() * 5000 + 1);

                    var order = new Order(userId, orderId, amount);

                    var topicName = "ECOMMERCE_NEW_ORDER";
                    orderDispatcher.send(topicName, userId, order);

                    var emailTopicName = "ECOMMERCE_SEND_EMAIL";
                    var email = "Thank you for your order! We are processing it!";
                    emailDispatcher.send(emailTopicName, userId, email);
                }
            }
        }

    }
}