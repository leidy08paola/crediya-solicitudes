package co.com.crediya.usecase.listarsolicitudes;

import co.com.crediya.model.solicitud.EstadoSolicitud;
import co.com.crediya.model.solicitud.PageResult;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.gateways.SolicitudRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarSolicitudesUseCaseTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @InjectMocks
    private ListarSolicitudesUseCase useCase;

    @Test
    @DisplayName("Lista las solicitudes con los estados por defecto")
    void shouldListSolicitudes() {
        when(solicitudRepository.findByEstadoIn(anyList(), anyInt(), anyInt()))
                .thenReturn(new PageResult<>(List.of(solicitud(EstadoSolicitud.PENDIENTE_REVISION)), 0, 10, 1));

        PageResult<Solicitud> resultado = useCase.listar(0, 10);

        assertEquals(1, resultado.content().size());
    }

    @Test
    @DisplayName("Filtra solicitudes por estado")
    void shouldListSolicitudesByEstado() {
        when(solicitudRepository.findByEstadoIn(List.of(EstadoSolicitud.REVISION_MANUAL), 0, 10))
                .thenReturn(new PageResult<>(List.of(solicitud(EstadoSolicitud.REVISION_MANUAL)), 0, 10, 1));

        PageResult<Solicitud> resultado = useCase.listarPorEstado(EstadoSolicitud.REVISION_MANUAL, 0, 10);

        assertEquals(1, resultado.content().size());
    }

    private Solicitud solicitud(EstadoSolicitud estado) {
        return Solicitud.builder()
                .documentoIdentidad("1020304050")
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .estado(estado)
                .build();
    }
}
