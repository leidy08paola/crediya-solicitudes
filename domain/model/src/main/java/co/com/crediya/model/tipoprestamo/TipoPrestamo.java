package co.com.crediya.model.tipoprestamo;

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
public class TipoPrestamo {
    private Long id;
    private String nombre;
    private BigDecimal tasaInteres;
    private boolean validacionAutomatica;
}