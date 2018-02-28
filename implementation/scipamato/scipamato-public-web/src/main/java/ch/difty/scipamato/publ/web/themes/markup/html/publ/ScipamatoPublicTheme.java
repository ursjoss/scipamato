package ch.difty.scipamato.publ.web.themes.markup.html.publ;

import java.util.Collections;
import java.util.List;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;

import de.agilecoders.wicket.core.settings.Theme;

/**
 * #### Description
 *
 * java representation of the SciPaMaTo-Public Theme. Is based on TODC Bootstrap
 * and the wicket-bootstrap implementation of Michael Haitz
 * <michael.haitz@agilecoders.de> in wicket-bootstrap.
 * 
 * This theme modifies bootstrap and therefore both files gets loaded
 * (`bootstrap.css` and `scipamato-public-bootstrap.css`).
 * <p>
 * <b>Usage<b/>
 *
 * <pre>
 * settings.setThemeProvider(new SingleThemeProvider(new ScipamatoPublicTheme()));
 * </pre>
 *
 * @author Michael Haitz <michael.haitz@agilecoders.de>
 * @author Urs Joss
 */
public class ScipamatoPublicTheme extends Theme {

    /**
     * Construct.
     */
    public ScipamatoPublicTheme(final String name) {
        super(name);
    }

    /**
     * Construct using default theme name: `google`
     */
    public ScipamatoPublicTheme() {
        this("scipamato-public");
    }

    @Override
    public List<HeaderItem> getDependencies() {
        // TODO using {@link ScipamatoPublicLessReference} during development. Replace
        // with {@link ScipamatoPublicCssReference} later
        // TODO consider using a configuration based approach to switch between less and
        // css reference
        HeaderItem headerItem = CssHeaderItem.forReference(ScipamatoPublicCssReference.instance())
            .setId(BOOTSTRAP_THEME_MARKUP_ID);
        return Collections.singletonList(headerItem);
    }
}
