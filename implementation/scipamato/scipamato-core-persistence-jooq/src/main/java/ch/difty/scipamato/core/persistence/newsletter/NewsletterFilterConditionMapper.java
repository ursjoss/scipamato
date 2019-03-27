package ch.difty.scipamato.core.persistence.newsletter;

import static ch.difty.scipamato.core.db.tables.Newsletter.NEWSLETTER;
import static ch.difty.scipamato.core.db.tables.PaperNewsletter.PAPER_NEWSLETTER;

import java.util.List;

import org.jooq.Condition;
import org.jooq.impl.DSL;

import ch.difty.scipamato.common.entity.newsletter.PublicationStatus;
import ch.difty.scipamato.common.persistence.AbstractFilterConditionMapper;
import ch.difty.scipamato.common.persistence.FilterConditionMapper;
import ch.difty.scipamato.core.entity.newsletter.NewsletterFilter;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;

/**
 * Mapper turning the provider {@link NewsletterFilter} into a jOOQ {@link Condition}.
 *
 * @author u.joss
 */
@FilterConditionMapper
public class NewsletterFilterConditionMapper extends AbstractFilterConditionMapper<NewsletterFilter> {

    @Override
    public void map(final NewsletterFilter filter, final List<Condition> conditions) {
        final String issueMask = filter.getIssueMask();
        if (issueMask != null) {
            final String likeExpression = "%" + issueMask + "%";
            conditions.add(NEWSLETTER.ISSUE.likeIgnoreCase(likeExpression));
        }

        final PublicationStatus publicationStatus = filter.getPublicationStatus();
        if (publicationStatus != null) {
            conditions.add(NEWSLETTER.PUBLICATION_STATUS.eq(publicationStatus.getId()));
        }

        final NewsletterTopic newsletterTopic = filter.getNewsletterTopic();
        if (newsletterTopic != null && newsletterTopic.getId() != null) {
            conditions.add(NEWSLETTER.ID.in(DSL
                .select(PAPER_NEWSLETTER.NEWSLETTER_ID)
                .from(PAPER_NEWSLETTER)
                .where(PAPER_NEWSLETTER.NEWSLETTER_TOPIC_ID.eq(DSL.val(newsletterTopic.getId())))));
        }
    }

}
