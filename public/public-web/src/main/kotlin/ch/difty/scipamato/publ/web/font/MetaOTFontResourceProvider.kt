package ch.difty.scipamato.publ.web.font

import ch.difty.scipamato.publ.config.ApplicationPublicProperties
import ch.difty.scipamato.publ.web.CommercialFontResourceProvider
import ch.difty.scipamato.publ.web.resources.MetaOTCssResourceReference
import org.apache.wicket.request.resource.CssResourceReference
import org.springframework.stereotype.Component

/**
 * This class guards calls to the MetaOT fonts based on the property values scipamato.commercial-font-present.
 */
@Component
class MetaOTFontResourceProvider(applicationProperties: ApplicationPublicProperties) : CommercialFontResourceProvider {

    private val _cssResourceReference: CssResourceReference? =
        if (applicationProperties.isCommercialFontPresent) MetaOTCssResourceReference
        else null

    override val isCommercialFontPresent: Boolean get() = _cssResourceReference != null

    override val cssResourceReference: CssResourceReference? get() = _cssResourceReference
}
