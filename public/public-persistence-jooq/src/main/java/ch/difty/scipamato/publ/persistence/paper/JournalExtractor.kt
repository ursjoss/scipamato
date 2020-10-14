package ch.difty.scipamato.publ.persistence.paper

/**
 * Implementations of this interface extract the journal name from the location string.
 */
fun interface JournalExtractor {

    /**
     * Extracts the journal name from the provided [location] string.
     */
    fun extractJournal(location: String?): String
}
