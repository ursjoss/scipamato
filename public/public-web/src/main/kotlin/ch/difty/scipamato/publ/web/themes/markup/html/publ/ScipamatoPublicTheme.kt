package ch.difty.scipamato.publ.web.themes.markup.html.publ

import ch.difty.scipamato.common.logger
import de.agilecoders.wicket.core.settings.ITheme
import de.agilecoders.wicket.core.settings.Theme
import org.apache.wicket.markup.head.CssHeaderItem
import org.apache.wicket.markup.head.HeaderItem

private val log = logger()

/**
 * Kotlin representation of the SciPaMaTo-Public Theme. Is based on TODC Bootstrap
 * and the wicket-bootstrap implementation of Michael Haitz in wicket-bootstrap.
 *
 * A flag [useLessOverCss] passed into the constructor indicates whether the precompiled CSS classes
 * will be used (preferable in production) or if the LESS files shall be dynamically compiled into CSS.
 *
 * This theme modifies bootstrap and therefore both css files get loaded
 * (`bootstrap.css` and `scipamato-public-bootstrap.css`).
 *
 * **Usage:**
 *
 * ```
 * settings.setThemeProvider(new SingleThemeProvider(new ScipamatoPublicTheme()));
 * ```
 *
 * @author [Michael Haitz](mailto:michael.haitz@agilecoders.de)
 * @author Urs Joss
 */
class ScipamatoPublicTheme(
    private val useLessOverCss: Boolean = false,
    name: String = "scipamato-public"
) : Theme(name) {

    override fun getDependencies(): List<HeaderItem> = listOf(makeHeaderItem())

    private fun makeHeaderItem(): CssHeaderItem =
        if (useLessOverCss) {
            log.debug { "Loading style sheets via ScipamatoPublicLessReference" }
            CssHeaderItem.forReference(ScipamatoPublicLessReference).setId(ITheme.BOOTSTRAP_THEME_MARKUP_ID)
        } else {
            CssHeaderItem.forReference(ScipamatoPublicCssReference).setId(ITheme.BOOTSTRAP_THEME_MARKUP_ID)
        }
}
