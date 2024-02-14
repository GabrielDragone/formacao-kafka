package br.com.gabrieldragone;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class CreateUserService {

    private final Connection connection;

    CreateUserService() throws SQLException {
        System.out.println("Initializing CreateUserService...");
        String url = "jdbc:sqlite:ecommerce/target/users_database.db"; // Pasta onde o arquivo será criado.
        connection = DriverManager.getConnection(url); // Cria e pega a conexão com o banco de dados.
        try {
            connection.createStatement().execute("create table if not exists Users (" +
                    "uuid varchar(200) primary key, " +
                    "email varchar(200))");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws SQLException {

        System.out.println("Initializing Fraud Detector...");

        var createUserService = new CreateUserService();
        try (var kafkaService = new KafkaConsumerMessage<>( // Independente se ocorrer erro ou se der certo, fechará a conexão com recurso na sequencia.
                CreateUserService.class.getSimpleName(), // Consumer Group.
                "ECOMMERCE_NEW_ORDER",
                createUserService::parse, // Referencia para o método parse.
                Order.class, // O tipo que estamos esperando para deserializar a mensagem
                Map.of())) { // Properiedades extras se quisermos setar
            kafkaService.run();
        }
    }

    private void parse(ConsumerRecord<String, Order> record) throws SQLException { //
        System.out.println("Processing new order, checking for new user...");
        System.out.println("Value: " + record.value());

        var order = record.value();

        var email = order.email();

        if (isNewUser(email)) {
            insertNewUser(order.userId(), email);
        }

        System.out.println("Order processed");
    }

    private void insertNewUser(String uuid, String email) throws SQLException {
        var insert = connection.prepareStatement("insert into Users (uuid, email) values (?, ?)");
        insert.setString(1, uuid);
        insert.setString(2, email);
        insert.execute();
        System.out.println("User with email " + email + " inserted");
    }

    private Boolean isNewUser(String email) throws SQLException {
        var exists = connection.prepareStatement("select uuid from Users where email = ? limit 1");
        exists.setString(1, email);
        var results = exists.executeQuery();
        return !results.next(); // Se não existe, então é um usuario novo
    }
}