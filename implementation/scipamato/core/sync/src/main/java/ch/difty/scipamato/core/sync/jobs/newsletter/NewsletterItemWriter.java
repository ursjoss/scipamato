package ch.difty.scipamato.core.sync.jobs.newsletter;

import static ch.difty.scipamato.publ.db.public_.tables.Newsletter.NEWSLETTER;

import org.jooq.DSLContext;
import org.springframework.batch.item.ItemWriter;

import ch.difty.scipamato.core.sync.jobs.ScipamatoItemWriter;

/**
 * {@link ItemWriter} implementation to synchronize newsletters from scipamato-core
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
public class NewsletterItemWriter extends ScipamatoItemWriter<PublicNewsletter> {

    @SuppressWarnings("WeakerAccess")
    public NewsletterItemWriter(final DSLContext jooqDslContextPublic) {
        super(jooqDslContextPublic, "newsletter");
    }

    @Override
    protected int executeUpdate(final PublicNewsletter p) {
        return getDslContext()
            .insertInto(NEWSLETTER)
            .columns(NEWSLETTER.ID, NEWSLETTER.ISSUE, NEWSLETTER.ISSUE_DATE, NEWSLETTER.VERSION, NEWSLETTER.CREATED,
                NEWSLETTER.LAST_MODIFIED, NEWSLETTER.LAST_SYNCHED)
            .values(p.getId(), p.getIssue(), p.getIssueDate(), p.getVersion(), p.getCreated(), p.getLastModified(),
                p.getLastSynched())
            .onConflict(NEWSLETTER.ID)
            .doUpdate()
            .set(NEWSLETTER.ISSUE, p.getIssue())
            .set(NEWSLETTER.ISSUE_DATE, p.getIssueDate())
            .set(NEWSLETTER.VERSION, p.getVersion())
            .set(NEWSLETTER.CREATED, p.getCreated())
            .set(NEWSLETTER.LAST_MODIFIED, p.getLastModified())
            .set(NEWSLETTER.LAST_SYNCHED, p.getLastSynched())
            .where(NEWSLETTER.ID.eq(p.getId()))
            .execute();
    }

}
