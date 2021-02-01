package br.gov.mt.pjc.artist.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(enableDefaultTransactions = false, basePackages = "br.gov.mt.pjc.artist.domain.repository")
public class JpaConfig {

}
