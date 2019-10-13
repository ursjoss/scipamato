package ch.difty.scipamato.publ.persistence.paper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implementations of this interface find a way to abbreviate the authors string
 * to max a specific length.
 *
 * @author Urs Joss
 */
@FunctionalInterface
public interface AuthorsAbbreviator {

    /**
     * @param authors
     *     author string that may need to be truncated
     * @return truncated authors string
     */
    @NotNull
    String abbreviate(@Nullable String authors);
}
