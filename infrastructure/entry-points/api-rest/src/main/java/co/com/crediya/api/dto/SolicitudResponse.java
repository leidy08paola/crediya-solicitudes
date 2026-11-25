package co.com.crediya.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record SolicitudResponse(
        @JsonProperty("id") Long id,
        @JsonProperty("documento_identidad") String documentoIdentidad,
        @JsonProperty("monto") BigDecimal monto,
        @JsonProperty("plazo") Integer plazo,
        @JsonProperty("tipo_prestamo") String tipoPrestamo,
        @JsonProperty("tasa_interes") BigDecimal tasaInteres,
        @JsonProperty("estado") String estado,
        @JsonProperty("estado_solicitud") String estadoSolicitud,
        @JsonProperty("email") String email,
        @JsonProperty("nombre") String nombre,
        @JsonProperty("salario_base") BigDecimal salarioBase,
        @JsonProperty("deuda_total_mensual_solicitudes_aprobadas") BigDecimal deudaTotalMensualSolicitudesAprobadas
) {
}