package br.com.gabrieldragone.servicehttpecommercespring

import jakarta.websocket.server.PathParam
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.util.*

@RestController
class NewOrderController(
    private val orderService: OrderService
) {

    @GetMapping
    fun helloWorld(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello World")
    }

    @GetMapping("/new-order")
    fun createOrder(
        @RequestParam("email") email: String,
        @RequestParam("amount") value: BigDecimal
    ) {
        val orderId = UUID.randomUUID().toString()
        val order = Order(orderId, value, email)
        orderService.createOrder(order)
    }

}