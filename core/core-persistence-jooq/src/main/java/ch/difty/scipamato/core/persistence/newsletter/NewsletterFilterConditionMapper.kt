package ch.difty.scipamato.core.persistence.newsletter

import ch.difty.scipamato.common.persistence.AbstractFilterConditionMapper
import ch.difty.scipamato.common.persistence.FilterConditionMapper
import ch.difty.scipamato.core.db.tables.Newsletter
import ch.difty.scipamato.core.db.tables.PaperNewsletter
import ch.difty.scipamato.core.entity.newsletter.NewsletterFilter
import org.jooq.Condition
import org.jooq.impl.DSL

/**
 * Mapper turning the provider [NewsletterFilter] into a jOOQ [Condition].
 */
@FilterConditionMapper
class NewsletterFilterConditionMapper : AbstractFilterConditionMapper<NewsletterFilter>() {

    override fun internalMap(filter: NewsletterFilter): List<Condition> {
        val conditions = mutableListOf<Condition>()
        filter.issueMask?.let { mask ->
            conditions.add(Newsletter.NEWSLETTER.ISSUE.likeIgnoreCase("%$mask%"))
        }
        filter.publicationStatus?.id?.let { statusId ->
            conditions.add(Newsletter.NEWSLETTER.PUBLICATION_STATUS.eq(statusId))
        }
        filter.newsletterTopic?.id?.let { topicId ->
            val newsletterIdsWithTopic = DSL
                .select(PaperNewsletter.PAPER_NEWSLETTER.NEWSLETTER_ID)
                .from(PaperNewsletter.PAPER_NEWSLETTER)
                .where(
                    PaperNewsletter.PAPER_NEWSLETTER.NEWSLETTER_TOPIC_ID.eq(DSL.`val`(topicId))
                )
            conditions.add(Newsletter.NEWSLETTER.ID.`in`(newsletterIdsWithTopic))
        }
        return conditions
    }
}
