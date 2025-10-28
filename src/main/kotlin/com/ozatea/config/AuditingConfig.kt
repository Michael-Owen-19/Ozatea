package com.ozatea.config

import com.ozatea.core.security.SpringSecurityAuditorAware
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
class AuditingConfig {
    @Bean
    fun auditorProvider(): SpringSecurityAuditorAware = SpringSecurityAuditorAware()
}