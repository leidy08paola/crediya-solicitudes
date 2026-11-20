package co.com.crediya.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record SolicitudResponse(
        @JsonProperty("id") Long id,
        @JsonProperty("documento_identidad") String documentoIdentidad,
        @JsonProperty("monto") BigDecimal monto,
        @JsonProperty("plazo") Integer plazo,
        @JsonProperty("tipo_prestamo") String tipoPrestamo,
        @JsonProperty("estado") String estado
) {
}