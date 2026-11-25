package co.com.crediya.model.usuario.gateways;

import co.com.crediya.model.usuario.UsuarioInfo;

import java.util.Optional;

public interface UsuarioGateway {

    Optional<UsuarioInfo> obtenerPorDocumento(String documentoIdentidad, String authorization);
}
