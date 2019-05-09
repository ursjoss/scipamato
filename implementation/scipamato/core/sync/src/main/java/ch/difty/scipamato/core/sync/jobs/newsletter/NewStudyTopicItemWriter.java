package ch.difty.scipamato.core.sync.jobs.newsletter;

import static ch.difty.scipamato.publ.db.public_.tables.NewStudyTopic.NEW_STUDY_TOPIC;

import org.jooq.DSLContext;
import org.springframework.batch.item.ItemWriter;

import ch.difty.scipamato.core.sync.jobs.ScipamatoItemWriter;

/**
 * {@link ItemWriter} implementation to synchronize newStudyTopics from scipamato-core
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
public class NewStudyTopicItemWriter extends ScipamatoItemWriter<PublicNewStudyTopic> {

    @SuppressWarnings("WeakerAccess")
    public NewStudyTopicItemWriter(final DSLContext jooqDslContextPublic) {
        super(jooqDslContextPublic, "newStudyTopic");
    }

    @Override
    protected int executeUpdate(final PublicNewStudyTopic p) {
        return getDslContext()
            .insertInto(NEW_STUDY_TOPIC)
            .columns(NEW_STUDY_TOPIC.NEWSLETTER_ID, NEW_STUDY_TOPIC.NEWSLETTER_TOPIC_ID, NEW_STUDY_TOPIC.SORT,
                NEW_STUDY_TOPIC.VERSION, NEW_STUDY_TOPIC.CREATED, NEW_STUDY_TOPIC.LAST_MODIFIED,
                NEW_STUDY_TOPIC.LAST_SYNCHED)
            .values(p.getNewsletterId(), p.getNewsletterTopicId(), p.getSort(), p.getVersion(), p.getCreated(),
                p.getLastModified(), p.getLastSynched())
            .onConflict(NEW_STUDY_TOPIC.NEWSLETTER_ID, NEW_STUDY_TOPIC.NEWSLETTER_TOPIC_ID)
            .doUpdate()
            .set(NEW_STUDY_TOPIC.SORT, p.getSort())
            .set(NEW_STUDY_TOPIC.VERSION, p.getVersion())
            .set(NEW_STUDY_TOPIC.CREATED, p.getCreated())
            .set(NEW_STUDY_TOPIC.LAST_MODIFIED, p.getLastModified())
            .set(NEW_STUDY_TOPIC.LAST_SYNCHED, p.getLastSynched())
            .where(NEW_STUDY_TOPIC.NEWSLETTER_ID
                .eq(p.getNewsletterId())
                .and(NEW_STUDY_TOPIC.NEWSLETTER_TOPIC_ID.eq(p.getNewsletterTopicId())))
            .execute();
    }

}
