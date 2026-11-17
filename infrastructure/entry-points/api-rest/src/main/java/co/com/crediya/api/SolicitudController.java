package co.com.crediya.api;

import co.com.crediya.api.dto.SolicitudRequestDto;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.usecase.registrarsolicitud.RegistrarSolicitudUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/solicitudes")
@RequiredArgsConstructor
public class SolicitudController {

    private final RegistrarSolicitudUseCase registrarSolicitudUseCase;

    @PostMapping
    public ResponseEntity<Solicitud> registrarSolicitud(@Valid @RequestBody SolicitudRequestDto requestDto) {
        Solicitud solicitud = Solicitud.builder()
                .documentoIdentidad(requestDto.getDocumentoIdentidad())
                .monto(requestDto.getMonto())
                .plazo(requestDto.getPlazo())
                .build();

        Solicitud nuevaSolicitud = registrarSolicitudUseCase.registrar(solicitud, requestDto.getTipoPrestamo());
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaSolicitud);
    }
}