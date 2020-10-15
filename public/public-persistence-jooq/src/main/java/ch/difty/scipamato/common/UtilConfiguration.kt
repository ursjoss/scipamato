package ch.difty.scipamato.common

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class UtilConfiguration {
    @Bean
    open fun dateTimeService(): DateTimeService = CurrentDateTimeService()
}
