package ch.difty.scipamato.publ.persistence.keyword

import ch.difty.scipamato.publ.entity.Keyword

/**
 * Provides access to the localized [Keyword]s.
 */
interface KeywordRepository {

    /**
     * Find all keywords localized in language with the provided [languageCode]
     */
    fun findKeywords(languageCode: String): List<Keyword>
}
