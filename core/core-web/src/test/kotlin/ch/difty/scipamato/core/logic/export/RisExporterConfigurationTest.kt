package ch.difty.scipamato.core.logic.export

import ch.difty.scipamato.core.config.ApplicationCoreProperties
import ch.difty.scipamato.core.logic.exporting.RisAdapterFactory
import ch.difty.scipamato.core.logic.exporting.RisExporterConfiguration
import ch.difty.scipamato.core.logic.exporting.RisExporterStrategy
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.Test

internal class RisExporterConfigurationTest {

    @Test
    fun canRetrieveRisAdapterFactory() {
        val conf = RisExporterConfiguration()
        val appProperties = mockk<ApplicationCoreProperties> {
            every { risExporterStrategy } returns RisExporterStrategy.DISTILLERSR
        }
        val factory = conf.risAdapterFactory(appProperties)
        factory shouldBeInstanceOf RisAdapterFactory::class
        verify { appProperties.risExporterStrategy }
    }
}
