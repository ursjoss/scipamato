package ch.difty.scipamato.publ.persistence.paper

/**
 * Implementations of this interface find a way to abbreviate the authors string to max a specific length.
 */
fun interface AuthorsAbbreviator {

    /**
     * @param authors author string that may need to be truncated
     * @return truncated authors string
     */
    fun abbreviate(authors: String?): String
}
