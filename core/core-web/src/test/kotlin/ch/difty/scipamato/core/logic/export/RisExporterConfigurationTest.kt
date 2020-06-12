package ch.difty.scipamato.core.logic.export

import ch.difty.scipamato.common.ClearAllMocksExtension
import ch.difty.scipamato.core.config.ApplicationCoreProperties
import ch.difty.scipamato.core.logic.exporting.RisAdapterFactory
import ch.difty.scipamato.core.logic.exporting.RisExporterConfiguration
import ch.difty.scipamato.core.logic.exporting.RisExporterStrategy
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class, ClearAllMocksExtension::class)
internal class RisExporterConfigurationTest {

    private lateinit var conf: RisExporterConfiguration

    @MockK
    private lateinit var appProperties: ApplicationCoreProperties

    @BeforeEach
    fun setUp() {
        conf = RisExporterConfiguration()
        every { appProperties.risExporterStrategy } returns RisExporterStrategy.DISTILLERSR
    }

    @Test
    fun canRetrieveRisAdapterFactory() {
        val factory = conf.risAdapterFactory(appProperties)
        factory shouldBeInstanceOf RisAdapterFactory::class
        verify { appProperties.risExporterStrategy }
    }
}
