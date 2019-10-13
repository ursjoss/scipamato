package ch.difty.scipamato.publ.persistence.keyword;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import ch.difty.scipamato.publ.entity.Keyword;

/**
 * Provides access to the localized {@link Keyword}s.
 *
 * @author u.joss
 */
public interface KeywordRepository {

    /**
     * Find all keywords localized in language with the provided languageCode
     *
     * @param languageCode
     *     the language code, e.g. 'en' or 'de'
     * @return a list of keywords
     */
    @NotNull
    List<Keyword> findKeywords(@NotNull String languageCode);
}
