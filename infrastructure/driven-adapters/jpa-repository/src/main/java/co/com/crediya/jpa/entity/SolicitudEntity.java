package co.com.crediya.jpa.entity;

import co.com.crediya.model.solicitud.EstadoSolicitud;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "solicitudes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "documento_identidad", nullable = false)
    private String documentoIdentidad;

    @Column(nullable = false)
    private BigDecimal monto;

    @Column(nullable = false)
    private Integer plazo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_prestamo_id", nullable = false)
    private TipoPrestamoEntity tipoPrestamo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSolicitud estado;
}