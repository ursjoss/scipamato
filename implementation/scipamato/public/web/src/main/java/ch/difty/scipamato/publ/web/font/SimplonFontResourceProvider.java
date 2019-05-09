package ch.difty.scipamato.publ.web.font;

import org.apache.wicket.request.resource.CssResourceReference;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.publ.config.ApplicationPublicProperties;
import ch.difty.scipamato.publ.web.CommercialFontResourceProvider;
import ch.difty.scipamato.publ.web.resources.SimplonCssResourceReference;

/**
 * This class guards calls to the Simplon fonts based on the property values
 * {@literal scipamato.commercial-font-present}.
 *
 * @author Urs Joss
 */
@Component
public class SimplonFontResourceProvider implements CommercialFontResourceProvider {

    private final CssResourceReference cssResourceReference;

    public SimplonFontResourceProvider(final ApplicationPublicProperties applicationProperties) {
        AssertAs.notNull(applicationProperties, "applicationProperties");
        if (applicationProperties.isCommercialFontPresent()) {
            cssResourceReference = SimplonCssResourceReference.get();
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
