package ch.difty.scipamato.publ.misc

import ch.difty.scipamato.publ.config.ScipamatoPublicProperties
import org.springframework.stereotype.Service
import java.util.Locale
import java.util.regex.Pattern

/**
 * [LocaleExtractor] implementation that is capable of extracting
 * the locale string from a parentUrl passed in as input.
 *
 * Examples of `parentUrl` and the resulting locales are:
 *
 *  * https://www.foo.ch/de/whatever/follows/next/ : Locale.German
 *  * https://www.foo.ch/en/projects/something/else/ : Locale.English
 *  * https://www.foo.ch/fr/bar/baz/ : LOCALE.FRENCH
 */
@Service
class ParentUrlLocaleExtractor(properties: ScipamatoPublicProperties) : LocaleExtractor {

    private val defaultLocale: String = properties.defaultLocalization

    override fun extractLocaleFrom(input: String?): Locale = Locale.forLanguageTag(extractLanguageCode(input))

    private fun extractLanguageCode(input: String?): String {
        if (input != null) {
            val matcher = PATTERN.matcher(input)
            if (matcher.find()) return matcher.group(1)
        }
        return defaultLocale
    }

    companion object {
        private val PATTERN = Pattern.compile("""https?://?[^/]+/(\w\w)/.+""", Pattern.CASE_INSENSITIVE)
    }
}
