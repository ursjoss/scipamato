package ch.difty.scipamato.publ.persistence.paper;

/**
 * Implementations of this interface extract the journal name from the location
 * string.
 *
 * @author Urs Joss
 */
@FunctionalInterface
public interface JournalExtractor {

    /**
     * Extracts the journal name from the provided location string.
     *
     * @param location
     *            location string from which the journal name is extracted
     * @return the journal name
     */
    String extractJournal(String location);
}
