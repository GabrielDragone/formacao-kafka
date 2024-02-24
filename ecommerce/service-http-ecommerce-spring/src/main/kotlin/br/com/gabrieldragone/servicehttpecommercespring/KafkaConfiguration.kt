package br.com.gabrieldragone.servicehttpecommercespring

import br.com.gabrieldragone.KafkaProducerMessage
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan("br.com.gabrieldragone")
class KafkaConfiguration {

    @Bean
    fun orderDispatcher(): KafkaProducerMessage<Order> {
        return KafkaProducerMessage()
    }

    @Bean
    fun emailDispatcher(): KafkaProducerMessage<String> {
        return KafkaProducerMessage()
    }

}