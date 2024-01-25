package br.com.gabrieldragone;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class GsonDeserializer<T> implements Deserializer<T> {

    public static final String TYPE_CONFIG = "br.com.gabrieldragone.type_config";
    private final Gson gson = new GsonBuilder().create();
    private Class<T> type; // Força a classe a ser do tipo T linha 19.

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        String typeName = String.valueOf(configs.get(TYPE_CONFIG));  // valueOf se for null devolve "null".
        try {
            this.type = (Class<T>) Class.forName(typeName); // Carrega a classe pelo nome. Se não encontrar a classe, vai dar Exception.
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Type for deserialize was not found in the classpath");
        }
    }

    @Override
    public T deserialize(String s, byte[] bytes) {
        return gson.fromJson(new String(bytes), type);
    }
}
