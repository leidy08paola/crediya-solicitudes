package co.com.crediya.model.solicitud.gateways;

import co.com.crediya.model.solicitud.PageResult;
import co.com.crediya.model.solicitud.Solicitud;

import java.util.List;

public interface SolicitudRepository {

    Solicitud save(Solicitud solicitud);

    PageResult<Solicitud> findByEstadoIn(List<co.com.crediya.model.solicitud.EstadoSolicitud> estados,
                                         int page,
                                         int size);
}
