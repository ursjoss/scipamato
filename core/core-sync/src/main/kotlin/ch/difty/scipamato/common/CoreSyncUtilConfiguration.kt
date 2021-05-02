package ch.difty.scipamato.common

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class CoreSyncUtilConfiguration {
    @Bean
    open fun dateTimeService(): DateTimeService = CurrentDateTimeService()
}
