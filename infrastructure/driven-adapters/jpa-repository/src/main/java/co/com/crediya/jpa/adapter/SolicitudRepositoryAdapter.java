package co.com.crediya.jpa.adapter;

import co.com.crediya.jpa.entity.SolicitudEntity;
import co.com.crediya.jpa.entity.TipoPrestamoEntity;
import co.com.crediya.jpa.repository.SolicitudDataRepository;
import co.com.crediya.jpa.repository.TipoPrestamoDataRepository;
import co.com.crediya.model.solicitud.CuotaCalculator;
import co.com.crediya.model.solicitud.EstadoSolicitud;
import co.com.crediya.model.solicitud.PageResult;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.model.tipoprestamo.TipoPrestamo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                .email(solicitud.getEmail())
                .nombre(solicitud.getNombre())
                .salarioBase(solicitud.getSalarioBase())
                .build();

        SolicitudEntity saved = solicitudDataRepository.save(entity);

        return solicitud.toBuilder()
                .id(saved.getId())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<Solicitud> findByEstadoIn(List<EstadoSolicitud> estados, int page, int size) {
        Page<SolicitudEntity> pageResult =
                solicitudDataRepository.findByEstadoIn(estados, PageRequest.of(page, size));

        List<Solicitud> contenido = new ArrayList<>();
        for (SolicitudEntity entity : pageResult.getContent()) {
            contenido.add(toDomain(entity));
        }
        Map<String, BigDecimal> deudasPorDocumento = calcularDeudasAprobadasPorDocumento();

        for (Solicitud solicitud : contenido) {
            if (solicitud.getDocumentoIdentidad() != null) {
                BigDecimal deuda = deudasPorDocumento.getOrDefault(solicitud.getDocumentoIdentidad(), BigDecimal.ZERO);
                solicitud.setDeudaTotalMensualSolicitudesAprobadas(deuda);
            }
        }
        return new PageResult<>(contenido, pageResult.getNumber(), pageResult.getSize(), pageResult.getTotalElements());
    }

    private Map<String, BigDecimal> calcularDeudasAprobadasPorDocumento() {
        Map<String, BigDecimal> deudas = new HashMap<>();
        for (SolicitudEntity entity : solicitudDataRepository.findByEstado(EstadoSolicitud.APROBADA)) {
            if (entity.getDocumentoIdentidad() == null || entity.getTipoPrestamo() == null) {
                continue;
            }
            BigDecimal cuota = CuotaCalculator.calcularCuota(
                    entity.getMonto(), entity.getTipoPrestamo().getTasaInteres(), entity.getPlazo());
            deudas.merge(entity.getDocumentoIdentidad(), cuota, BigDecimal::add);
        }
        return deudas;
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
                .deudaTotalMensualSolicitudesAprobadas(BigDecimal.ZERO)
                .build();
    }
}
