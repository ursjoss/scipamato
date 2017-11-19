package ch.difty.scipamato.persistence.config;

import org.jooq.conf.Settings;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqSpringBootConfiguration {

    @Bean
    public Settings settings() {
        return new DefaultConfiguration().settings().withExecuteWithOptimisticLocking(true);
    }
}
