package ch.difty.scipamato.core.sync.jobs.language

import ch.difty.scipamato.core.sync.PublicLanguage
import org.jooq.DSLContext
import ch.difty.scipamato.core.sync.jobs.ScipamatoItemWriter
import ch.difty.scipamato.publ.db.tables.Language

/**
 * [ScipamatoItemWriter] implementation to synchronize languages from scipamato-core to public.
 *
 *  * Takes care of inserts and updates.
 *  * Every record will be updated at least with the current timestamp in last_synched.
 */
class LanguageItemWriter(jooqDslContextPublic: DSLContext) : ScipamatoItemWriter<PublicLanguage>(jooqDslContextPublic, "language") {
    override fun executeUpdate(i: PublicLanguage): Int =
        dslContext
            .insertInto(Language.LANGUAGE)
            .columns(Language.LANGUAGE.CODE, Language.LANGUAGE.MAIN_LANGUAGE, Language.LANGUAGE.LAST_SYNCHED)
            .values(i.code, i.mainLanguage, i.lastSynched)
            .onConflict(Language.LANGUAGE.CODE)
            .doUpdate()
            .set(Language.LANGUAGE.CODE, i.code)
            .set(Language.LANGUAGE.MAIN_LANGUAGE, i.mainLanguage)
            .set(Language.LANGUAGE.LAST_SYNCHED, i.lastSynched)
            .where(Language.LANGUAGE.CODE.eq(i.code))
            .execute()
}
