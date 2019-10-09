package ch.difty.scipamato.core.sync.jobs.newsletter

import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterTest
import org.jooq.DSLContext

internal class NewStudyTopicItemWriterTest : AbstractItemWriterTest<PublicNewStudyTopic, NewStudyTopicItemWriter>() {
    override fun newWriter(dslContextMock: DSLContext?) = NewStudyTopicItemWriter(dslContextMock)
}
