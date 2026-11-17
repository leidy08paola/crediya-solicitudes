package co.com.bancolombia.usecase.registrarsolicitud;

import co.com.bancolombia.model.solicitud.Solicitud;
import co.com.bancolombia.model.solicitud.gateways.SolicitudRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@RequiredArgsConstructor
public class RegistrarSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;

    public Solicitud registrar(Solicitud solicitud) {
        log.info("Iniciando caso de uso para registrar solicitud de préstamo...");

        // 1. Validar que el tipo de préstamo exista según las reglas de negocio
        boolean existeTipo = solicitudRepository.existsTipoPrestamo(solicitud.getTipoPrestamo());
        if (!existeTipo && solicitud.getTipoPrestamo() != null) {
            // Opcional o validación según criterio: lanzar excepción si no existe
            // throw new IllegalArgumentException("El tipo de préstamo seleccionado no es válido.");
        }

        // 2. Asegurar estado inicial por regla de negocio ("Pendiente de revisión")
        solicitud.setEstadoSolicitud("Pendiente de revisión");

        // 3. Guardar a través del puerto (Gateway)
        Solicitud solicitudGuardada = solicitudRepository.save(solicitud);

        log.info("Solicitud registrada con éxito con ID: " + solicitudGuardada.getId());
        return solicitudGuardada;
    }
}