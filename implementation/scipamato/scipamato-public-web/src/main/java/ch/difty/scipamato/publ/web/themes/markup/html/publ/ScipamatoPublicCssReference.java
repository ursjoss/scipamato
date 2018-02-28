package ch.difty.scipamato.publ.web.themes.markup.html.publ;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.util.Dependencies;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.request.resource.CssResourceReference;

import java.util.List;

/**
 * css resource reference that references `scipamato-public-bootstrap.css`.
 * <p>
 * <b>Usage:</b>
 *
 * <pre>
 * response.render(CssHeaderItem.forReference(GoogleCssReference.instance()));
 * </pre>
 *
 * @author Michael Haitz <michael.haitz@agilecoders.de>
 * @author Urs Joss
 */
public class ScipamatoPublicCssReference extends CssResourceReference {
    private static final long serialVersionUID = 1L;

    /**
     * @return singleton instance of {@link ScipamatoPublicCssReference}
     */
    public static ScipamatoPublicCssReference instance() {
        return Holder.INSTANCE;
    }

    /**
     * Singleton instance of this reference
     */
    private static final class Holder {
        private static final ScipamatoPublicCssReference INSTANCE = new ScipamatoPublicCssReference();
    }

    /**
     * Private constructor.
     */
    private ScipamatoPublicCssReference() {
        super(ScipamatoPublicCssReference.class, "css/scipamato-public-bootstrap.min.css");
    }

    @Override
    public List<HeaderItem> getDependencies() {
        return Dependencies.combine(super.getDependencies(), CssHeaderItem.forReference(Bootstrap.getSettings()
            .getCssResourceReference()));
    }
}
