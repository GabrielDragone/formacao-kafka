package br.com.gabrieldragone;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Pattern;

public class KafkaService implements Closeable {

    private final KafkaConsumer consumer;
    private final ConsumerFunction parse;

    public KafkaService(String groupId, String topic, ConsumerFunction parse) {
        this(parse, groupId);
        consumer.subscribe(Collections.singletonList(topic)); // Daria pra escutar de vários tópicos, mas ficaria muito bagunçado.
    }

    public KafkaService(String groupId, Pattern topic, ConsumerFunction parse) {
        this(parse, groupId);
        consumer.subscribe(Collections.singletonList(topic));
    }

    private KafkaService(ConsumerFunction parse, String groupId) { // Inicializa os dois campos para serem usados nos construtores acima.
        this.parse = parse;
        this.consumer = new KafkaConsumer<>(properties(groupId));
    }

    public void run(){
        while (true) { // Apenas para forçar o sistema a continuar buscando as mensagens.
            ConsumerRecords records = consumer.poll(Duration.ofMillis(100));
            if (!records.isEmpty()) {
                System.out.println("Founded " + records.count() + " records"); // Como ta com o max poll 1, vai encontrar apenas 1.
                for (var record : records) {
                    parse.consume((ConsumerRecord<String, String>) record);
                }
            }
        }
    }

    private static Properties properties(String groupId) {
        var properties = new Properties();

        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        // Deserializa Bytes em Strings:
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());

        return properties;
    }

    @Override
    public void close() {
        consumer.close();
    }
}
