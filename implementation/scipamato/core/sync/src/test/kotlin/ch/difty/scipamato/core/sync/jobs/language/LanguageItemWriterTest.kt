package ch.difty.scipamato.core.sync.jobs.language

import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterTest
import org.jooq.DSLContext

internal class LanguageItemWriterTest : AbstractItemWriterTest<PublicLanguage, LanguageItemWriter>() {
    override fun newWriter(dslContextMock: DSLContext?) = LanguageItemWriter(dslContextMock)
}
