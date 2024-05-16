package ch.difty.scipamato.core.sync.jobs.codeclass

import ch.difty.scipamato.core.sync.PublicCodeClass
import ch.difty.scipamato.core.sync.jobs.ScipamatoItemWriter
import ch.difty.scipamato.publ.db.tables.CodeClass
import org.jooq.DSLContext

/**
 * [ScipamatoItemWriter] implementation to synchronize code classes from scipamato-core to public.
 *
 *  * Takes care of inserts and updates.
 *  * Every record will be updated at least with the current timestamp in last_synched.
 *  * id columns and audit fields are those of the scipamato-core tables
 */
class CodeClassItemWriter(jooqDslContextPublic: DSLContext)
    : ScipamatoItemWriter<PublicCodeClass>(jooqDslContextPublic, "code_class") {
    override fun executeUpdate(i: PublicCodeClass): Int =
        dslContext
            .insertInto(CodeClass.CODE_CLASS)
            .columns(CodeClass.CODE_CLASS.CODE_CLASS_ID, CodeClass.CODE_CLASS.LANG_CODE, CodeClass.CODE_CLASS.NAME, CodeClass.CODE_CLASS.DESCRIPTION,
                CodeClass.CODE_CLASS.VERSION, CodeClass.CODE_CLASS.CREATED, CodeClass.CODE_CLASS.LAST_MODIFIED, CodeClass.CODE_CLASS.LAST_SYNCHED)
            .values(i.codeClassId, i.langCode, i.name, i.description, i.version, i.created, i.lastModified, i.lastSynched)
            .onConflict(CodeClass.CODE_CLASS.CODE_CLASS_ID, CodeClass.CODE_CLASS.LANG_CODE)
            .doUpdate()
            .set(CodeClass.CODE_CLASS.NAME, i.name)
            .set(CodeClass.CODE_CLASS.DESCRIPTION, i.description)
            .set(CodeClass.CODE_CLASS.VERSION, i.version)
            .set(CodeClass.CODE_CLASS.CREATED, i.created)
            .set(CodeClass.CODE_CLASS.LAST_MODIFIED, i.lastModified)
            .set(CodeClass.CODE_CLASS.LAST_SYNCHED, i.lastSynched)
            .where(CodeClass.CODE_CLASS.CODE_CLASS_ID.eq(i.codeClassId).and(CodeClass.CODE_CLASS.LANG_CODE.eq(i.langCode)))
            .execute()
}
