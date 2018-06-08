package ch.difty.scipamato.core.persistence.newsletter;

import java.util.List;

import org.jooq.Condition;

import ch.difty.scipamato.common.persistence.AbstractFilterConditionMapper;
import ch.difty.scipamato.common.persistence.FilterConditionMapper;
import ch.difty.scipamato.core.db.tables.Newsletter;
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
            conditions.add(Newsletter.NEWSLETTER.ISSUE.likeIgnoreCase(likeExpression));
        }
        if (filter.getPublicationStatus() != null) {
            conditions.add(Newsletter.NEWSLETTER.PUBLICATION_STATUS.eq(filter
                .getPublicationStatus()
                .getId()));
        }
    }

}
