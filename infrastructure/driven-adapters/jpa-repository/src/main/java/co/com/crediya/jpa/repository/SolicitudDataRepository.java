package co.com.crediya.jpa.repository;

import co.com.crediya.jpa.entity.SolicitudEntity;
import org.springframework.data.repository.CrudRepository;

public interface SolicitudDataRepository extends CrudRepository<SolicitudEntity, Long> {
}