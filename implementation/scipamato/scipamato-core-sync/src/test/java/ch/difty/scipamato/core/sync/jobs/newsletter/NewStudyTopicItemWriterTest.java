package ch.difty.scipamato.core.sync.jobs.newsletter;

import org.jooq.DSLContext;

import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterTest;

public class NewStudyTopicItemWriterTest extends AbstractItemWriterTest<PublicNewStudyTopic, NewStudyTopicItemWriter> {

    @Override
    protected NewStudyTopicItemWriter newWriter(DSLContext dslContextMock) {
        return new NewStudyTopicItemWriter(dslContextMock);
    }
}