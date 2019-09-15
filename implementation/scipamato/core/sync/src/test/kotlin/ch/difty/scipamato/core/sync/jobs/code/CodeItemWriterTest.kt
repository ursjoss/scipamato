package ch.difty.scipamato.core.sync.jobs.code

import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterTest
import org.jooq.DSLContext

internal class CodeItemWriterTest : AbstractItemWriterTest<PublicCode, CodeItemWriter>() {

    override fun newWriter(dslContextMock: DSLContext?) = CodeItemWriter(dslContextMock)

}
