package ch.difty.scipamato.core.persistence.newsletter;

import static ch.difty.scipamato.core.db.tables.Newsletter.NEWSLETTER;
import static ch.difty.scipamato.core.db.tables.NewsletterNewsletterTopic.NEWSLETTER_NEWSLETTER_TOPIC;

import java.util.List;

import org.jooq.Condition;
import org.jooq.impl.DSL;

import ch.difty.scipamato.common.persistence.AbstractFilterConditionMapper;
import ch.difty.scipamato.common.persistence.FilterConditionMapper;
import ch.difty.scipamato.core.entity.newsletter.NewsletterFilter;

/**
 * Mapper turning the provider {@link NewsletterFilter} into a jOOQ
 * {@link Condition}.
 *
 * @author u.joss
 */
@FilterConditionMapper
public class NewsletterFilterConditionMapper extends AbstractFilterConditionMapper<NewsletterFilter> {

    @Override
    public void map(final NewsletterFilter filter, final List<Condition> conditions) {
        if (filter.getIssueMask() != null) {
            final String likeExpression = "%" + filter.getIssueMask() + "%";
            conditions.add(NEWSLETTER.ISSUE.likeIgnoreCase(likeExpression));
        }
        if (filter.getPublicationStatus() != null) {
            conditions.add(NEWSLETTER.PUBLICATION_STATUS.eq(filter
                .getPublicationStatus()
                .getId()));
        }
        if (filter.getNewsletterTopic() != null && filter
                                                       .getNewsletterTopic()
                                                       .getId() != null) {
            conditions.add(NEWSLETTER.ID.in(DSL
                .select(NEWSLETTER_NEWSLETTER_TOPIC.NEWSLETTER_ID)
                .from(NEWSLETTER_NEWSLETTER_TOPIC)
                .where(NEWSLETTER_NEWSLETTER_TOPIC.NEWSLETTER_ID.eq(NEWSLETTER.ID))
                .and(NEWSLETTER_NEWSLETTER_TOPIC.NEWSLETTER_TOPIC_ID.eq(DSL.val(filter
                    .getNewsletterTopic()
                    .getId())))));
        }
    }

}
