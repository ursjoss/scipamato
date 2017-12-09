package ch.difty.scipamato.core.sync.jobs.code;

import org.jooq.DSLContext;

import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterTest;

public class CodeItemWriterTest extends AbstractItemWriterTest<PublicCode, CodeItemWriter> {

    @Override
    protected CodeItemWriter newWriter(DSLContext dslContextMock) {
        return new CodeItemWriter(dslContextMock);
    }

}
