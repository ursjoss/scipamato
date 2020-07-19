package ch.difty.scipamato.publ.web.themes.markup.html.publ

import de.agilecoders.wicket.core.Bootstrap
import de.agilecoders.wicket.core.util.Dependencies
import org.apache.wicket.markup.head.CssHeaderItem
import org.apache.wicket.markup.head.HeaderItem
import org.apache.wicket.request.resource.CssResourceReference

/**
 * css resource reference that references `scipamato-public-bootstrap.css`.
 *
 * **Usage:**
 *
 * ```
 * response.render(CssHeaderItem.forReference(ScipamatoPublicCssReference.INSTANCE));
 * ```
 *
 * @author [Michael Haitz](mailto:michael.haitz@agilecoders.de)
 * @author Urs Joss
 */
object ScipamatoPublicCssReference : CssResourceReference(
    ScipamatoPublicCssReference::class.java,
    "css/scipamato-public-bootstrap.min.css"
) {

    override fun getDependencies(): List<HeaderItem> = Dependencies.combine(
        super.getDependencies(),
        CssHeaderItem.forReference(Bootstrap.getSettings().cssResourceReference)
    )
}
