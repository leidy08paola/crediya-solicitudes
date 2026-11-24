package co.com.crediya.model.solicitud.gateways;

import co.com.crediya.model.solicitud.EstadoSolicitud;
import co.com.crediya.model.solicitud.Solicitud;

import java.util.List;

public interface SolicitudRepository {

    Solicitud save(Solicitud solicitud);

    List<Solicitud> findAll();

    List<Solicitud> findByEstado(EstadoSolicitud estado);
}