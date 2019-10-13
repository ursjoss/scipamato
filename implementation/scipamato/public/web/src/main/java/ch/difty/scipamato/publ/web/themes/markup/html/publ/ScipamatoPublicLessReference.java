package ch.difty.scipamato.publ.web.themes.markup.html.publ;

import java.util.List;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.util.Dependencies;
import de.agilecoders.wicket.less.LessResourceReference;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.jetbrains.annotations.NotNull;

/**
 * less resource reference that references the main less file. Can
 * be used by the ScipamatoPublicTheme in the development process of the
 * less/css classes. Should not be used in production. Use
 * {@link ScipamatoPublicCssReference} instead.
 * <p>
 * <strong>Usage:</strong>
 *
 * <pre>
 * response.render(CssHeaderItem.forReference(ScipamatoPublicLessReference.instance()));
 * </pre>
 *
 * @author <a href="mailto:michael.haitz@agilecoders.de">Michael Haitz</a>
 * @author Urs Joss
 */
public class ScipamatoPublicLessReference extends LessResourceReference {
    private static final long serialVersionUID = 1L;

    /**
     * Private constructor.
     */
    private ScipamatoPublicLessReference() {
        super(ScipamatoPublicLessReference.class, "less/__main.less");
    }

    /**
     * @return singleton instance of {@link ScipamatoPublicLessReference}
     */
    @NotNull
    public static ScipamatoPublicLessReference instance() {
        return Holder.INSTANCE;
    }

    /**
     * Singleton instance of this reference
     */
    private static final class Holder {
        private static final ScipamatoPublicLessReference INSTANCE = new ScipamatoPublicLessReference();
    }

    @NotNull
    @Override
    public List<HeaderItem> getDependencies() {
        return Dependencies.combine(super.getDependencies(), CssHeaderItem.forReference(Bootstrap
            .getSettings()
            .getCssResourceReference()));
    }
}
