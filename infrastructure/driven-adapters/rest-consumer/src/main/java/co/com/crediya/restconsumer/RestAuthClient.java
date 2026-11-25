package co.com.crediya.restconsumer;

import co.com.crediya.model.usuario.UsuarioInfo;
import co.com.crediya.model.usuario.gateways.UsuarioGateway;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;

/**
 * Driven adapter que consulta usuarios a AUTENTICACION via REST
 * (GET {autenticacion.base-url}/api/v1/usuarios/{documentoIdentidad}).
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RestAuthClient implements UsuarioGateway {

    @Value("${jwt.auth-url:http://localhost:8080}")
    private String authUrl;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(3))
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Optional<UsuarioInfo> obtenerPorDocumento(String documentoIdentidad, String authorization) {
        try {
            URI uri = URI.create(authUrl + "/api/v1/usuarios/" + documentoIdentidad);
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(uri).GET();
            if (authorization != null) {
                requestBuilder.header("Authorization", authorization);
            }
            HttpResponse<String> response = httpClient.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != HttpStatus.OK.value()) {
                return Optional.empty();
            }
            JsonNode json = objectMapper.readTree(response.body());
            if (json == null) {
                return Optional.empty();
            }
            return Optional.of(new UsuarioInfo(
                    json.path("correo_electronico").asText(null),
                    json.path("nombres").asText(null),
                    json.path("salario_base").decimalValue()));
        } catch (Exception ex) {
            log.error("Error consultando usuario en AUTENTICACION: {}", ex.getMessage());
            throw new co.com.crediya.restconsumer.UsuarioAuthException(
                    HttpStatus.SERVICE_UNAVAILABLE, "AUTENTICACION no disponible", ex);
        }
    }
}
