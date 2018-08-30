package ch.difty.scipamato.publ.web.font;

import org.apache.wicket.request.resource.CssResourceReference;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.publ.config.ApplicationPublicProperties;
import ch.difty.scipamato.publ.web.CommercialFontResourceProvider;
import ch.difty.scipamato.publ.web.resources.IcoMoonCssResourceReference;

/**
 * This class guards calls to the IcoMoon fonts based on the property values
 * {@literal scipamato.commercial-font-present}.
 *
 * @author Urs Joss
 */
@Component
public class IcoMoonFontResourceProvider implements CommercialFontResourceProvider {

    private final CssResourceReference cssResourceReference;

    public IcoMoonFontResourceProvider(final ApplicationPublicProperties applicationProperties) {
        AssertAs.notNull(applicationProperties, "applicationProperties");
        if (applicationProperties.isCommercialFontPresent()) {
            cssResourceReference = IcoMoonCssResourceReference.get();
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
