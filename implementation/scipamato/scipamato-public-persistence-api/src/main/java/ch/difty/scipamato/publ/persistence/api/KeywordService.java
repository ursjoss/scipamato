package ch.difty.scipamato.publ.persistence.api;

import java.util.List;

import ch.difty.scipamato.publ.entity.Keyword;

/**
 * Service operating with {@link Keyword}s.
 *
 * @author u.joss
 */
public interface KeywordService {

    /**
     * Find all keyword localized in language with the provided languageCode
     *
     * @param languageCode
     *     the language code, e.g. 'en' or 'de'.
     * @return a list of codes
     */
    List<Keyword> findKeywords(String languageCode);
}
