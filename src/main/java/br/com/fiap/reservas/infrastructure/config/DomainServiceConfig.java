package br.com.fiap.reservas.infrastructure.config;

import br.com.fiap.reservas.domain.service.IResourceAvailabilityChecker;
import br.com.fiap.reservas.domain.service.MinimumAdvanceNoticePolicy;
import br.com.fiap.reservas.domain.service.ResourceAvailabilityChecker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainServiceConfig {

    @Bean
    public IResourceAvailabilityChecker resourceAvailabilityChecker() {
        return new ResourceAvailabilityChecker();
    }

    @Bean
    public MinimumAdvanceNoticePolicy minimumAdvanceNoticePolicy() {
        return new MinimumAdvanceNoticePolicy();
    }
}
