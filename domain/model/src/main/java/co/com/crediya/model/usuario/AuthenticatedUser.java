package co.com.crediya.model.usuario;

import co.com.crediya.model.user.Role;

public record AuthenticatedUser(String correoElectronico, Role rol, String documentoIdentidad, String token) {
}
