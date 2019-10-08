package ch.difty.scipamato.core.persistence.newsletter;

import static ch.difty.scipamato.core.db.tables.Newsletter.NEWSLETTER;

import java.sql.Date;
import java.sql.Timestamp;

import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateSetMoreStep;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.UtilsKt;
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
        AssertAs.INSTANCE.notNull(step, "step");
        AssertAs.INSTANCE.notNull(e, "entity");
        final Timestamp created = e.getCreated() == null ? null : UtilsKt.toTimestamp(e.getCreated());
        final Timestamp lastMod = e.getLastModified() == null ? null : UtilsKt.toTimestamp(e.getLastModified());
        return step
            .set(NEWSLETTER.ISSUE, e.getIssue())
            .set(NEWSLETTER.ISSUE_DATE, Date.valueOf(e.getIssueDate()))
            .set(NEWSLETTER.PUBLICATION_STATUS, e
                .getPublicationStatus()
                .getId())
            .set(NEWSLETTER.CREATED, created)
            .set(NEWSLETTER.CREATED_BY, e.getCreatedBy())
            .set(NEWSLETTER.LAST_MODIFIED, lastMod)
            .set(NEWSLETTER.LAST_MODIFIED_BY, e.getLastModifiedBy())
            .set(NEWSLETTER.VERSION, e.getVersion() + 1);
    }

}
