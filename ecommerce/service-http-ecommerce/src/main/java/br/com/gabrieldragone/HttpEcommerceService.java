package br.com.gabrieldragone;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class HttpEcommerceService {

    public static void main(String[] args) throws Exception {
        System.out.println("Initializing HTTP Ecommerce Service!");

        var context = new ServletContextHandler();
        context.setContextPath("/"); // Definindo o contexto da aplicação.
        context.addServlet(new ServletHolder(NewOrderServlet.class), "/new"); // Adicionando um ponto de entrada para nossa aplicação que será atribuido à classe NewOrderServlet.

        var server = new Server(8080);
        server.start(); // Inicia a aplicação.
        server.join(); // Fica aguardando o servidor terminar para ai finalizarmos nossa aplicação.
    }

}