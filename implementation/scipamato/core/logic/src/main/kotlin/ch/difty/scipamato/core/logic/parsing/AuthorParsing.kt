package ch.difty.scipamato.core.logic.parsing

/**
 * data class used by an author parser to represent one individual author,
 * extracted from the raw author string passed to its constructor.
 */
data class Author(val authorString: String, val lastName: String, val firstName: String)

/**
 * Implementations of the [AuthorParser] interface accept an
 * `author` String (typically as a constructor argument). The string is
 * lexed and parsed and individual authors are maintained. There are various
 * ways to access the parsed authors, especially the `First Author` is of
 * importance.
 */
interface AuthorParser {
    val firstAuthor: String?
    val authorsString: String
    val authors: List<Author>
}

/**
 * The implementation of the [AuthorParserFactory] provides a configured [AuthorParser]
 * able parse the provided property String `authorString`.
 */
interface AuthorParserFactory {

    /**
     * Creates an implementation of an [AuthorParser] depending on the provided `author string`.
     * Returns a `PUBMED` [AuthorParser]
     */
    fun createParser(authorString: String): AuthorParser

    companion object {
        fun create(authorParserStrategy: AuthorParserStrategy) = object : AuthorParserFactory {
            override fun createParser(authorString: String): AuthorParser = when (authorParserStrategy) {
                AuthorParserStrategy.PUBMED -> PubmedAuthorParser(authorString)
            }
        }
    }
}
