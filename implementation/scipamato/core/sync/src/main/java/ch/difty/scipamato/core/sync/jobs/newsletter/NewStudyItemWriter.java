package ch.difty.scipamato.core.sync.jobs.newsletter;

import static ch.difty.scipamato.publ.db.tables.NewStudy.NEW_STUDY;

import org.jooq.DSLContext;
import org.springframework.batch.item.ItemWriter;

import ch.difty.scipamato.core.sync.jobs.ScipamatoItemWriter;

/**
 * {@link ItemWriter} implementation to synchronize newStudies from scipamato-core
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
public class NewStudyItemWriter extends ScipamatoItemWriter<PublicNewStudy> {

    public NewStudyItemWriter(final DSLContext jooqDslContextPublic) {
        super(jooqDslContextPublic, "newStudy");
    }

    @Override
    protected int executeUpdate(final PublicNewStudy p) {
        return getDslContext()
            .insertInto(NEW_STUDY)
            .columns(NEW_STUDY.NEWSLETTER_ID, NEW_STUDY.NEWSLETTER_TOPIC_ID, NEW_STUDY.SORT, NEW_STUDY.PAPER_NUMBER,
                NEW_STUDY.YEAR, NEW_STUDY.AUTHORS, NEW_STUDY.HEADLINE, NEW_STUDY.DESCRIPTION, NEW_STUDY.VERSION,
                NEW_STUDY.CREATED, NEW_STUDY.LAST_MODIFIED, NEW_STUDY.LAST_SYNCHED)
            .values(p.getNewsletterId(), p.getNewsletterTopicId(), p.getSort(), p.getPaperNumber(), p.getYear(),
                p.getAuthors(), p.getHeadline(), p.getDescription(), p.getVersion(), p.getCreated(),
                p.getLastModified(), p.getLastSynched())
            .onConflict(NEW_STUDY.NEWSLETTER_ID, NEW_STUDY.PAPER_NUMBER)
            .doUpdate()
            .set(NEW_STUDY.NEWSLETTER_TOPIC_ID, p.getNewsletterTopicId())
            .set(NEW_STUDY.SORT, p.getSort())
            .set(NEW_STUDY.PAPER_NUMBER, p.getPaperNumber())
            .set(NEW_STUDY.YEAR, p.getYear())
            .set(NEW_STUDY.AUTHORS, p.getAuthors())
            .set(NEW_STUDY.HEADLINE, p.getHeadline())
            .set(NEW_STUDY.DESCRIPTION, p.getDescription())
            .set(NEW_STUDY.VERSION, p.getVersion())
            .set(NEW_STUDY.CREATED, p.getCreated())
            .set(NEW_STUDY.LAST_MODIFIED, p.getLastModified())
            .set(NEW_STUDY.LAST_SYNCHED, p.getLastSynched())
            .where(NEW_STUDY.NEWSLETTER_ID
                .eq(p.getNewsletterId())
                .and(NEW_STUDY.PAPER_NUMBER.eq(p.getPaperNumber())))
            .execute();
    }

}
