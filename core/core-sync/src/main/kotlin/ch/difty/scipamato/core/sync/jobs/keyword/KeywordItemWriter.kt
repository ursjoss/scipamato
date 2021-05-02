package ch.difty.scipamato.core.sync.jobs.keyword

import ch.difty.scipamato.core.sync.PublicKeyword
import org.jooq.DSLContext
import ch.difty.scipamato.core.sync.jobs.ScipamatoItemWriter
import ch.difty.scipamato.publ.db.tables.Keyword

/**
 * [ScipamatoItemWriter] implementation to synchronize keywords from scipamato-core to public.
 *
 *  * Takes care of inserts and updates.
 *  * Every record will be updated at least with the current timestamp in last_synched.
 *  * id columns and audit fields are those of the scipamato-core tables
 */
class KeywordItemWriter(jooqDslContextPublic: DSLContext) : ScipamatoItemWriter<PublicKeyword>(jooqDslContextPublic, "keyword") {

    override fun executeUpdate(i: PublicKeyword): Int =
        dslContext
            .insertInto(Keyword.KEYWORD)
            .columns(Keyword.KEYWORD.ID, Keyword.KEYWORD.KEYWORD_ID, Keyword.KEYWORD.LANG_CODE, Keyword.KEYWORD.NAME, Keyword.KEYWORD.VERSION, Keyword.KEYWORD.CREATED,
                    Keyword.KEYWORD.LAST_MODIFIED, Keyword.KEYWORD.LAST_SYNCHED, Keyword.KEYWORD.SEARCH_OVERRIDE)
            .values(i.id, i.keywordId, i.langCode, i.name, i.version, i.created,i.lastModified, i.lastSynched, i.searchOverride)
            .onConflict(Keyword.KEYWORD.ID)
            .doUpdate()
            .set(Keyword.KEYWORD.KEYWORD_ID, i.keywordId)
            .set(Keyword.KEYWORD.LANG_CODE, i.langCode)
            .set(Keyword.KEYWORD.NAME, i.name)
            .set(Keyword.KEYWORD.VERSION, i.version)
            .set(Keyword.KEYWORD.CREATED, i.created)
            .set(Keyword.KEYWORD.LAST_MODIFIED, i.lastModified)
            .set(Keyword.KEYWORD.LAST_SYNCHED, i.lastSynched)
            .set(Keyword.KEYWORD.SEARCH_OVERRIDE, i.searchOverride)
            .where(Keyword.KEYWORD.ID.eq(i.id))
            .execute()
}
