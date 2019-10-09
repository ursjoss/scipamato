package ch.difty.scipamato.core.sync.jobs.newsletter

import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterTest
import org.jooq.DSLContext

internal class NewsletterTopicItemWriterTest :
    AbstractItemWriterTest<PublicNewsletterTopic, NewsletterTopicItemWriter>() {
    override fun newWriter(dslContextMock: DSLContext?) = NewsletterTopicItemWriter(dslContextMock)
}
