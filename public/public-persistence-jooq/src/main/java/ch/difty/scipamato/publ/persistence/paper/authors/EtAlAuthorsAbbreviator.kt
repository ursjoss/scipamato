package ch.difty.scipamato.publ.persistence.paper.authors

import ch.difty.scipamato.publ.config.ApplicationPublicProperties
import ch.difty.scipamato.publ.persistence.paper.AuthorsAbbreviator
import org.springframework.stereotype.Component

/**
 * Implementation of [AuthorsAbbreviator] truncating author strings longer
 * than 80 characters and adding ' et al.'.
 *
 * @author Urs Joss
 */
@Component
internal class EtAlAuthorsAbbreviator(properties: ApplicationPublicProperties) : AuthorsAbbreviator {

    private val authorsAbbreviatedMaxLength: Int = properties.authorsAbbreviatedMaxLength
    private val authorsAbbreviatedCutOff: Int = authorsAbbreviatedMaxLength - ET_AL.length
    private val authorsTruncatedCutOff: Int = authorsAbbreviatedMaxLength - ELLIPSIS.length

    override fun abbreviate(authors: String?): String {
        if (authors == null) return ""
        if (noNeedToAbbreviate(authors)) return authors
        val authorsHead = authors.substring(0, authorsAbbreviatedCutOff)
        return if (oneOrMoreEntireAuthorsTruncated(authors)) "$authorsHead$ET_AL"
        else fixAuthorsHead(authors, authorsHead.lastIndexOf(','))
    }

    private fun noNeedToAbbreviate(authors: String): Boolean =
        authors.length <= authorsAbbreviatedMaxLength || authorsAbbreviatedMaxLength == 0

    /**
     * The character after the truncated part is a ',' -> the truncated string is a
     * full author. No need to skip the partially cut-off last author.
     */
    private fun oneOrMoreEntireAuthorsTruncated(authors: String): Boolean = authors[authorsAbbreviatedCutOff] == ','

    /**
     * If there are multiple authors, skip last partially cut off author and add 'et al.'.
     * If however only one very long author is present (no comma), truncate its name and add ellipsis.
     */
    private fun fixAuthorsHead(authors: String, lastComma: Int): String =
        if (lastComma > 0) "${authors.substring(0, lastComma)}$ET_AL"
        else "${authors.substring(0, authorsTruncatedCutOff)}$ELLIPSIS"

    companion object {
        private const val ET_AL = " et al."
        private const val ELLIPSIS = "..."
    }
}
