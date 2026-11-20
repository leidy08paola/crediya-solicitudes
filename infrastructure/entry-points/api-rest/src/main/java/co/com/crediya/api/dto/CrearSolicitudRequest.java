package co.com.crediya.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "Datos para registrar una solicitud de préstamo")
public record CrearSolicitudRequest(
        @NotBlank(message = "El campo documento_identidad es obligatorio")
        @Size(max = 20, message = "El campo documento_identidad no puede superar 20 caracteres")
        @JsonProperty("documento_identidad") String documentoIdentidad,

        @NotNull(message = "El campo monto es obligatorio")
        @Positive(message = "El campo monto debe ser mayor que cero")
        @Digits(integer = 10, fraction = 2, message = "El campo monto admite máximo 2 decimales")
        @JsonProperty("monto") BigDecimal monto,

        @NotNull(message = "El campo plazo es obligatorio")
        @Positive(message = "El campo plazo debe ser mayor que cero")
        @JsonProperty("plazo") Integer plazo,

        @NotBlank(message = "El campo tipo_prestamo es obligatorio")
        @Size(max = 100, message = "El campo tipo_prestamo no puede superar 100 caracteres")
        @JsonProperty("tipo_prestamo") String tipoPrestamo
) {
}