package ch.difty.scipamato.common

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
open class PublicWebUtilTestConfiguration {

    @Bean
    @Primary
    open fun dateTimeService(): DateTimeService {
        return FrozenDateTimeService()
    }
}
