package ch.difty.scipamato.core.persistence.newsletter

import ch.difty.scipamato.common.persistence.FilterConditionMapperTest
import ch.difty.scipamato.core.db.tables.NewsletterTopic
import ch.difty.scipamato.core.db.tables.NewsletterTopic.NEWSLETTER_TOPIC
import ch.difty.scipamato.core.db.tables.records.NewsletterTopicRecord
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicFilter

internal class NewsletterTopicFilterConditionMapperTest : FilterConditionMapperTest<NewsletterTopicRecord, NewsletterTopic, NewsletterTopicFilter>() {
    override val mapper = NewsletterTopicFilterConditionMapper()
    override val filter = NewsletterTopicFilter()
    override val table: NewsletterTopic = NEWSLETTER_TOPIC
}