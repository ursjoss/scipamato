package ch.difty.scipamato.publ.persistence.api

import ch.difty.scipamato.common.persistence.CodeClassLikeService
import ch.difty.scipamato.common.persistence.CodeLikeService
import ch.difty.scipamato.publ.entity.Code
import ch.difty.scipamato.publ.entity.CodeClass
import ch.difty.scipamato.publ.entity.Keyword

/**
 * Service operating with [Code]s.
 */
interface CodeService : CodeLikeService<Code>

/**
 * Service operating with [CodeClass]es.
 */
interface CodeClassService : CodeClassLikeService<CodeClass>

/**
 * Service operating with [Keyword]s.
 */
interface KeywordService {
    /**
     * Find all keyword localized in language with the provided [languageCode] (e.g. 'en' or 'de')
     */
    fun findKeywords(languageCode: String): List<Keyword>
}
