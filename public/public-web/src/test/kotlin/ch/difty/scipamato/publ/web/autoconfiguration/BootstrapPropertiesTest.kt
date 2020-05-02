package ch.difty.scipamato.publ.web.autoconfiguration

import de.agilecoders.wicket.themes.markup.html.bootswatch.BootswatchTheme
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.junit.jupiter.api.Test

internal class BootstrapPropertiesTest {

    private val bp = BootstrapProperties()

    @Test
    fun standardTheme_isSandstone() {
        bp.theme shouldBeEqualTo BootswatchTheme.Sandstone
    }

    @Test
    fun canModifyTheme() {
        bp.theme = BootswatchTheme.Cerulean
        bp.theme shouldBeEqualTo BootswatchTheme.Cerulean
    }

    @Test
    fun isEnabledByDefault() {
        bp.isEnabled.shouldBeTrue()
    }

    @Test
    fun canDisable() {
        bp.isEnabled = false
        bp.isEnabled.shouldBeFalse()
    }
}