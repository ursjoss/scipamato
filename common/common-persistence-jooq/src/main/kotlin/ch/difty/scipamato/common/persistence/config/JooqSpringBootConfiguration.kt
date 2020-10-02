package ch.difty.scipamato.common.persistence.config

import org.jooq.conf.Settings
import org.jooq.impl.DefaultConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class JooqSpringBootConfiguration {
    @Bean
    open fun settings(): Settings =
        DefaultConfiguration().settings()
            .withExecuteWithOptimisticLocking(true)
}
