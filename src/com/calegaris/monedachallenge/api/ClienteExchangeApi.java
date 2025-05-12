package com.calegaris.monedachallenge.api;

import com.calegaris.monedachallenge.modelos.Moneda;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

public class ClienteExchangeApi {

    private final String apiKey;

    public ClienteExchangeApi(){
        this.apiKey = cargarApiKey();
    }

    private String cargarApiKey() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("No se encontró el archivo config.properties");
            }
            properties.load(input);
            return properties.getProperty("api.key");
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar el archivo config.properties", e);
        }

    }

    public Moneda convertirMoneda(String monedaBase, String monedaDestino) throws IOException, InterruptedException {
        String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/pair/" + monedaBase + "/" + monedaDestino;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Gson gson = new Gson();
        return gson.fromJson(response.body(), Moneda.class); // Podés parsearlo con Gson si querés usar objetos
    }




}
