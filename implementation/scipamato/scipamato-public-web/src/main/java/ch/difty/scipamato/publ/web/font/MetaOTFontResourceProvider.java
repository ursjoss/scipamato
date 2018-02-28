package ch.difty.scipamato.publ.web.font;

import org.apache.wicket.request.resource.CssResourceReference;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.publ.config.ApplicationPublicProperties;
import ch.difty.scipamato.publ.web.CommercialFontResourceProvider;
import ch.difty.scipamato.publ.web.resources.MetaOTCssResourceReference;

/**
 * This class guards calls to the MetaOT fonts based on the property values
 * {@literal scipamato.commercial-font-present}.
 *
 * @author Urs Joss
 */
@Component
public class MetaOTFontResourceProvider implements CommercialFontResourceProvider {

    private final CssResourceReference cssResourceReference;

    public MetaOTFontResourceProvider(final ApplicationPublicProperties applicationProperties) {
        if (applicationProperties.isCommercialFontPresent()) {
            cssResourceReference = MetaOTCssResourceReference.get();
        } else {
            cssResourceReference = null;
        }
    }

    @Override
    public boolean isCommercialFontPresent() {
        return cssResourceReference != null;
    }

    @Override
    public CssResourceReference getCssResourceReference() {
        return cssResourceReference;
    }

}
