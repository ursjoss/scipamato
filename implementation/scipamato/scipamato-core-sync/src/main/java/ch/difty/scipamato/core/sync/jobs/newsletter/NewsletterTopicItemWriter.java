package ch.difty.scipamato.core.sync.jobs.newsletter;

import static ch.difty.scipamato.publ.db.public_.tables.NewsletterTopic.NEWSLETTER_TOPIC;

import org.jooq.DSLContext;
import org.springframework.batch.item.ItemWriter;

import ch.difty.scipamato.core.sync.jobs.ScipamatoItemWriter;

/**
 * {@link ItemWriter} implementation to synchronize newsletter topics from scipamato-core
 * to public.
 *
 * <ul>
 * <li>Takes care of inserts and updates.</li>
 * <li>Every record will be updated at least with the current timestamp in
 * last_synched.</li>
 * <li>id columns and audit fields are those of the scipamato-core tables</li>
 * </ul>
 *
 * @author u.joss
 */
public class NewsletterTopicItemWriter extends ScipamatoItemWriter<PublicNewsletterTopic> {

    @SuppressWarnings("WeakerAccess")
    public NewsletterTopicItemWriter(final DSLContext jooqDslContextPublic) {
        super(jooqDslContextPublic, "newsletterTopic");
    }

    @Override
    protected int executeUpdate(final PublicNewsletterTopic p) {
        return getDslContext()
            .insertInto(NEWSLETTER_TOPIC)
            .columns(NEWSLETTER_TOPIC.ID, NEWSLETTER_TOPIC.LANG_CODE, NEWSLETTER_TOPIC.TITLE, NEWSLETTER_TOPIC.VERSION,
                NEWSLETTER_TOPIC.CREATED, NEWSLETTER_TOPIC.LAST_MODIFIED, NEWSLETTER_TOPIC.LAST_SYNCHED)
            .values(p.getId(), p.getLangCode(), p.getTitle(), p.getVersion(), p.getCreated(), p.getLastModified(),
                p.getLastSynched())
            .onConflict(NEWSLETTER_TOPIC.ID, NEWSLETTER_TOPIC.LANG_CODE)
            .doUpdate()
            .set(NEWSLETTER_TOPIC.TITLE, p.getTitle())
            .set(NEWSLETTER_TOPIC.VERSION, p.getVersion())
            .set(NEWSLETTER_TOPIC.CREATED, p.getCreated())
            .set(NEWSLETTER_TOPIC.LAST_MODIFIED, p.getLastModified())
            .set(NEWSLETTER_TOPIC.LAST_SYNCHED, p.getLastSynched())
            .where(NEWSLETTER_TOPIC.ID
                .eq(p.getId())
                .and(NEWSLETTER_TOPIC.LANG_CODE.eq(p.getLangCode())))
            .execute();
    }

}
