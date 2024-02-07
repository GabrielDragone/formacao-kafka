package br.com.gabrieldragone;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class KafkaConsumerMessage<T> implements Closeable {

    private final KafkaConsumer consumer;
    private final ConsumerFunction parse;

    public KafkaConsumerMessage(String groupId, String topic, ConsumerFunction parse, Class<T> classType,
                                Map<String, String> extraProperties) {
        this(parse, groupId, classType, extraProperties);
        consumer.subscribe(Collections.singletonList(topic)); // Daria pra escutar de vários tópicos, mas ficaria muito bagunçado.
    }

    public KafkaConsumerMessage(String groupId, Pattern topic, ConsumerFunction parse, Class<T> classType,
                                Map<String, String> extraProperties) {
        this(parse, groupId, classType, extraProperties);
        consumer.subscribe(topic);
    }

    private KafkaConsumerMessage(ConsumerFunction parse, String groupId, Class<T> classType,
                                 Map<String, String> extraProperties) { // Inicializa os dois campos para serem usados nos construtores acima.
        this.parse = parse;
        this.consumer = new KafkaConsumer<>(setProperties(classType, groupId, extraProperties));
    }

    public void run() {
        while (true) { // Apenas para forçar o sistema a continuar buscando as mensagens.
            var records = consumer.poll(Duration.ofMillis(100));
            if (!records.isEmpty()) {
                System.out.println("Founded " + records.count() + " records"); // Como ta com o max poll 1, vai encontrar apenas 1.
                for (var record : records) {
                    try {
                        parse.consume((ConsumerRecord<String, T>) record);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private Properties setProperties(Class<T> classType, String groupId, Map<String, String> overrideProperties) {
        var properties = new Properties();

        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        // Deserializa Bytes em Strings:
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
//        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
//        properties.setProperty(GsonDeserializer.TYPE_CONFIG, String.class.getName()); // Configuração para o GsonDeserializer saber qual classe ele vai deserializar.
        properties.setProperty(GsonDeserializer.TYPE_CONFIG, classType.getName()); // Configuração para o GsonDeserializer saber qual classe ele vai deserializar de forma dinamica.

        // Adiciona as propriedades extras que foram passadas no construtor. No caso abaixo, sobescreve VALUE_DESERIALIZER_CLASS_CONFIG:
        properties.putAll(overrideProperties);
        return properties;
    }

    @Override
    public void close() {
        consumer.close();
    }
}
