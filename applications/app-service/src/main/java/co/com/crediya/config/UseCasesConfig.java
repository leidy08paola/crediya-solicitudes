package co.com.crediya.config;

import co.com.crediya.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.usecase.registrarsolicitud.RegistrarSolicitudUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfig {

        @Bean
        public RegistrarSolicitudUseCase registrarSolicitudUseCase(SolicitudRepository solicitudRepository) {
                return new RegistrarSolicitudUseCase(solicitudRepository);
        }
}