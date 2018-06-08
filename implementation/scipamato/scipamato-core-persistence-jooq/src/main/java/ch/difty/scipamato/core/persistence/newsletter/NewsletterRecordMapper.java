package ch.difty.scipamato.core.persistence.newsletter;

import org.springframework.stereotype.Component;

import ch.difty.scipamato.core.db.tables.records.NewsletterRecord;
import ch.difty.scipamato.core.entity.newsletter.Newsletter;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;
import ch.difty.scipamato.core.entity.newsletter.PublicationStatus;
import ch.difty.scipamato.core.persistence.AuditFields;
import ch.difty.scipamato.core.persistence.EntityRecordMapper;

/**
 * Mapper mapping {@link NewsletterRecord}s into entity {@link Newsletter}.
 *
 * <b>Note:</b> the mapper leaves the {@link NewsletterTopic}s empty.
 *
 * @author u.joss
 */
@Component
public class NewsletterRecordMapper extends EntityRecordMapper<NewsletterRecord, Newsletter> {

    @Override
    protected Newsletter makeEntity() {
        return new Newsletter();
    }

    @Override
    protected AuditFields getAuditFieldsOf(NewsletterRecord r) {
        return new AuditFields(r.getCreated(), r.getCreatedBy(), r.getLastModified(), r.getLastModifiedBy(),
            r.getVersion());
    }

    @Override
    protected void mapFields(NewsletterRecord from, Newsletter to) {
        to.setId(from.getId());
        to.setIssue(from.getIssue());
        to.setIssueDate(from.getIssueDate() != null ?
            from
                .getIssueDate()
                .toLocalDate() :
            null);
        to.setPublicationStatus(PublicationStatus.byId(from.getPublicationStatus()));
    }

}
