package br.com.gabrieldragone.servicehttpecommercespring;

import java.math.BigDecimal;

public record Order(String orderId, BigDecimal amount, String email) { }