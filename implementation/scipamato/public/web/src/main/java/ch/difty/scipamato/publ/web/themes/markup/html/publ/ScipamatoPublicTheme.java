package ch.difty.scipamato.publ.web.themes.markup.html.publ;

import java.util.Collections;
import java.util.List;

import de.agilecoders.wicket.core.settings.Theme;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;

/**
 * Java representation of the SciPaMaTo-Public Theme. Is based on TODC Bootstrap
 * and the wicket-bootstrap implementation of Michael Haitz in wicket-bootstrap.
 * <p>
 * A flag passed into the constructor indicates whether the precompiled CSS
 * classes will be used (preferable in production) or if the LESS files shall be
 * dynamically compiled into CSS.
 * <p>
 * This theme modifies bootstrap and therefore both css files get loaded
 * (`bootstrap.css` and `scipamato-public-bootstrap.css`).
 *
 * <span class="strong">Usage:</span>
 *
 * <pre>
 * settings.setThemeProvider(new SingleThemeProvider(new ScipamatoPublicTheme()));
 * </pre>
 *
 * @author <a href="mailto:michael.haitz@agilecoders.de">Michael Haitz</a>
 * @author Urs Joss
 */
public class ScipamatoPublicTheme extends Theme {

    private final boolean useLessOverCss;

    /**
     * Instantiates a new ScipamatoPublicTheme instance.
     *
     * @param useLessOverCss
     *     if true: SciPaMaTo will dynamically compile the LESS files into
     *     CSS. If false, the precompiled CSS is used.
     * @param name
     *     name for the theme
     */
    public ScipamatoPublicTheme(final boolean useLessOverCss, final String name) {
        super(name);
        this.useLessOverCss = useLessOverCss;
    }

    /**
     * Instantiates a new ScipamatoPublicTheme instance with the default name
     * {@literal scipamato-public}.
     *
     * @param useLessOverCss
     *     if true: SciPaMaTo will dynamically compile the LESS files into
     *     CSS. If false, the precompiled CSS is used.
     */
    public ScipamatoPublicTheme(final boolean useLessOverCss) {
        this(useLessOverCss, "scipamato-public");
    }

    @Override
    public List<HeaderItem> getDependencies() {
        return Collections.singletonList(makeHeaderItem());
    }

    private CssHeaderItem makeHeaderItem() {
        if (useLessOverCss)
            return CssHeaderItem
                .forReference(ScipamatoPublicLessReference.instance())
                .setId(BOOTSTRAP_THEME_MARKUP_ID);
        else
            return CssHeaderItem
                .forReference(ScipamatoPublicCssReference.instance())
                .setId(BOOTSTRAP_THEME_MARKUP_ID);
    }
}
