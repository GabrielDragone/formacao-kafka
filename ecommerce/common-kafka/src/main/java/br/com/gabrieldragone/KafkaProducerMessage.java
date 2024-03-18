package br.com.gabrieldragone;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Component
public class KafkaProducerMessage<T> implements Closeable { // <T> Tipo genérico

    private final KafkaProducer<String, T> producer;

    public KafkaProducerMessage() {
        this.producer = new KafkaProducer<>(properties());
    }

    private static Properties properties() {
        var properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9093");
//        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:8097");
        // Serializa Strings em Bytes:
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GsonSerializer.class.getName()); // Criada a serialização via Gson do tipo Genérico
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all"); // Garante que a mensagem foi enviada para todos os brokers.
        return properties;
    }

    public void send(String topicName, String key, T value) throws ExecutionException, InterruptedException {
        var record = new ProducerRecord<>(topicName, key, value);
        Callback callback = (data, exception) -> {
            if (exception != null) {
                exception.printStackTrace();
                return;
            }
            System.out.println("Sent with success " + data.topic() + ":::partition " + data.partition() + "/ offset " + data.offset() + "/ timestamp " + data.timestamp());
        };
        producer.send(record, callback).get(); // Fazemos o código esperar o retorno do Future, após o lider informar que está ok.
    }

    @Override
    public void close() { // Qualquer erro que ocorrer no envio da mensagem, o producer vai fechar a conexão.
        producer.close();
    }
}
