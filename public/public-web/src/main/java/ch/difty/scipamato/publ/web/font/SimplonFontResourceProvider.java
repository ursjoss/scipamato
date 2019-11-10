package ch.difty.scipamato.publ.web.font;

import org.apache.wicket.request.resource.CssResourceReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

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

    public SimplonFontResourceProvider(@NotNull final ApplicationPublicProperties applicationProperties) {
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

    @Nullable
    @Override
    public CssResourceReference getCssResourceReference() {
        return cssResourceReference;
    }
}
