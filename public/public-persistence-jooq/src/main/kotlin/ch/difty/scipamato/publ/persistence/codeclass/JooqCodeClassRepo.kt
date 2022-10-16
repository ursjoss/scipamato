package ch.difty.scipamato.publ.persistence.codeclass

import ch.difty.scipamato.common.persistence.trimLanguageCode
import ch.difty.scipamato.publ.entity.CodeClass
import org.jooq.DSLContext
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Repository

@Repository
@CacheConfig(cacheNames = ["codeClasses"])
open class JooqCodeClassRepo(private val dslContext: DSLContext) : CodeClassRepository {

    @Suppress("MagicNumber")
    @Cacheable
    override fun find(languageCode: String): List<CodeClass> = dslContext
        .select(
            ch.difty.scipamato.publ.db.tables.CodeClass.CODE_CLASS.CODE_CLASS_ID,
            ch.difty.scipamato.publ.db.tables.CodeClass.CODE_CLASS.LANG_CODE,
            ch.difty.scipamato.publ.db.tables.CodeClass.CODE_CLASS.NAME,
            ch.difty.scipamato.publ.db.tables.CodeClass.CODE_CLASS.DESCRIPTION,
        )
        .from(ch.difty.scipamato.publ.db.tables.CodeClass.CODE_CLASS)
        .where(ch.difty.scipamato.publ.db.tables.CodeClass.CODE_CLASS.LANG_CODE.eq(trimLanguageCode(languageCode)))
        .orderBy(ch.difty.scipamato.publ.db.tables.CodeClass.CODE_CLASS.CODE_CLASS_ID)
        .map { r -> CodeClass(r[0] as Int?, r[1] as String, r[2] as String?, r[3] as String) }
}
