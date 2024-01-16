package br.com.gabrieldragone;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        System.out.println("Initializing Kafka Producer...");

       try (var dispatcher = new KafkaDispatcher()) { // Qualquer erro que ocorrer ou se executar com sucesso, o KafkaDispatcher vai fechar a conexão. Método close().
           for(var i = 0; i < 10; i++) {
               var topicName = "ECOMMERCE_NEW_ORDER";
               var key = UUID.randomUUID().toString();
               var value = "123,456,789";

               dispatcher.send(topicName, key, value);

               var emailTopicName = "ECOMMERCE_SEND_EMAIL";
               dispatcher.send(emailTopicName, key, value);
           }
       }

    }
}