package br.com.gabrieldragone;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ConsumerFunction<T> {
//    void consume(ConsumerRecord<String, T> record) throws ExecutionException, InterruptedException, SQLException;
    void consume(ConsumerRecord<String, T> record) throws Exception; // Evitar usar dessa forma genérica
}
