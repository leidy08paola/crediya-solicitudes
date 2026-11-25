package co.com.crediya.usecase.listarsolicitudes;

import co.com.crediya.model.solicitud.EstadoSolicitud;
import co.com.crediya.model.solicitud.PageResult;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.gateways.SolicitudRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ListarSolicitudesUseCase {

    private static final List<EstadoSolicitud> ESTADOS_PENDIENTES =
            List.of(EstadoSolicitud.PENDIENTE_REVISION, EstadoSolicitud.RECHAZADA, EstadoSolicitud.REVISION_MANUAL);

    private final SolicitudRepository solicitudRepository;

    public PageResult<Solicitud> listar(int page, int size) {
        return solicitudRepository.findByEstadoIn(ESTADOS_PENDIENTES, page, size);
    }

    public PageResult<Solicitud> listarPorEstado(EstadoSolicitud estado, int page, int size) {
        return solicitudRepository.findByEstadoIn(List.of(estado), page, size);
    }
}
