package ch.difty.scipamato.core.sync.jobs.newsletter

import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterTest
import org.jooq.DSLContext

internal class NewsletterItemWriterTest : AbstractItemWriterTest<PublicNewsletter, NewsletterItemWriter>() {
    override fun newWriter(dslContextMock: DSLContext) = NewsletterItemWriter(dslContextMock)
}
