package co.com.crediya.jpa.adapter;

import co.com.crediya.jpa.entity.SolicitudEntity;
import co.com.crediya.jpa.entity.TipoPrestamoEntity;
import co.com.crediya.jpa.repository.SolicitudDataRepository;
import co.com.crediya.jpa.repository.TipoPrestamoDataRepository;
import co.com.crediya.model.solicitud.EstadoSolicitud;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.model.tipoprestamo.TipoPrestamo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
        enrichWithAuthData(entity);

        SolicitudEntity saved = solicitudDataRepository.save(entity);

        return solicitud.toBuilder()
                .id(saved.getId())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Solicitud> findAll() {
        return toDomainList(solicitudDataRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Solicitud> findByEstado(EstadoSolicitud estado) {
        return toDomainList(solicitudDataRepository.findByEstado(estado));
    }

    private List<Solicitud> toDomainList(Iterable<SolicitudEntity> entities) {
        List<Solicitud> lista = new ArrayList<>();
        for (SolicitudEntity entity : entities) {
            lista.add(toDomain(entity));
        }
        return lista;
    }

    private static Solicitud toDomain(SolicitudEntity entity) {
        return Solicitud.builder()
                .id(entity.getId())
                .documentoIdentidad(entity.getDocumentoIdentidad())
                .monto(entity.getMonto())
                .plazo(entity.getPlazo())
                .tipoPrestamo(entity.getTipoPrestamo() != null
                        ? TipoPrestamo.builder()
                        .id(entity.getTipoPrestamo().getId())
                        .nombre(entity.getTipoPrestamo().getNombre())
                        .tasaInteres(entity.getTipoPrestamo().getTasaInteres())
                        .build()
                        : null)
                .estado(entity.getEstado())
                .email(entity.getEmail())
                .nombre(entity.getNombre())
                .salarioBase(entity.getSalarioBase())
                .build();
    }

    private void enrichWithAuthData(SolicitudEntity entity) {
        Map<String, Object> user = new AuthUsersClient().findByDocumentoIdentidad(entity.getDocumentoIdentidad());
        if (user.get("email") != null) {
            entity.setEmail((String) user.get("email"));
        }
        if (user.get("nombre") != null) {
            entity.setNombre((String) user.get("nombre"));
        }
        if (user.get("salarioBase") != null) {
            entity.setSalarioBase((BigDecimal) user.get("salarioBase"));
        }
    }
}