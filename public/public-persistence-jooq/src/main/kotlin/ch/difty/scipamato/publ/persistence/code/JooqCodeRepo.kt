package ch.difty.scipamato.publ.persistence.code

import ch.difty.scipamato.common.entity.CodeClassId
import ch.difty.scipamato.common.persistence.trimLanguageCode
import ch.difty.scipamato.publ.entity.Code
import org.jooq.DSLContext
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Repository

@Repository
@CacheConfig(cacheNames = ["codes"])
open class JooqCodeRepo(private val dslContext: DSLContext) : CodeRepository {

    @Suppress("MagicNumber")
    @Cacheable
    override fun findCodesOfClass(codeClassId: CodeClassId, languageCode: String): List<Code> = dslContext
        .select(
            ch.difty.scipamato.publ.db.tables.Code.CODE.CODE_CLASS_ID,
            ch.difty.scipamato.publ.db.tables.Code.CODE.CODE_,
            ch.difty.scipamato.publ.db.tables.Code.CODE.LANG_CODE,
            ch.difty.scipamato.publ.db.tables.Code.CODE.NAME,
            ch.difty.scipamato.publ.db.tables.Code.CODE.COMMENT,
            ch.difty.scipamato.publ.db.tables.Code.CODE.SORT
        ).from(ch.difty.scipamato.publ.db.tables.Code.CODE)
        .where(ch.difty.scipamato.publ.db.tables.Code.CODE.LANG_CODE.eq(trimLanguageCode(languageCode))
            .and(ch.difty.scipamato.publ.db.tables.Code.CODE.CODE_CLASS_ID.equal(codeClassId.id)))
        .orderBy(
            ch.difty.scipamato.publ.db.tables.Code.CODE.CODE_CLASS_ID,
            ch.difty.scipamato.publ.db.tables.Code.CODE.SORT
        ).map { r -> Code(r[0] as Int?, r[1] as String?, r[2] as String?, r[3] as String?, r[4] as String?, r[5] as Int) }
}
