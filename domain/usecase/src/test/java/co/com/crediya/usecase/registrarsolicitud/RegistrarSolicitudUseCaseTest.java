package co.com.crediya.usecase.registrarsolicitud;

import co.com.crediya.model.solicitud.EstadoSolicitud;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.exception.BusinessException;
import co.com.crediya.model.solicitud.exception.ErrorCode;
import co.com.crediya.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.model.tipoprestamo.TipoPrestamo;
import co.com.crediya.model.tipoprestamo.gateways.TipoPrestamoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class RegistrarSolicitudUseCaseTest {

    private static final String TIPO = "LIBRE_INVERSION";

    private final SolicitudRepository solicitudRepository = mock(SolicitudRepository.class);
    private final TipoPrestamoRepository tipoPrestamoRepository = mock(TipoPrestamoRepository.class);
    private final RegistrarSolicitudUseCase useCase =
            new RegistrarSolicitudUseCase(solicitudRepository, tipoPrestamoRepository);

    @Test
    @DisplayName("Registra la solicitud como pendiente y con el tipo de préstamo resuelto")
    void shouldRegisterPendingRequestWithResolvedLoanType() {
        TipoPrestamo tipoExistente = TipoPrestamo.builder()
                .id(1L).nombre(TIPO).tasaInteres(new BigDecimal("0.015")).build();
        when(tipoPrestamoRepository.findByNombre(TIPO)).thenReturn(Optional.of(tipoExistente));
        when(solicitudRepository.save(any(Solicitud.class))).thenAnswer(inv -> inv.getArgument(0));

        Solicitud resultado = useCase.registrar(solicitudDelTipo(TIPO, null));

        ArgumentCaptor<Solicitud> captor = ArgumentCaptor.forClass(Solicitud.class);
        verify(solicitudRepository).save(captor.capture());
        assertEquals(EstadoSolicitud.PENDIENTE_REVISION, captor.getValue().getEstado());
        assertEquals(1L, captor.getValue().getTipoPrestamo().getId());
        assertNotNull(resultado);

        assertEquals(EstadoSolicitud.PENDIENTE_REVISION, resultado.getEstado());
    }
    @Test
    @DisplayName("Ignora un estado distinto de pendiente enviado desde afuera")
    void shouldForcePendingStatusEvenIfRequestComesApproved() {
        when(tipoPrestamoRepository.findByNombre(TIPO))
                .thenReturn(Optional.of(TipoPrestamo.builder().id(1L).nombre(TIPO).build()));
        when(solicitudRepository.save(any(Solicitud.class))).thenAnswer(inv -> inv.getArgument(0));

        useCase.registrar(solicitudDelTipo(TIPO, EstadoSolicitud.APROBADA));

        ArgumentCaptor<Solicitud> captor = ArgumentCaptor.forClass(Solicitud.class);
        verify(solicitudRepository).save(captor.capture());
        assertEquals(EstadoSolicitud.PENDIENTE_REVISION, captor.getValue().getEstado());
    }

    @Test
    @DisplayName("Rechaza un tipo de préstamo inexistente y no guarda nada")
    void shouldRejectUnknownLoanTypeAndNotSave() {
        when(tipoPrestamoRepository.findByNombre("INEXISTENTE")).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> useCase.registrar(solicitudDelTipo("INEXISTENTE", null)));

        assertEquals(ErrorCode.TIPO_PRESTAMO_NO_ENCONTRADO, exception.getErrorCode());
        verifyNoInteractions(solicitudRepository);
    }

    @Test
    @DisplayName("Una solicitud nula es un error de programación")
    void shouldFailWhenRequestIsNull() {
        assertThrows(NullPointerException.class, () -> useCase.registrar(null));
    }

    private Solicitud solicitudDelTipo(String nombreTipo, EstadoSolicitud estado) {
        return Solicitud.builder()
                .documentoIdentidad("1001234567")
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .tipoPrestamo(TipoPrestamo.builder().nombre(nombreTipo).build())
                .estado(estado)
                .build();
    }
}