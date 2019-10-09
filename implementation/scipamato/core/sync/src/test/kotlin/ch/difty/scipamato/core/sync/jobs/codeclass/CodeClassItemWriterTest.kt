package ch.difty.scipamato.core.sync.jobs.codeclass

import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterTest
import org.jooq.DSLContext

internal class CodeClassItemWriterTest : AbstractItemWriterTest<PublicCodeClass, CodeClassItemWriter>() {
    override fun newWriter(dslContextMock: DSLContext?) = CodeClassItemWriter(dslContextMock)
}
