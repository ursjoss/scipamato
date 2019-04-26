package ch.difty.scipamato.core.sync.jobs.newsletter;

import org.jooq.DSLContext;

import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterTest;

class NewStudyItemWriterTest extends AbstractItemWriterTest<PublicNewStudy, NewStudyItemWriter> {

    @Override
    protected NewStudyItemWriter newWriter(DSLContext dslContextMock) {
        return new NewStudyItemWriter(dslContextMock);
    }
}