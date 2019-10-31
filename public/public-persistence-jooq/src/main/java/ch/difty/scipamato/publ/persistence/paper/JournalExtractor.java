package ch.difty.scipamato.publ.persistence.paper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
     *     location string from which the journal name is extracted
     * @return the journal name
     */
    @NotNull
    String extractJournal(@Nullable String location);
}
