package co.com.crediya.jpa.repository;

import co.com.crediya.jpa.entity.SolicitudEntity;
import co.com.crediya.model.solicitud.EstadoSolicitud;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SolicitudDataRepository extends CrudRepository<SolicitudEntity, Long> {

    List<SolicitudEntity> findByEstado(EstadoSolicitud estado);
}