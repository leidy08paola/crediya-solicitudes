package co.com.crediya.jpa.repository;

import co.com.crediya.jpa.entity.TipoPrestamoEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TipoPrestamoDataRepository extends CrudRepository<TipoPrestamoEntity, Long> {
    Optional<TipoPrestamoEntity> findByNombre(String nombre);
}