package ch.difty.scipamato.publ

import ch.difty.scipamato.common.ClearAllMocksExtension
import ch.difty.scipamato.common.logger
import ch.difty.scipamato.publ.config.ScipamatoPublicProperties
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

private val log = logger()

@ExtendWith(MockKExtension::class, ClearAllMocksExtension::class)
internal class ScipamatoPublicApplicationTest {

    @MockK
    private lateinit var properties: ScipamatoPublicProperties

    @Test
    fun withCommercialFontEnabled_willOutputLog() {
        every { properties.isCommercialFontPresent } returns true
        every { properties.isResponsiveIframeSupportEnabled } returns false
        val app = ScipamatoPublicApplication(properties)

        log.info { "We should see single log about commercial font being enabled" }

        app.logSpecialConfiguration()
        // visually assert the respective log is on console (no automatic assertion)
        log.info { "----" }
    }

    @Test
    fun withPymEnabled_willOutputLog() {
        every { properties.isCommercialFontPresent } returns false
        every { properties.isResponsiveIframeSupportEnabled } returns true

        val app = ScipamatoPublicApplication(properties)

        log.info("We should see single log about pym being enabled")

        app.logSpecialConfiguration()

        // visually assert the respective log is on console (no automatic assertion)
        log.info("----")
    }

    @Test
    fun withPropertiesDisabled_willNotOutputLogs() {
        every { properties.isCommercialFontPresent } returns false
        every { properties.isResponsiveIframeSupportEnabled } returns false

        val app = ScipamatoPublicApplication(properties)

        log.info { "We should see no logs (about commercial fonts or pym)" }

        app.logSpecialConfiguration()
        // visually assert no logs are on console
        log.info { "----" }
    }
}