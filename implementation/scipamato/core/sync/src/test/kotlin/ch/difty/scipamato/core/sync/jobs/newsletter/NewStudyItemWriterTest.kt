package ch.difty.scipamato.core.sync.jobs.newsletter

import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterTest
import org.jooq.DSLContext

internal class NewStudyItemWriterTest : AbstractItemWriterTest<PublicNewStudy, NewStudyItemWriter>() {
    override fun newWriter(dslContextMock: DSLContext?) = NewStudyItemWriter(dslContextMock)
}