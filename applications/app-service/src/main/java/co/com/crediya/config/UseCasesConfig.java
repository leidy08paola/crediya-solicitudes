package co.com.crediya.config;

import co.com.crediya.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.model.tipoprestamo.gateways.TipoPrestamoRepository;
import co.com.crediya.model.usuario.gateways.UsuarioGateway;
import co.com.crediya.usecase.listarsolicitudes.ListarSolicitudesUseCase;
import co.com.crediya.usecase.registrarsolicitud.RegistrarSolicitudUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfig {

        @Bean
        public RegistrarSolicitudUseCase registrarSolicitudUseCase(
                SolicitudRepository solicitudRepository,
                TipoPrestamoRepository tipoPrestamoRepository,
                UsuarioGateway usuarioGateway) {
                return new RegistrarSolicitudUseCase(solicitudRepository, tipoPrestamoRepository, usuarioGateway);
        }

        @Bean
        public ListarSolicitudesUseCase listarSolicitudesUseCase(SolicitudRepository solicitudRepository) {
                return new ListarSolicitudesUseCase(solicitudRepository);
        }
}