package co.com.crediya.api.mapper;

import co.com.crediya.api.dto.CrearSolicitudRequest;
import co.com.crediya.api.dto.SolicitudPageResponse;
import co.com.crediya.api.dto.SolicitudResponse;
import co.com.crediya.model.solicitud.PageResult;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.tipoprestamo.TipoPrestamo;

import java.util.stream.Collectors;

public final class SolicitudDtoMapper {

    private SolicitudDtoMapper() {
    }

    public static Solicitud toDomain(CrearSolicitudRequest request) {
        return Solicitud.builder()
                .documentoIdentidad(request.documentoIdentidad())
                .monto(request.monto())
                .plazo(request.plazo())
                .tipoPrestamo(TipoPrestamo.builder().nombre(request.tipoPrestamo()).build())
                .build();
    }

    public static SolicitudResponse toResponse(Solicitud solicitud) {
        String estado = solicitud.getEstado() != null ? solicitud.getEstado().name() : null;
        String etiqueta = solicitud.getEstado() != null ? solicitud.getEstado().getEtiqueta() : null;
        return new SolicitudResponse(
                solicitud.getId(),
                solicitud.getDocumentoIdentidad(),
                solicitud.getMonto(),
                solicitud.getPlazo(),
                solicitud.getTipoPrestamo() != null ? solicitud.getTipoPrestamo().getNombre() : null,
                solicitud.getTipoPrestamo() != null ? solicitud.getTipoPrestamo().getTasaInteres() : null,
                etiqueta,
                estado,
                solicitud.getEmail(),
                solicitud.getNombre(),
                solicitud.getSalarioBase(),
                solicitud.getDeudaTotalMensualSolicitudesAprobadas()
        );
    }

    public static SolicitudPageResponse toPageResponse(PageResult<Solicitud> page) {
        return new SolicitudPageResponse(
                page.content().stream().map(SolicitudDtoMapper::toResponse).collect(Collectors.toList()),
                page.page(),
                page.size(),
                page.totalElements()
        );
    }
}