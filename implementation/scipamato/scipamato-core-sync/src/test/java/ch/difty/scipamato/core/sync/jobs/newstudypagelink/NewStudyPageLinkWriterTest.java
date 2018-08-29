package ch.difty.scipamato.core.sync.jobs.newstudypagelink;

import org.jooq.DSLContext;

import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterTest;

public class NewStudyPageLinkWriterTest
    extends AbstractItemWriterTest<PublicNewStudyPageLink, NewStudyPageLinkItemWriter> {

    @Override
    protected NewStudyPageLinkItemWriter newWriter(DSLContext dslContextMock) {
        return new NewStudyPageLinkItemWriter(dslContextMock);
    }

}