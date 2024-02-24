package br.com.gabrieldragone.servicehttpecommercespring

import br.com.gabrieldragone.KafkaProducerMessage
import org.springframework.stereotype.Service

@Service
class OderServiceImpl constructor(
    private var orderDispatcher: KafkaProducerMessage<Order>,
    private var emailDispatcher: KafkaProducerMessage<String>
): OrderService {

    private val NEW_ORDER_TOPIC = "ECOMMERCE_NEW_ORDER"
    private val SEND_EMAIL_TOPIC = "ECOMMERCE_SEND_EMAIL"

    override fun createOrder(order: Order) {
        orderDispatcher.send(NEW_ORDER_TOPIC, order.email, order)
        emailDispatcher.send(SEND_EMAIL_TOPIC, order.email, "Thank you for your order! We are processing your order!")
    }

}