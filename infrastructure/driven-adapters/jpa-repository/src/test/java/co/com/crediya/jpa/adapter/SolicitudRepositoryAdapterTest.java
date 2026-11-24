package co.com.crediya.jpa.adapter;

import co.com.crediya.jpa.entity.SolicitudEntity;
import co.com.crediya.jpa.entity.TipoPrestamoEntity;
import co.com.crediya.jpa.repository.SolicitudDataRepository;
import co.com.crediya.jpa.repository.TipoPrestamoDataRepository;
import co.com.crediya.model.solicitud.EstadoSolicitud;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.tipoprestamo.TipoPrestamo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SolicitudRepositoryAdapterTest {

    @Mock
    private SolicitudDataRepository solicitudDataRepository;

    @Mock
    private TipoPrestamoDataRepository tipoPrestamoDataRepository;

    @InjectMocks
    private SolicitudRepositoryAdapter adapter;

    @Test
    @DisplayName("Guarda la solicitud y devuelve el modelo con el ID generado")
    void shouldSaveSolicitudAndReturnWithId() {
        TipoPrestamoEntity tipoEntity = TipoPrestamoEntity.builder()
                .id(1L)
                .nombre("LIBRE_INVERSION")
                .build();

        Solicitud entrada = Solicitud.builder()
                .documentoIdentidad("1020304050")
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .tipoPrestamo(TipoPrestamo.builder().id(1L).nombre("LIBRE_INVERSION").build())
                .estado(EstadoSolicitud.PENDIENTE_REVISION)
                .build();

        SolicitudEntity guardada = SolicitudEntity.builder()
                .id(10L)
                .documentoIdentidad("1020304050")
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .tipoPrestamo(tipoEntity)
                .estado(EstadoSolicitud.PENDIENTE_REVISION)
                .build();

        when(tipoPrestamoDataRepository.findById(1L)).thenReturn(Optional.of(tipoEntity));
        when(solicitudDataRepository.save(any(SolicitudEntity.class))).thenReturn(guardada);

        Solicitud resultado = adapter.save(entrada);

        assertEquals(10L, resultado.getId());
        assertEquals("1020304050", resultado.getDocumentoIdentidad());
        verify(solicitudDataRepository).save(any(SolicitudEntity.class));
    }

    @Test
    @DisplayName("Lista todas las solicitudes")
    void shouldListAllSolicitudes() {
        when(solicitudDataRepository.findAll()).thenReturn(List.of(solicitudEntity()));

        List<Solicitud> resultado = adapter.findAll();

        assertEquals(1, resultado.size());
        assertEquals("1020304050", resultado.get(0).getDocumentoIdentidad());
    }

    @Test
    @DisplayName("Lista las solicitudes de un estado")
    void shouldListSolicitudesByEstado() {
        when(solicitudDataRepository.findByEstado(EstadoSolicitud.REVISION_MANUAL))
                .thenReturn(List.of(solicitudEntity()));

        List<Solicitud> resultado = adapter.findByEstado(EstadoSolicitud.REVISION_MANUAL);

        assertEquals(1, resultado.size());
    }

    private SolicitudEntity solicitudEntity() {
        return SolicitudEntity.builder()
                .id(10L)
                .documentoIdentidad("1020304050")
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .tipoPrestamo(TipoPrestamoEntity.builder()
                        .id(1L)
                        .nombre("LIBRE_INVERSION")
                        .build())
                .estado(EstadoSolicitud.PENDIENTE_REVISION)
                .build();
    }

    @Test
    @DisplayName("Lanza excepción si el ID del tipo de préstamo no existe en base de datos")
    void shouldThrowWhenTipoPrestamoIdNotFound() {
        Solicitud entrada = Solicitud.builder()
                .tipoPrestamo(TipoPrestamo.builder().id(99L).build())
                .build();

        when(tipoPrestamoDataRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> adapter.save(entrada));
    }
}