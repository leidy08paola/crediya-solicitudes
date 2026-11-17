package co.com.bancolombia.model.solicitud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Solicitud {
    private Long id;
    private String documentoIdentidad;
    private BigDecimal monto;
    private Integer plazo;
    private String tipoPrestamo;
    @Builder.Default
    private String estadoSolicitud = "Pendiente de revisión";
}