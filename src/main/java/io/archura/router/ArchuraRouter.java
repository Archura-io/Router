package io.archura.router;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

public class ArchuraRouter implements HttpHandler {
    private final HttpClient httpClient = HttpClient
            .newBuilder()
            .executor(Executors.newVirtualThreadPerTaskExecutor())
            .build();

    public static void main(String[] args) throws IOException {
        final ArchuraRouter application = new ArchuraRouter();
        application.start();
    }

    private void start() throws IOException {
        final InetSocketAddress serverAddress = new InetSocketAddress("0.0.0.0", 8080);
        final HttpServer localhost = HttpServer.create(serverAddress, 100);
        localhost.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        localhost.createContext("/", this);
        localhost.start();

        log("Server started.");
    }

    private static void log(String message) {
        System.out.println(message);
    }

    @Override
    public void handle(HttpExchange exchange) {
        handleRequest(exchange);
    }

    private void handleRequest(final HttpExchange exchange) {
        try {
            final HttpRequest httpRequest = HttpRequest.newBuilder().GET().uri(URI.create("http://192.168.68.118:9090/test.txt")).build();
            final HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            final String responseMessage = httpResponse.body();
            exchange.sendResponseHeaders(200, responseMessage.length());
            exchange.getResponseBody().write(responseMessage.getBytes(StandardCharsets.UTF_8));
            exchange.getResponseBody().close();
        } catch (Exception e) {
            final String errorMessage = String.valueOf(e.getMessage());
            log("Error: %s, message: %s".formatted(e.getClass().getSimpleName(), errorMessage));
            try {
                exchange.sendResponseHeaders(500, errorMessage.length());
                exchange.getResponseBody().write(errorMessage.getBytes(StandardCharsets.UTF_8));
                exchange.getResponseBody().close();
            } catch (Exception error) {
                log("Error: %s, message: %s".formatted(e.getClass().getSimpleName(), errorMessage));
            }
        } finally {
            exchange.close();
        }
    }
}
