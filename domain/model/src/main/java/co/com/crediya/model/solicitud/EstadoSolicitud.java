package co.com.crediya.model.solicitud;

import lombok.Getter;

@Getter
public enum EstadoSolicitud {

    PENDIENTE_REVISION("Pendiente de revisión"),
    REVISION_MANUAL("Revisión manual"),
    RECHAZADA("Rechazada"),
    APROBADA("Aprobada");

    private final String etiqueta;

    EstadoSolicitud(String etiqueta) {
        this.etiqueta = etiqueta;
    }
}