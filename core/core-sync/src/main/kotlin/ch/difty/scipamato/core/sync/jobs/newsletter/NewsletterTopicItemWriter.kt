package ch.difty.scipamato.core.sync.jobs.newsletter

import ch.difty.scipamato.core.sync.PublicNewsletterTopic
import ch.difty.scipamato.core.sync.jobs.ScipamatoItemWriter
import ch.difty.scipamato.publ.db.tables.NewsletterTopic
import org.jooq.DSLContext

/**
 * [ScipamatoItemWriter] implementation to synchronize newsletter topics from scipamato-core to public.
 *
 *  * Takes care of inserts and updates.
 *  * Every record will be updated at least with the current timestamp in last_synched.
 *  * id columns and audit fields are those of the scipamato-core tables
 */
class NewsletterTopicItemWriter(
    jooqDslContextPublic: DSLContext
) : ScipamatoItemWriter<PublicNewsletterTopic>(jooqDslContextPublic, "newsletterTopic") {
    override fun executeUpdate(i: PublicNewsletterTopic): Int =
        dslContext
            .insertInto(NewsletterTopic.NEWSLETTER_TOPIC)
            .columns(NewsletterTopic.NEWSLETTER_TOPIC.ID, NewsletterTopic.NEWSLETTER_TOPIC.LANG_CODE,
                NewsletterTopic.NEWSLETTER_TOPIC.TITLE, NewsletterTopic.NEWSLETTER_TOPIC.VERSION,
                NewsletterTopic.NEWSLETTER_TOPIC.CREATED, NewsletterTopic.NEWSLETTER_TOPIC.LAST_MODIFIED,
                NewsletterTopic.NEWSLETTER_TOPIC.LAST_SYNCHED)
            .values(i.id, i.langCode, i.title, i.version, i.created, i.lastModified,
                i.lastSynched)
            .onConflict(NewsletterTopic.NEWSLETTER_TOPIC.ID, NewsletterTopic.NEWSLETTER_TOPIC.LANG_CODE)
            .doUpdate()
            .set(NewsletterTopic.NEWSLETTER_TOPIC.TITLE, i.title)
            .set(NewsletterTopic.NEWSLETTER_TOPIC.VERSION, i.version)
            .set(NewsletterTopic.NEWSLETTER_TOPIC.CREATED, i.created)
            .set(NewsletterTopic.NEWSLETTER_TOPIC.LAST_MODIFIED, i.lastModified)
            .set(NewsletterTopic.NEWSLETTER_TOPIC.LAST_SYNCHED, i.lastSynched)
            .where(NewsletterTopic.NEWSLETTER_TOPIC.ID.eq(i.id)
                .and(NewsletterTopic.NEWSLETTER_TOPIC.LANG_CODE.eq(i.langCode)))
            .execute()
}
