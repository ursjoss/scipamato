package ch.difty.scipamato.publ.persistence.keyword

import ch.difty.scipamato.common.persistence.trimLanguageCode
import ch.difty.scipamato.publ.entity.Keyword
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
open class JooqKeywordRepo(private val dslContext: DSLContext) : KeywordRepository {

    @Suppress("MagicNumber")
    override fun findKeywords(languageCode: String): List<Keyword> = dslContext
        .select(
            ch.difty.scipamato.publ.db.tables.Keyword.KEYWORD.ID,
            ch.difty.scipamato.publ.db.tables.Keyword.KEYWORD.KEYWORD_ID,
            ch.difty.scipamato.publ.db.tables.Keyword.KEYWORD.LANG_CODE,
            ch.difty.scipamato.publ.db.tables.Keyword.KEYWORD.NAME,
            ch.difty.scipamato.publ.db.tables.Keyword.KEYWORD.SEARCH_OVERRIDE
        )
        .from(ch.difty.scipamato.publ.db.tables.Keyword.KEYWORD)
        .where(ch.difty.scipamato.publ.db.tables.Keyword.KEYWORD.LANG_CODE.eq(trimLanguageCode(languageCode)))
        .orderBy(ch.difty.scipamato.publ.db.tables.Keyword.KEYWORD.NAME)
        .map { r -> Keyword(r[0] as Int, r[1] as Int, r[2] as String?, r[3] as String?, r[4] as String?) }
}
