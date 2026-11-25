package co.com.crediya.usecase.registrarsolicitud;

import co.com.crediya.model.solicitud.EstadoSolicitud;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.exception.BusinessException;
import co.com.crediya.model.solicitud.exception.ErrorCode;
import co.com.crediya.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.model.tipoprestamo.TipoPrestamo;
import co.com.crediya.model.tipoprestamo.gateways.TipoPrestamoRepository;
import co.com.crediya.model.usuario.AuthenticatedUser;
import co.com.crediya.model.usuario.UsuarioInfo;
import co.com.crediya.model.usuario.gateways.UsuarioGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;

@Log
@RequiredArgsConstructor
public class RegistrarSolicitudUseCase {

    private static final EstadoSolicitud ESTADO_INICIAL = EstadoSolicitud.PENDIENTE_REVISION;

    private final SolicitudRepository solicitudRepository;
    private final TipoPrestamoRepository tipoPrestamoRepository;
    private final UsuarioGateway usuarioGateway;

    public Solicitud registrar(Solicitud solicitud, AuthenticatedUser authenticatedUser) {
        Objects.requireNonNull(solicitud, "La solicitud no puede ser nula");
        log.info("Iniciando registro de solicitud de préstamo");
        validateSolicitud(solicitud);

        TipoPrestamo tipoPrestamo = obtenerTipoPrestamo(solicitud.getTipoPrestamo().getNombre());

        Solicitud nueva = solicitud.toBuilder()
                .tipoPrestamo(tipoPrestamo)
                .estado(ESTADO_INICIAL)
                .build();
        enriquecerConUsuario(nueva, authenticatedUser);

        Solicitud guardada = solicitudRepository.save(nueva);
        log.log(Level.INFO, "Solicitud registrada con id {0}", String.valueOf(guardada.getId()));
        return guardada;
    }

    private void validateSolicitud(Solicitud solicitud) {
        if (solicitud.getDocumentoIdentidad() == null || solicitud.getDocumentoIdentidad().isBlank()) {
            throw new BusinessException(ErrorCode.REQUIRED_FIELD_EMPTY, "documento_identidad");
        }
        if (solicitud.getMonto() == null || solicitud.getMonto().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ErrorCode.AMOUNT_TOO_LOW);
        }
        if (solicitud.getPlazo() == null || solicitud.getPlazo() <= 0) {
            throw new BusinessException(ErrorCode.PLAZO_INVALID);
        }
    }

    private void enriquecerConUsuario(Solicitud solicitud, AuthenticatedUser authenticatedUser) {
        String documento = solicitud.getDocumentoIdentidad();
        if (documento == null || authenticatedUser == null) {
            return;
        }
        Optional<UsuarioInfo> usuario = usuarioGateway.obtenerPorDocumento(documento, authenticatedUser.token());
        usuario.ifPresent(info -> {
            if (info.correoElectronico() != null) {
                solicitud.setEmail(info.correoElectronico());
            }
            if (info.nombre() != null) {
                solicitud.setNombre(info.nombre());
            }
            if (info.salarioBase() != null) {
                solicitud.setSalarioBase(info.salarioBase());
            }
        });
    }

    private TipoPrestamo obtenerTipoPrestamo(String nombre) {
        return tipoPrestamoRepository.findByNombre(nombre)
                .orElseThrow(() -> {
                    log.warning("Tipo de préstamo inexistente en la solicitud");
                    return new BusinessException(ErrorCode.TIPO_PRESTAMO_NO_ENCONTRADO);
                });
    }
}