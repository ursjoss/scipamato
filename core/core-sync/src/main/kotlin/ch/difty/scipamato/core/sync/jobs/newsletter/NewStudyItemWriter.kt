package ch.difty.scipamato.core.sync.jobs.newsletter

import ch.difty.scipamato.core.sync.PublicNewStudy
import ch.difty.scipamato.core.sync.jobs.ScipamatoItemWriter
import ch.difty.scipamato.publ.db.tables.NewStudy
import org.jooq.DSLContext

/**
 * [ScipamatoItemWriter] implementation to synchronize newStudies from scipamato-core to public.
 *
 *  * Takes care of inserts and updates.
 *  * Every record will be updated at least with the current timestamp in last_synched.
 *  * id columns and audit fields are those of the scipamato-core tables
 */
class NewStudyItemWriter(jooqDslContextPublic: DSLContext) : ScipamatoItemWriter<PublicNewStudy>(jooqDslContextPublic, "newStudy") {
    override fun executeUpdate(i: PublicNewStudy): Int =
        dslContext
            .insertInto(NewStudy.NEW_STUDY)
            .columns(NewStudy.NEW_STUDY.NEWSLETTER_ID, NewStudy.NEW_STUDY.NEWSLETTER_TOPIC_ID, NewStudy.NEW_STUDY.SORT,
                NewStudy.NEW_STUDY.PAPER_NUMBER, NewStudy.NEW_STUDY.YEAR, NewStudy.NEW_STUDY.AUTHORS, NewStudy.NEW_STUDY.HEADLINE,
                NewStudy.NEW_STUDY.DESCRIPTION, NewStudy.NEW_STUDY.VERSION, NewStudy.NEW_STUDY.CREATED, NewStudy.NEW_STUDY.LAST_MODIFIED,
                NewStudy.NEW_STUDY.LAST_SYNCHED)
            .values(i.newsletterId, i.newsletterTopicId, i.sort, i.paperNumber, i.year,
                i.authors, i.headline, i.description, i.version, i.created,
                i.lastModified, i.lastSynched)
            .onConflict(NewStudy.NEW_STUDY.NEWSLETTER_ID, NewStudy.NEW_STUDY.PAPER_NUMBER)
            .doUpdate()
            .set(NewStudy.NEW_STUDY.NEWSLETTER_TOPIC_ID, i.newsletterTopicId)
            .set(NewStudy.NEW_STUDY.SORT, i.sort)
            .set(NewStudy.NEW_STUDY.PAPER_NUMBER, i.paperNumber)
            .set(NewStudy.NEW_STUDY.YEAR, i.year)
            .set(NewStudy.NEW_STUDY.AUTHORS, i.authors)
            .set(NewStudy.NEW_STUDY.HEADLINE, i.headline)
            .set(NewStudy.NEW_STUDY.DESCRIPTION, i.description)
            .set(NewStudy.NEW_STUDY.VERSION, i.version)
            .set(NewStudy.NEW_STUDY.CREATED, i.created)
            .set(NewStudy.NEW_STUDY.LAST_MODIFIED, i.lastModified)
            .set(NewStudy.NEW_STUDY.LAST_SYNCHED, i.lastSynched)
            .where(NewStudy.NEW_STUDY.NEWSLETTER_ID.eq(i.newsletterId).and(NewStudy.NEW_STUDY.PAPER_NUMBER.eq(i.paperNumber)))
            .execute()
}
