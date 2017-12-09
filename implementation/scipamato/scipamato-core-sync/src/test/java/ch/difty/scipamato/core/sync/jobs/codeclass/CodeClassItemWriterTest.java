package ch.difty.scipamato.core.sync.jobs.codeclass;

import org.jooq.DSLContext;

import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterTest;

public class CodeClassItemWriterTest extends AbstractItemWriterTest<PublicCodeClass, CodeClassItemWriter> {

    @Override
    protected CodeClassItemWriter newWriter(DSLContext dslContextMock) {
        return new CodeClassItemWriter(dslContextMock);
    }

}
