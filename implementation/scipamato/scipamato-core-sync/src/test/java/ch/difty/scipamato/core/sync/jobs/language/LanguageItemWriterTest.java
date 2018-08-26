package ch.difty.scipamato.core.sync.jobs.language;

import org.jooq.DSLContext;

import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterTest;

public class LanguageItemWriterTest extends AbstractItemWriterTest<PublicLanguage, LanguageItemWriter> {

    @Override
    protected LanguageItemWriter newWriter(DSLContext dslContextMock) {
        return new LanguageItemWriter(dslContextMock);
    }

}
