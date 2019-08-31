package ch.difty.scipamato.core.logic.parsing

import ch.difty.scipamato.common.logger
import ch.difty.scipamato.core.entity.Paper
import java.util.regex.Pattern

private val log = logger()

/**
 * Parses a property value to define the AuthorParserStrategy to be used.
 */
enum class AuthorParserStrategy {

    /**
     * The [AuthorParserStrategy] interpreting PubMed Authors string
     */
    PUBMED;

    companion object {

        private val STRATEGIES = values()

        /**
         * Converts the string based [propertyValue] into the proper [AuthorParserStrategy] enum value.
         * Accepts a [propertyKey] for logging purposes
         */
        fun fromProperty(propertyValue: String, propertyKey: String): AuthorParserStrategy =
                propertyValue.asProperty(STRATEGIES, PUBMED, propertyKey)
    }
}

/**
 * Utility class to lex and parse Author strings in the PubMed format. From the
 * list of parsed authors it can return the first author.
 *
 * An example of a typical PubMed author string is:
 *
 * `Turner MC, Cohen A, Jerret M, Gapstur SM, Driver WR, Pope CA 3rd, Krewsky D, Beckermann BS, Samet JM.`
 *
 *
 *  * An author can have one or more names + initials.
 *  * One or more authors appear in a comma delimited list.
 *  * The last character is a period.
 *
 *
 * **Note:** The entity [Paper] currently has a static JSR303 bean
 * validation regex making sure the author string only contains an authors
 * format compliant with the PubmedAuthorParser. Once we have more than one
 * author parser strategy, we need a different (more dynamic) way of validating
 * the author strings in the entity.
 *
 * @author u.joss
 */
@Suppress("SpellCheckingInspection")
class PubmedAuthorParser(authorsString: String) : AuthorParser {

    override val authorsString: String = authorsString.trim { it <= ' ' }
    override val authors: List<Author> = lexedAuthors().map(::parseAuthor)
    override val firstAuthor: String? = authors.firstOrNull()?.lastName

    private fun lexedAuthors(): List<String> {
        val dotlessAuthors = if (authorsString.endsWith(".")) authorsString.dropLast(1) else authorsString
        return dotlessAuthors.split(AUTHORS_SEPARATOR_REGEX).dropLastWhile { it.isEmpty() }
    }

    private fun parseAuthor(authorString: String): Author {
        var lastName = authorString
        var firstName = ""

        val tokens = authorString.split(TOKEN_SEPARATOR_REGEX).dropLastWhile { it.isEmpty() }
        if (tokens.size > 1) {
            val i = tokens.firstNameIndex()
            firstName = tokens.subList(i, tokens.size).joinToString(" ").trim { it <= ' ' }
            lastName = tokens.subList(0, i).joinToString(" ").trim { it <= ' ' }
        }
        return Author(authorString, lastName, firstName)
    }

    private fun List<String>.firstNameIndex(): Int {
        val i = size - 1
        return if (CARDINALITY_PATTERN.matcher(this[i]).find()) i - 1 else i
    }

    companion object {
        private val AUTHORS_SEPARATOR_REGEX = " *, *".toRegex()
        private val TOKEN_SEPARATOR_REGEX = " +".toRegex()
        private val CARDINALITY_PATTERN = Pattern.compile("(?:1st|2nd|3rd)|(?:\\d+th)|(?:Jr)")
    }
}


/**
 * Derive an enum of type [T] from a configuration [this@toProperty], all [values] of the enum (`T.values`),
 * a [defaultValue] in case the property value does not match and a [propertyKey] used for logging.
 */
internal fun <T : Enum<T>> String.asProperty(values: Array<T>, defaultValue: T, propertyKey: String): T {
    if (isNotBlank()) {
        values.filter { equals(it.name, ignoreCase = true) }.take(1).apply {
            if (isNotEmpty()) {
                log.info("{}={}", propertyKey, this@asProperty)
                return first()
            }
        }
    }
    val msg = "{} is not properly defined. Current value: '{}' - now using {} - " + "specify one of {} in your property configuration (e.g. application.properties)."
    log.warn(msg, propertyKey, this, defaultValue, values)
    return defaultValue
}
