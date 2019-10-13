package ch.difty.scipamato.publ.misc;

import java.util.Locale;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implementations of this interface have the ability
 * of parsing some kind of text value to identify and
 * return the Locale.
 */
@FunctionalInterface
public interface LocaleExtractor {

    /**
     * Extracts the {@link Locale} from given textual input.
     * If the input cannot be parsed correctly, the
     * default locale is returned.
     *
     * @param input
     *     the input value containing the locale
     * @return the locale encoded in the input.
     *     Is never null.
     */
    @NotNull
    Locale extractLocaleFrom(@Nullable String input);
}
