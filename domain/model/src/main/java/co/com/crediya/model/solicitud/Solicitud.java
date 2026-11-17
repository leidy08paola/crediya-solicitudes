package co.com.crediya.model.solicitud;

import co.com.crediya.model.tipoprestamo.TipoPrestamo;
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
    private TipoPrestamo tipoPrestamo;
    private EstadoSolicitud estado;
}