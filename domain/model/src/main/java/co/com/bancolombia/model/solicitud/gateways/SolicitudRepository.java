package co.com.bancolombia.model.solicitud.gateways;

import co.com.bancolombia.model.solicitud.Solicitud;

public interface SolicitudRepository {
    Solicitud save(Solicitud solicitud);
    boolean existsTipoPrestamo(String tipoPrestamo);
}