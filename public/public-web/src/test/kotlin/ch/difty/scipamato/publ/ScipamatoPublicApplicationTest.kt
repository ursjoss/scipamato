package ch.difty.scipamato.publ

import ch.difty.scipamato.common.logger
import ch.difty.scipamato.publ.config.ScipamatoPublicProperties
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

private val log = logger()

internal class ScipamatoPublicApplicationTest {

    @Test
    fun withCommercialFontEnabled_willOutputLog() {
        val properties = mockk<ScipamatoPublicProperties> {
            every { isCommercialFontPresent } returns true
            every { isResponsiveIframeSupportEnabled } returns false
        }
        val app = ScipamatoPublicApplication(properties)

        log.info { "We should see single log about commercial font being enabled" }

        app.logSpecialConfiguration()
        // visually assert the respective log is on console (no automatic assertion)
        log.info { "----" }
    }

    @Test
    fun withPymEnabled_willOutputLog() {
        val properties = mockk<ScipamatoPublicProperties> {
            every { isCommercialFontPresent } returns false
            every { isResponsiveIframeSupportEnabled } returns true
        }

        val app = ScipamatoPublicApplication(properties)

        log.info("We should see single log about pym being enabled")

        app.logSpecialConfiguration()

        // visually assert the respective log is on console (no automatic assertion)
        log.info("----")
    }

    @Test
    fun withPropertiesDisabled_willNotOutputLogs() {
        val properties = mockk<ScipamatoPublicProperties> {
            every { isCommercialFontPresent } returns false
            every { isResponsiveIframeSupportEnabled } returns false
        }

        val app = ScipamatoPublicApplication(properties)

        log.info { "We should see no logs (about commercial fonts or pym)" }

        app.logSpecialConfiguration()
        // visually assert no logs are on console
        log.info { "----" }
    }
}
