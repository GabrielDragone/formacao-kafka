package br.com.gabrieldragone;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderServlet extends HttpServlet {

    // Ao invés de todas as vezes fazermos a instanciação dos dispatchers via try catch, podemos fazer a instanciação direto na declaração.
    private final KafkaProducerMessage<Order> orderDispatcher = new KafkaProducerMessage<>();
    private final KafkaProducerMessage<String> emailDispatcher = new KafkaProducerMessage<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    public void destroy() {
        super.destroy();
        orderDispatcher.close();
        emailDispatcher.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            var email = req.getParameter("email");
            var amount = new BigDecimal(req.getParameter("amount"));

            var orderId = UUID.randomUUID().toString();

            var order = new Order(orderId, amount, email);

            var topicName = "ECOMMERCE_NEW_ORDER";
            orderDispatcher.send(topicName, email, order);

            var emailTopicName = "ECOMMERCE_SEND_EMAIL";
            var emailToSend = "Thank you for your order! We are processing it!";
            emailDispatcher.send(emailTopicName, email, emailToSend);

            System.out.println("New order sent successfully!");

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("New order sent successfully!");
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
