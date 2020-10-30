package ch.difty.scipamato.common

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class PublicPersistenceJooqUtilConfiguration {
    @Bean
    open fun dateTimeService(): DateTimeService = CurrentDateTimeService()
}
