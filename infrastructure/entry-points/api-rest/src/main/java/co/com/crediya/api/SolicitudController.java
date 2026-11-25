package co.com.crediya.api;

import co.com.crediya.api.dto.CrearSolicitudRequest;
import co.com.crediya.api.dto.SolicitudPageResponse;
import co.com.crediya.api.dto.SolicitudResponse;
import co.com.crediya.api.exception.ErrorResponse;
import co.com.crediya.api.mapper.SolicitudDtoMapper;
import co.com.crediya.api.security.AuthException;
import co.com.crediya.model.usuario.AuthenticatedUser;
import co.com.crediya.model.solicitud.EstadoSolicitud;
import co.com.crediya.model.solicitud.PageResult;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.user.Role;
import co.com.crediya.usecase.listarsolicitudes.ListarSolicitudesUseCase;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/solicitud")
@RequiredArgsConstructor
@Tag(name = "Solicitudes", description = "Gestión de solicitudes de préstamo")
public class SolicitudController {

    private final RegistrarSolicitudUseCase registrarSolicitudUseCase;
    private final ListarSolicitudesUseCase listarSolicitudesUseCase;

    @GetMapping
    @Operation(summary = "Listar solicitudes de préstamo", description = "Listado paginado y filtrable por estado")
    public ResponseEntity<SolicitudPageResponse> listar(
            @RequestAttribute("authenticatedUser") AuthenticatedUser authenticatedUser,
            @RequestParam(name = "estado", required = false) EstadoSolicitud estado,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        requireRole(authenticatedUser, Role.ASESOR);
        validatePagination(page, size);
        log.info("Solicitud de listado de solicitudes recibida");

        PageResult<Solicitud> resultado = estado != null
                ? listarSolicitudesUseCase.listarPorEstado(estado, page, size)
                : listarSolicitudesUseCase.listar(page, size);

        return ResponseEntity.ok(SolicitudDtoMapper.toPageResponse(resultado));
    }

    @PostMapping
    @Operation(summary = "Registrar una solicitud de préstamo")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Solicitud registrada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o tipo de préstamo inexistente",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error inesperado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<SolicitudResponse> registrar(
            @RequestAttribute("authenticatedUser") AuthenticatedUser authenticatedUser,
            @Valid @RequestBody CrearSolicitudRequest request) {
        log.info("Solicitud de registro de préstamo recibida");
        requireRole(authenticatedUser, Role.CLIENTE);
        requireMatchingDocument(authenticatedUser, request.documentoIdentidad());

        Solicitud creada = registrarSolicitudUseCase.registrar(SolicitudDtoMapper.toDomain(request), authenticatedUser);

        log.info("Solicitud registrada con id {}", creada.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(SolicitudDtoMapper.toResponse(creada));
    }

    private void requireRole(AuthenticatedUser authenticatedUser, Role rolPermitido) {
        if (authenticatedUser == null || authenticatedUser.rol() != rolPermitido) {
            throw new AuthException(HttpStatus.FORBIDDEN, "Role no autorizado para esta acción");
        }
    }

    private void requireMatchingDocument(AuthenticatedUser authenticatedUser, String documentoIdentidad) {
        String docAutenticado = authenticatedUser.documentoIdentidad();
        if (docAutenticado == null || !docAutenticado.equals(documentoIdentidad)) {
            throw new AuthException(HttpStatus.FORBIDDEN,
                    "El documento de identidad no coincide con el autenticado");
        }
    }

    private void validatePagination(int page, int size) {
        if (page < 0) {
            throw new AuthException(HttpStatus.BAD_REQUEST, "El parámetro page debe ser >= 0");
        }
        if (size < 1 || size > 50) {
            throw new AuthException(HttpStatus.BAD_REQUEST, "El parámetro size debe estar entre 1 y 50");
        }
    }
}