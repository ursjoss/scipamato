package ch.difty.scipamato.core.sync.jobs.newsletter;

import org.jooq.DSLContext;

import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterTest;

public class NewsletterTopicItemWriterTest extends AbstractItemWriterTest<PublicNewsletterTopic, NewsletterTopicItemWriter> {

    @Override
    protected NewsletterTopicItemWriter newWriter(DSLContext dslContextMock) {
        return new NewsletterTopicItemWriter(dslContextMock);
    }
}