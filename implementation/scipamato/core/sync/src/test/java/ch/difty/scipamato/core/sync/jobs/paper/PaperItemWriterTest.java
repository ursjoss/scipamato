package ch.difty.scipamato.core.sync.jobs.paper;

import org.jooq.DSLContext;

import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterTest;

class PaperItemWriterTest extends AbstractItemWriterTest<PublicPaper, PaperItemWriter> {

    @Override
    protected PaperItemWriter newWriter(DSLContext dslContextMock) {
        return new PaperItemWriter(dslContextMock);
    }

}
