package ch.difty.scipamato.publ.web.themes.markup.html.publ;

import java.util.List;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.util.Dependencies;
import de.agilecoders.wicket.less.LessResourceReference;

/**
 * less resource reference that references `scipamato-public-bootstrap.css`. Can
 * be used by the ScipamatoPublicTheme in the development process of the
 * less/css classes. Should not be used in production. Use
 * {@link ScipamatoPublicCssReference} instead.
 * <p>
 * <b>Usage<b/>
 *
 * <pre>
 * response.render(CssHeaderItem.forReference(ScipamatoPublicLessReference.instance()));
 * </pre>
 *
 * @author Michael Haitz <michael.haitz@agilecoders.de>
 * @author Urs Joss
 */
public class ScipamatoPublicLessReference extends LessResourceReference {
    private static final long serialVersionUID = 1L;

    /**
     * @return singleton instance of {@link ScipamatoPublicLessReference}
     */
    public static ScipamatoPublicLessReference instance() {
        return Holder.INSTANCE;
    }

    /**
     * Singleton instance of this reference
     */
    private static final class Holder {
        private static final ScipamatoPublicLessReference INSTANCE = new ScipamatoPublicLessReference();
    }

    /**
     * Private constructor.
     */
    private ScipamatoPublicLessReference() {
        super(ScipamatoPublicLessReference.class, "less/__main.less");
    }

    @Override
    public List<HeaderItem> getDependencies() {
        return Dependencies.combine(super.getDependencies(), CssHeaderItem.forReference(Bootstrap.getSettings()
            .getCssResourceReference()));
    }
}
