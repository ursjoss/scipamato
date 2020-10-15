package ch.difty.scipamato.publ.persistence.keyword

import ch.difty.scipamato.publ.entity.Keyword
import ch.difty.scipamato.publ.persistence.api.KeywordService
import org.springframework.stereotype.Service

/**
 * jOOQ specific implementation of the [KeywordService] interface.
 */
@Service
class JooqKeywordService(private val repo: KeywordRepository) : KeywordService {
    override fun findKeywords(languageCode: String): List<Keyword> = repo.findKeywords(languageCode)
}
