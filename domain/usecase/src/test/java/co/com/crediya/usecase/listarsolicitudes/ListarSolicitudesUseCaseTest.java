package co.com.crediya.usecase.listarsolicitudes;

import co.com.crediya.model.solicitud.EstadoSolicitud;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarSolicitudesUseCaseTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @InjectMocks
    private ListarSolicitudesUseCase useCase;

    @Test
    @DisplayName("Lista todas las solicitudes")
    void shouldListAllSolicitudes() {
        when(solicitudRepository.findAll()).thenReturn(List.of(solicitud(EstadoSolicitud.PENDIENTE_REVISION)));

        assertEquals(1, useCase.listar().size());
    }

    @Test
    @DisplayName("Filtra solicitudes por estado")
    void shouldListSolicitudesByEstado() {
        when(solicitudRepository.findByEstado(EstadoSolicitud.REVISION_MANUAL))
                .thenReturn(List.of(solicitud(EstadoSolicitud.REVISION_MANUAL)));

        List<Solicitud> resultado = useCase.listarPorEstado(EstadoSolicitud.REVISION_MANUAL);

        assertEquals(1, resultado.size());
        assertEquals(EstadoSolicitud.REVISION_MANUAL, resultado.get(0).getEstado());
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
