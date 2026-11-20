package co.com.crediya.api;

import co.com.crediya.api.dto.CrearSolicitudRequest;
import co.com.crediya.api.dto.SolicitudResponse;
import co.com.crediya.api.exception.ErrorResponse;
import co.com.crediya.api.mapper.SolicitudDtoMapper;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.usecase.registrarsolicitud.RegistrarSolicitudUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/solicitud")
@RequiredArgsConstructor
@Tag(name = "Solicitudes", description = "Gestión de solicitudes de préstamo")
public class SolicitudController {

    private final RegistrarSolicitudUseCase registrarSolicitudUseCase;

    @PostMapping
    @Operation(summary = "Registrar una solicitud de préstamo")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Solicitud registrada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o tipo de préstamo inexistente",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error inesperado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<SolicitudResponse> registrar(@Valid @RequestBody CrearSolicitudRequest request) {
        log.info("Solicitud de registro de préstamo recibida");

        Solicitud creada = registrarSolicitudUseCase.registrar(SolicitudDtoMapper.toDomain(request));

        log.info("Solicitud registrada con id {}", creada.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(SolicitudDtoMapper.toResponse(creada));
    }
}