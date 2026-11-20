package co.com.crediya.jpa.adapter;

import co.com.crediya.jpa.entity.SolicitudEntity;
import co.com.crediya.jpa.entity.TipoPrestamoEntity;
import co.com.crediya.jpa.repository.SolicitudDataRepository;
import co.com.crediya.jpa.repository.TipoPrestamoDataRepository;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.gateways.SolicitudRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class SolicitudRepositoryAdapter implements SolicitudRepository {

    private final SolicitudDataRepository solicitudDataRepository;
    private final TipoPrestamoDataRepository tipoPrestamoDataRepository;

    @Override
    @Transactional
    public Solicitud save(Solicitud solicitud) {
        TipoPrestamoEntity tipoEntity = tipoPrestamoDataRepository.findById(solicitud.getTipoPrestamo().getId())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de préstamo no encontrado"));

        SolicitudEntity entity = SolicitudEntity.builder()
                .documentoIdentidad(solicitud.getDocumentoIdentidad())
                .monto(solicitud.getMonto())
                .plazo(solicitud.getPlazo())
                .tipoPrestamo(tipoEntity)
                .estado(solicitud.getEstado())
                .build();

        SolicitudEntity saved = solicitudDataRepository.save(entity);

        return solicitud.toBuilder()
                .id(saved.getId())
                .build();
    }
}