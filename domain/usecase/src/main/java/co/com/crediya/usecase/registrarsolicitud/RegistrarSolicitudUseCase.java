package co.com.crediya.usecase.registrarsolicitud;

import co.com.crediya.model.solicitud.EstadoSolicitud;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.exception.BusinessException;
import co.com.crediya.model.solicitud.exception.ErrorCode;
import co.com.crediya.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.model.tipoprestamo.TipoPrestamo;
import co.com.crediya.model.tipoprestamo.gateways.TipoPrestamoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.Objects;
import java.util.logging.Level;

@Log
@RequiredArgsConstructor
public class RegistrarSolicitudUseCase {

    private static final EstadoSolicitud ESTADO_INICIAL = EstadoSolicitud.PENDIENTE_REVISION;

    private final SolicitudRepository solicitudRepository;
    private final TipoPrestamoRepository tipoPrestamoRepository;

    public Solicitud registrar(Solicitud solicitud) {
        Objects.requireNonNull(solicitud, "La solicitud no puede ser nula");
        log.info("Iniciando registro de solicitud de préstamo");

        TipoPrestamo tipoPrestamo = obtenerTipoPrestamo(solicitud.getTipoPrestamo().getNombre());

        Solicitud nueva = solicitud.toBuilder()
                .tipoPrestamo(tipoPrestamo)
                .estado(ESTADO_INICIAL)
                .build();

        Solicitud guardada = solicitudRepository.save(nueva);
        log.log(Level.INFO, "Solicitud registrada con id {0}", String.valueOf(guardada.getId()));
        return guardada;
    }

    private TipoPrestamo obtenerTipoPrestamo(String nombre) {
        return tipoPrestamoRepository.findByNombre(nombre)
                .orElseThrow(() -> {
                    log.warning("Tipo de préstamo inexistente en la solicitud");
                    return new BusinessException(ErrorCode.TIPO_PRESTAMO_NO_ENCONTRADO);
                });
    }
}