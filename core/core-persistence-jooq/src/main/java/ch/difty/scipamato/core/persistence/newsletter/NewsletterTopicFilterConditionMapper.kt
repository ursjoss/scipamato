package ch.difty.scipamato.core.persistence.newsletter

import ch.difty.scipamato.common.persistence.AbstractFilterConditionMapper
import ch.difty.scipamato.common.persistence.FilterConditionMapper
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicFilter
import org.jooq.Condition

/**
 * Mapper turning the provider [NewsletterTopicFilter] into a jOOQ [Condition].
 */
@FilterConditionMapper
class NewsletterTopicFilterConditionMapper : AbstractFilterConditionMapper<NewsletterTopicFilter>() {

    // currently no fields to be handled
    override fun internalMap(filter: NewsletterTopicFilter): List<Condition> = emptyList()
}
