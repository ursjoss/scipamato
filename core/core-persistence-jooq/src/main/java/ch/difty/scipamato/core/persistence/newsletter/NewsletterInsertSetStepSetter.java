package ch.difty.scipamato.core.persistence.newsletter;

import static ch.difty.scipamato.core.db.tables.Newsletter.NEWSLETTER;

import java.sql.Date;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.InsertSetMoreStep;
import org.jooq.InsertSetStep;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.core.db.tables.records.NewsletterRecord;
import ch.difty.scipamato.core.entity.newsletter.Newsletter;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;
import ch.difty.scipamato.core.persistence.InsertSetStepSetter;

/**
 * The insert step setter used for inserting new {@link Newsletter}s.
 * <br/><br/>
 * <b>Note:</b> the {@link NewsletterTopic}s are not inserted here.
 *
 * @author u.joss
 */
@Component
public class NewsletterInsertSetStepSetter implements InsertSetStepSetter<NewsletterRecord, Newsletter> {

    @NotNull
    @Override
    public InsertSetMoreStep<NewsletterRecord> setNonKeyFieldsFor(@NotNull final InsertSetStep<NewsletterRecord> step, @NotNull final Newsletter e) {
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
    public void considerSettingKeyOf(@NotNull final InsertSetMoreStep<NewsletterRecord> step, @NotNull final Newsletter entity) {
        final Integer id = entity.getId();
        if (id != null) {
            //noinspection ResultOfMethodCallIgnored
            step.set(NEWSLETTER.ID, id);
        }
    }

    @Override
    public void resetIdToEntity(@NotNull final Newsletter entity, @Nullable final NewsletterRecord saved) {
        if (saved != null)
            entity.setId(saved.getId());
    }
}
