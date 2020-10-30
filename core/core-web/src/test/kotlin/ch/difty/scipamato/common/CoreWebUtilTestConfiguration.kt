package ch.difty.scipamato.common

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
open class CoreWebUtilTestConfiguration {

    @Bean
    @Primary
    open fun dateTimeService(): DateTimeService = FrozenDateTimeService()
}
