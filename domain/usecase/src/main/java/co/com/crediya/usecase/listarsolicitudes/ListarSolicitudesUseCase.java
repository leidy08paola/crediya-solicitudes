package co.com.crediya.usecase.listarsolicitudes;

import co.com.crediya.model.solicitud.EstadoSolicitud;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.gateways.SolicitudRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ListarSolicitudesUseCase {

    private final SolicitudRepository solicitudRepository;

    public List<Solicitud> listar() {
        return solicitudRepository.findAll();
    }

    public List<Solicitud> listarPorEstado(EstadoSolicitud estado) {
        return solicitudRepository.findByEstado(estado);
    }
}
