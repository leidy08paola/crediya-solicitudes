package co.com.crediya.jpa.adapter;

import co.com.crediya.jpa.repository.TipoPrestamoDataRepository;
import co.com.crediya.model.tipoprestamo.TipoPrestamo;
import co.com.crediya.model.tipoprestamo.gateways.TipoPrestamoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TipoPrestamoRepositoryAdapter implements TipoPrestamoRepository {

    private final TipoPrestamoDataRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Optional<TipoPrestamo> findByNombre(String nombre) {
        return repository.findByNombre(nombre)
                .map(entity -> TipoPrestamo.builder()
                        .id(entity.getId())
                        .nombre(entity.getNombre())
                        .tasaInteres(entity.getTasaInteres())
                        .validacionAutomatica(entity.isValidacionAutomatica())
                        .build());
    }
}