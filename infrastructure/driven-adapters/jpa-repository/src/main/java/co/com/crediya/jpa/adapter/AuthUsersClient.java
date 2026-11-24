package co.com.crediya.jpa.adapter;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthUsersClient {

    private static final String DEFAULT_AUTH_URL = "http://localhost:8080";
    private static final Pattern EMAIL_PATTERN = Pattern.compile("\"correo_electronico\"\\s*:\\s*\"([^\"]+)\"");
    private static final Pattern NOMBRES_PATTERN = Pattern.compile("\"nombres\"\\s*:\\s*\"([^\"]+)\"");
    private static final Pattern APELLIDOS_PATTERN = Pattern.compile("\"apellidos\"\\s*:\\s*\"([^\"]+)\"");
    private static final Pattern SALARIO_PATTERN = Pattern.compile("\"salario_base\"\\s*:\\s*([\\d.]+)");

    private final HttpClient httpClient;
    private final String authUrl;

    public AuthUsersClient() {
        this.httpClient = HttpClient.newHttpClient();
        String envUrl = System.getenv("CREDIYA_AUTH_URL");
        this.authUrl = (envUrl != null && !envUrl.isBlank()) ? envUrl : DEFAULT_AUTH_URL;
    }

    public Map<String, Object> findByDocumentoIdentidad(String documentoIdentidad) {
        try {
            HttpRequest request = HttpRequest.newBuilder(
                            URI.create(authUrl + "/api/v1/usuarios/" + documentoIdentidad))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                return Map.of();
            }
            String body = response.body();
            Map<String, Object> user = new HashMap<>();
            putIfMatch(user, body, EMAIL_PATTERN, "email");
            putIfMatch(user, body, NOMBRES_PATTERN, "nombre");
            putIfMatch(user, body, APELLIDOS_PATTERN, "apellido");
            Matcher salarioMatcher = SALARIO_PATTERN.matcher(body);
            if (salarioMatcher.find()) {
                try {
                    user.put("salarioBase", new BigDecimal(salarioMatcher.group(1)));
                } catch (NumberFormatException ignored) {
                    // sin salario numérico: dejar sin completar
                }
            }
            return user;
        } catch (Exception ex) {
            // Sin conexión a AUTH: devolver mapa vacío (la solicitud sigue sin enriquecer)
            return Map.of();
        }
    }

    private static void putIfMatch(Map<String, Object> user, String body, Pattern pattern, String key) {
        Matcher matcher = pattern.matcher(body);
        if (matcher.find() && matcher.group(1) != null && !matcher.group(1).isBlank()) {
            user.put(key, matcher.group(1));
        }
    }
}
