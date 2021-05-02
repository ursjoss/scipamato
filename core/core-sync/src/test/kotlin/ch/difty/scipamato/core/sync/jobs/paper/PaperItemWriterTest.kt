package ch.difty.scipamato.core.sync.jobs.paper

import ch.difty.scipamato.core.sync.PublicPaper
import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterTest
import org.jooq.DSLContext

internal class PaperItemWriterTest : AbstractItemWriterTest<PublicPaper, PaperItemWriter>() {
    override fun newWriter(dslContextMock: DSLContext) = PaperItemWriter(dslContextMock)
}
