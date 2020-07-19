package ch.difty.scipamato.publ.web.themes.markup.html.publ

import de.agilecoders.wicket.core.Bootstrap
import de.agilecoders.wicket.core.util.Dependencies
import de.agilecoders.wicket.less.LessResourceReference
import org.apache.wicket.markup.head.CssHeaderItem
import org.apache.wicket.markup.head.HeaderItem

/**
 * less resource reference that references the main less file. Can be used by the [ScipamatoPublicTheme]
 * in the development process of the less/css classes. Should not be used in production.
 * Use [ScipamatoPublicCssReference] instead.
 *
 * **Usage:**
 *
 * ```
 * response.render(CssHeaderItem.forReference(ScipamatoPublicLessReference.INSTANCE));
 * ```
 *
 * @author [Michael Haitz](mailto:michael.haitz@agilecoders.de)
 * @author Urs Joss
 */
object ScipamatoPublicLessReference : LessResourceReference(
    ScipamatoPublicLessReference::class.java,
    "less/__main.less"
) {

    override fun getDependencies(): List<HeaderItem> = Dependencies.combine(
        super.getDependencies(),
        CssHeaderItem.forReference(Bootstrap.getSettings().cssResourceReference)
    )
}
