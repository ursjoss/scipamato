package ch.difty.scipamato.core.persistence.newsletter;

import static ch.difty.scipamato.core.db.tables.Newsletter.NEWSLETTER;

import java.sql.Date;

import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateSetMoreStep;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.DateUtils;
import ch.difty.scipamato.core.db.tables.records.NewsletterRecord;
import ch.difty.scipamato.core.entity.newsletter.Newsletter;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;
import ch.difty.scipamato.core.persistence.UpdateSetStepSetter;

/**
 * The update step setter used for updating {@link Newsletter}s.
 *
 *
 * <b>Note:</b> the {@link NewsletterTopic}s are not updated here.
 *
 * @author u.joss
 */
@Component
public class NewsletterUpdateSetStepSetter implements UpdateSetStepSetter<NewsletterRecord, Newsletter> {

    @Override
    public UpdateSetMoreStep<NewsletterRecord> setFieldsFor(final UpdateSetFirstStep<NewsletterRecord> step,
        final Newsletter e) {
        AssertAs.notNull(step, "step");
        AssertAs.notNull(e, "entity");
        return step
            .set(NEWSLETTER.ISSUE, e.getIssue())
            .set(NEWSLETTER.ISSUE_DATE, Date.valueOf(e.getIssueDate()))
            .set(NEWSLETTER.PUBLICATION_STATUS, e
                .getPublicationStatus()
                .getId())

            .set(NEWSLETTER.CREATED, DateUtils.tsOf(e.getCreated()))
            .set(NEWSLETTER.CREATED_BY, e.getCreatedBy())
            .set(NEWSLETTER.LAST_MODIFIED, DateUtils.tsOf(e.getLastModified()))
            .set(NEWSLETTER.LAST_MODIFIED_BY, e.getLastModifiedBy())
            .set(NEWSLETTER.VERSION, e.getVersion() + 1);
    }

}
