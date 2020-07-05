package ch.difty.scipamato.core.logic.export

import ch.difty.scipamato.core.logic.exporting.RisAdapterFactory
import ch.difty.scipamato.core.logic.exporting.RisAdapterFactory.Companion.create
import ch.difty.scipamato.core.logic.exporting.RisExporterStrategy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
open class TestRisExporterConfiguration {
    @Bean
    @Primary
    open fun risAdapterFactory(): RisAdapterFactory = create(RisExporterStrategy.DEFAULT)
}
