package ch.difty.scipamato.core.persistence.newsletter;

import static ch.difty.scipamato.core.db.tables.Newsletter.NEWSLETTER;

import java.sql.Date;

import org.jooq.InsertSetMoreStep;
import org.jooq.InsertSetStep;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.core.db.tables.records.NewsletterRecord;
import ch.difty.scipamato.core.entity.newsletter.Newsletter;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;
import ch.difty.scipamato.core.persistence.InsertSetStepSetter;

/**
 * The insert step setter used for inserting new {@link Newsletter}s.
 *
 *
 * <b>Note:</b> the {@link NewsletterTopic}s are not inserted here.
 *
 * @author u.joss
 */
@Component
public class NewsletterInsertSetStepSetter implements InsertSetStepSetter<NewsletterRecord, Newsletter> {

    @Override
    public InsertSetMoreStep<NewsletterRecord> setNonKeyFieldsFor(final InsertSetStep<NewsletterRecord> step,
        final Newsletter e) {
        AssertAs.notNull(step, "step");
        AssertAs.notNull(e, "entity");

        return step
            .set(NEWSLETTER.ISSUE, e.getIssue())
            .set(NEWSLETTER.ISSUE_DATE, Date.valueOf(e.getIssueDate()))
            .set(NEWSLETTER.PUBLICATION_STATUS, e
                .getPublicationStatus()
                .getId())

            .set(NEWSLETTER.CREATED_BY, e.getCreatedBy())
            .set(NEWSLETTER.LAST_MODIFIED_BY, e.getLastModifiedBy());
    }

    @Override
    public void considerSettingKeyOf(final InsertSetMoreStep<NewsletterRecord> step, final Newsletter entity) {
        AssertAs.notNull(step, "step");
        AssertAs.notNull(entity, "entity");
        final Integer id = entity.getId();
        if (id != null)
            step.set(NEWSLETTER.ID, id);
    }

    @Override
    public void resetIdToEntity(Newsletter entity, NewsletterRecord saved) {
        if (saved != null)
            entity.setId(saved.getId());
    }

}
