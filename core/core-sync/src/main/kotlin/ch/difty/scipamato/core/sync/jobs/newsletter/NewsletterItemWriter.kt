package ch.difty.scipamato.core.sync.jobs.newsletter

import ch.difty.scipamato.core.sync.PublicNewsletter
import ch.difty.scipamato.core.sync.jobs.ScipamatoItemWriter
import ch.difty.scipamato.publ.db.tables.Newsletter
import org.jooq.DSLContext

/**
 * [ScipamatoItemWriter] implementation to synchronize newsletters from scipamato-core to public.
 *
 *  * Takes care of inserts and updates.
 *  * Every record will be updated at least with the current timestamp in last_synched.
 *  * id columns and audit fields are those of the scipamato-core tables
 */
class NewsletterItemWriter(jooqDslContextPublic: DSLContext) : ScipamatoItemWriter<PublicNewsletter>(jooqDslContextPublic, "newsletter") {
    override fun executeUpdate(i: PublicNewsletter): Int =
        dslContext
            .insertInto(Newsletter.NEWSLETTER)
            .columns(Newsletter.NEWSLETTER.ID, Newsletter.NEWSLETTER.ISSUE, Newsletter.NEWSLETTER.ISSUE_DATE, Newsletter.NEWSLETTER.VERSION, Newsletter.NEWSLETTER.CREATED,
                Newsletter.NEWSLETTER.LAST_MODIFIED, Newsletter.NEWSLETTER.LAST_SYNCHED)
            .values(i.id, i.issue, i.issueDate, i.version, i.created, i.lastModified, i.lastSynched)
            .onConflict(Newsletter.NEWSLETTER.ID)
            .doUpdate()
            .set(Newsletter.NEWSLETTER.ISSUE, i.issue)
            .set(Newsletter.NEWSLETTER.ISSUE_DATE, i.issueDate)
            .set(Newsletter.NEWSLETTER.VERSION, i.version)
            .set(Newsletter.NEWSLETTER.CREATED, i.created)
            .set(Newsletter.NEWSLETTER.LAST_MODIFIED, i.lastModified)
            .set(Newsletter.NEWSLETTER.LAST_SYNCHED, i.lastSynched)
            .where(Newsletter.NEWSLETTER.ID.eq(i.id))
            .execute()
}
