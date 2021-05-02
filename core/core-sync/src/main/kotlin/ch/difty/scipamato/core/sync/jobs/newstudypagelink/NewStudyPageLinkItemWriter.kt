package ch.difty.scipamato.core.sync.jobs.newstudypagelink

import ch.difty.scipamato.core.sync.PublicNewStudyPageLink
import ch.difty.scipamato.core.sync.jobs.ScipamatoItemWriter
import ch.difty.scipamato.publ.db.tables.NewStudyPageLink
import org.jooq.DSLContext

/**
 * [ScipamatoItemWriter] implementation to synchronize newStudyPageLinks from scipamato-core to public.
 *
 *  * Takes care of inserts and updates.
 *  * Every record will be updated at least with the current timestamp in last_synched.
 */
class NewStudyPageLinkItemWriter(jooqDslContextPublic: DSLContext) : ScipamatoItemWriter<PublicNewStudyPageLink>(jooqDslContextPublic, "newStudyPageLink") {
    override fun executeUpdate(i: PublicNewStudyPageLink): Int =
        dslContext
            .insertInto(NewStudyPageLink.NEW_STUDY_PAGE_LINK)
            .columns(NewStudyPageLink.NEW_STUDY_PAGE_LINK.LANG_CODE, NewStudyPageLink.NEW_STUDY_PAGE_LINK.SORT,
                NewStudyPageLink.NEW_STUDY_PAGE_LINK.TITLE, NewStudyPageLink.NEW_STUDY_PAGE_LINK.URL,
                NewStudyPageLink.NEW_STUDY_PAGE_LINK.LAST_SYNCHED
            )
            .values(i.langCode, i.sort, i.title, i.url, i.lastSynched)
            .onConflict(NewStudyPageLink.NEW_STUDY_PAGE_LINK.LANG_CODE, NewStudyPageLink.NEW_STUDY_PAGE_LINK.SORT)
            .doUpdate()
            .set(NewStudyPageLink.NEW_STUDY_PAGE_LINK.LANG_CODE, i.langCode)
            .set(NewStudyPageLink.NEW_STUDY_PAGE_LINK.SORT, i.sort)
            .set(NewStudyPageLink.NEW_STUDY_PAGE_LINK.TITLE, i.title)
            .set(NewStudyPageLink.NEW_STUDY_PAGE_LINK.URL, i.url)
            .set(NewStudyPageLink.NEW_STUDY_PAGE_LINK.LAST_SYNCHED, i.lastSynched)
            .where(NewStudyPageLink.NEW_STUDY_PAGE_LINK.LANG_CODE.eq(i.langCode).and(NewStudyPageLink.NEW_STUDY_PAGE_LINK.SORT.eq(i.sort)))
            .execute()
}
