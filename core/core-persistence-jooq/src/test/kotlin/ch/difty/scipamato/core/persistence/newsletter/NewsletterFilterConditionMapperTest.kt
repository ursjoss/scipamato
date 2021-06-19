package ch.difty.scipamato.core.persistence.newsletter

import ch.difty.scipamato.common.entity.newsletter.PublicationStatus
import ch.difty.scipamato.common.persistence.FilterConditionMapperTest
import ch.difty.scipamato.core.db.tables.Newsletter
import ch.difty.scipamato.core.db.tables.Newsletter.NEWSLETTER
import ch.difty.scipamato.core.db.tables.records.NewsletterRecord
import ch.difty.scipamato.core.entity.newsletter.NewsletterFilter
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

internal class NewsletterFilterConditionMapperTest :
    FilterConditionMapperTest<NewsletterRecord, Newsletter, NewsletterFilter>() {

    override val mapper = NewsletterFilterConditionMapper()
    override val filter = NewsletterFilter()

    override val table: Newsletter
        get() = NEWSLETTER

    @Test
    fun creatingWhereCondition_withIssueMask_searchesFirstIssue() {
        val pattern = "im"
        filter.issueMask = pattern
        mapper.map(filter).toString() shouldBeEqualTo makeWhereClause(pattern, "issue")
    }

    @Test
    fun creatingWhereCondition_withPublicationStatus() {
        filter.publicationStatus = PublicationStatus.CANCELLED
        mapper.map(filter).toString() shouldBeEqualTo
            """"public"."newsletter"."publication_status" = -1"""
    }

    @Test
    fun creatingWhereCondition_withNewsletterTopic() {
        filter.newsletterTopic = NewsletterTopic(5, "foo")
        mapper.map(filter).toString() shouldBeEqualTo
            """"public"."newsletter"."id" in (
            |  select "public"."paper_newsletter"."newsletter_id"
            |  from "public"."paper_newsletter"
            |  where "public"."paper_newsletter"."newsletter_topic_id" = 5
            |)""".trimMargin()
    }

    @Test
    fun creatingWhereCondition_withNewsletterTopicWithNullId() {
        filter.newsletterTopic = NewsletterTopic(null, "foo")
        mapper.map(filter).toString() shouldBeEqualTo "true"
    }
}
