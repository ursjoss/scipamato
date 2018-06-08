package ch.difty.scipamato.core.persistence.newsletter;

import java.util.List;

import org.jooq.Condition;

import ch.difty.scipamato.common.persistence.AbstractFilterConditionMapper;
import ch.difty.scipamato.common.persistence.FilterConditionMapper;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicFilter;

/**
 * Mapper turning the provider {@link NewsletterTopicFilter} into a jOOQ
 * {@link Condition}.
 *
 * @author u.joss
 */
@FilterConditionMapper
public class NewsletterTopicFilterConditionMapper extends AbstractFilterConditionMapper<NewsletterTopicFilter> {

    @Override
    public void map(final NewsletterTopicFilter filter, final List<Condition> conditions) {
    }

}
