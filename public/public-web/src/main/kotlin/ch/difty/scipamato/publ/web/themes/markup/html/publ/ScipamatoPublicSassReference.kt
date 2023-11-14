package ch.difty.scipamato.publ.web.themes.markup.html.publ

import de.agilecoders.wicket.core.Bootstrap
import de.agilecoders.wicket.core.util.Dependencies
import de.agilecoders.wicket.sass.SassResourceReference
import org.apache.wicket.markup.head.CssHeaderItem
import org.apache.wicket.markup.head.HeaderItem

/**
 * SASS resource reference that references the main sass file. Can be used by the [ScipamatoPublicTheme]
 * in the development process of the sass/css classes. Should not be used in production.
 * Use [ScipamatoPublicCssReference] instead.
 *
 * **Usage:**
 *
 * ```
 * response.render(CssHeaderItem.forReference(ScipamatoPublicSassReference.INSTANCE));
 * ```
 *
 * @author [Michael Haitz](mailto:michael.haitz@agilecoders.de)
 * @author Urs Joss
 */
object ScipamatoPublicSassReference : SassResourceReference(
    ScipamatoPublicSassReference::class.java,
    "less/__main.less" // TODO migrate to SASS
) {

    override fun getDependencies(): List<HeaderItem> = Dependencies.combine(
        super.getDependencies(),
        CssHeaderItem.forReference(Bootstrap.getSettings().cssResourceReference)
    )
}
