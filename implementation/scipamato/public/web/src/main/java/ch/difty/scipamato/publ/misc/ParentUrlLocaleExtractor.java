package ch.difty.scipamato.publ.misc;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import ch.difty.scipamato.publ.config.ScipamatoPublicProperties;

/**
 * {@link LocaleExtractor} implementation that is capable of extracting
 * the locale string from a parentUrl passed in as input.
 * <p>
 * Examples of {@code parentUrl} and the resulting locales are:
 *
 * <ul>
 * <li>https://www.foo.ch/de/whatever/follows/next/ : Locale.German</li>
 * <li>https://www.foo.ch/en/projects/something/else/ : Locale.English</li>
 * <li>https://www.foo.ch/fr/bar/baz/ : LOCALE.FRENCH</li>
 * </ul>
 */
@Service
public class ParentUrlLocaleExtractor implements LocaleExtractor {

    private final String defaultLocale;

    private static final Pattern PATTERN = Pattern.compile("https?://?[^/]+/(\\w\\w)/.+", Pattern.CASE_INSENSITIVE);

    public ParentUrlLocaleExtractor(final ScipamatoPublicProperties properties) {
        this.defaultLocale = properties.getDefaultLocalization();
    }

    @Override
    public Locale extractLocaleFrom(final String input) {
        final String lt = extractLanguageCode(input);
        return Locale.forLanguageTag(lt);
    }

    private String extractLanguageCode(final String input) {
        if (input != null) {
            final Matcher matcher = PATTERN.matcher(input);
            if (matcher.find())
                return matcher.group(1);
        }
        return defaultLocale;
    }
}
