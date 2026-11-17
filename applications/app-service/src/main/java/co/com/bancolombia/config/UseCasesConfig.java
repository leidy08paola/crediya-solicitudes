package co.com.bancolombia.config;

import co.com.bancolombia.model.solicitud.gateways.SolicitudRepository;
import co.com.bancolombia.usecase.registrarsolicitud.RegistrarSolicitudUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfig {

        @Bean
        public RegistrarSolicitudUseCase registrarSolicitudUseCase(SolicitudRepository solicitudRepository) {
                return new RegistrarSolicitudUseCase(solicitudRepository);
        }
}