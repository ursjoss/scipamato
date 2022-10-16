package ch.difty.scipamato.core.sync.jobs.code

import ch.difty.scipamato.core.sync.PublicCode
import org.jooq.DSLContext
import ch.difty.scipamato.core.sync.jobs.ScipamatoItemWriter
import ch.difty.scipamato.publ.db.tables.Code

/**
 * [ScipamatoItemWriter] implementation to synchronize codes from scipamato-core to public.
 *
 *  * Takes care of inserts and updates.
 *  * Every record will be updated at least with the current timestamp in last_synched.
 *  * id columns and audit fields are those of the scipamato-core tables
 */
class CodeItemWriter(
    jooqDslContextPublic: DSLContext
) : ScipamatoItemWriter<PublicCode>(jooqDslContextPublic, "code") {

    override fun executeUpdate(i: PublicCode): Int =
        dslContext
            .insertInto(Code.CODE)
            .columns(Code.CODE.CODE_, Code.CODE.LANG_CODE, Code.CODE.CODE_CLASS_ID, Code.CODE.NAME, Code.CODE.COMMENT,
                Code.CODE.SORT, Code.CODE.VERSION, Code.CODE.CREATED, Code.CODE.LAST_MODIFIED, Code.CODE.LAST_SYNCHED)
            .values(
                i.code, i.langCode, i.codeClassId, i.name, i.comment, i.sort, i.version,
                i.created, i.lastModified, i.lastSynched
            ).onConflict(Code.CODE.CODE_, Code.CODE.LANG_CODE)
            .doUpdate()
            .set(Code.CODE.CODE_CLASS_ID, i.codeClassId)
            .set(Code.CODE.NAME, i.name)
            .set(Code.CODE.COMMENT, i.comment)
            .set(Code.CODE.SORT, i.sort)
            .set(Code.CODE.VERSION, i.version)
            .set(Code.CODE.CREATED, i.created)
            .set(Code.CODE.LAST_MODIFIED, i.lastModified)
            .set(Code.CODE.LAST_SYNCHED, i.lastSynched)
            .where(Code.CODE.CODE_.eq(i.code).and(Code.CODE.LANG_CODE.eq(i.langCode)))
            .execute()
}
