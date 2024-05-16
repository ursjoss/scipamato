package ch.difty.scipamato.core.persistence.newsletter;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.common.entity.newsletter.PublicationStatus;
import ch.difty.scipamato.core.db.tables.records.NewsletterRecord;
import ch.difty.scipamato.core.entity.newsletter.Newsletter;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;
import ch.difty.scipamato.core.persistence.AuditFields;
import ch.difty.scipamato.core.persistence.EntityRecordMapper;

/**
 * Mapper mapping {@link NewsletterRecord}s into entity {@link Newsletter}.
 * <br/><br/>
 * <b>Note:</b> the mapper leaves the {@link NewsletterTopic}s empty.
 *
 * @author u.joss
 */
@Component
public class NewsletterRecordMapper extends EntityRecordMapper<NewsletterRecord, Newsletter> {

    @NotNull
    @Override
    protected Newsletter makeEntity() {
        return new Newsletter();
    }

    @NotNull
    @Override
    protected AuditFields getAuditFieldsOf(@NotNull final NewsletterRecord r) {
        return new AuditFields(r.getCreated(), r.getCreatedBy(), r.getLastModified(), r.getLastModifiedBy(), r.getVersion());
    }

    @Override
    protected void mapFields(@NotNull final NewsletterRecord from, @NotNull final Newsletter to) {
        to.setId(from.getId());
        to.setIssue(from.getIssue());
        to.setIssueDate(from.getIssueDate() != null ?
            from
                .getIssueDate()
                .toLocalDate() :
            null);
        to.setPublicationStatus(PublicationStatus.Companion.byId(from.getPublicationStatus()));
    }
}
