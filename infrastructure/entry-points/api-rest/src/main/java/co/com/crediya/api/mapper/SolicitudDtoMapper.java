package co.com.crediya.api.mapper;

import co.com.crediya.api.dto.CrearSolicitudRequest;
import co.com.crediya.api.dto.SolicitudResponse;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.tipoprestamo.TipoPrestamo;

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
        return new SolicitudResponse(
                solicitud.getId(),
                solicitud.getDocumentoIdentidad(),
                solicitud.getMonto(),
                solicitud.getPlazo(),
                solicitud.getTipoPrestamo().getNombre(),
                solicitud.getEstado().getEtiqueta()
        );
    }
}