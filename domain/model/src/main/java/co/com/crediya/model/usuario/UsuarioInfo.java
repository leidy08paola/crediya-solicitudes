package co.com.crediya.model.usuario;

import java.math.BigDecimal;

/**
 * Información de un usuario consultada en el microservicio AUTENTICACION.
 */
public record UsuarioInfo(String correoElectronico, String nombre, BigDecimal salarioBase) {
}
