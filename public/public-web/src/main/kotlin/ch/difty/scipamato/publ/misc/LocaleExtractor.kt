package ch.difty.scipamato.publ.misc

import java.util.Locale

/**
 * Implementations of this interface have the ability of parsing some kind of text value
 * to identify and return the Locale.
 */
fun interface LocaleExtractor {

    /**
     * Extracts the [Locale] from given textual [input].
     * If the input cannot be parsed correctly, the
     * default locale is returned.
     */
    fun extractLocaleFrom(input: String?): Locale
}
