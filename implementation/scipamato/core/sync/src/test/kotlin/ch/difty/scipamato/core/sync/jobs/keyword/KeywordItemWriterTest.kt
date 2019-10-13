package ch.difty.scipamato.core.sync.jobs.keyword

import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterTest
import org.jooq.DSLContext

internal class KeywordItemWriterTest : AbstractItemWriterTest<PublicKeyword, KeywordItemWriter>() {
    override fun newWriter(dslContextMock: DSLContext) = KeywordItemWriter(dslContextMock)
}
