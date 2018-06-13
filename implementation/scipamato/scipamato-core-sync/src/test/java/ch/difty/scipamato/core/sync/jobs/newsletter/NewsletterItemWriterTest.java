package ch.difty.scipamato.core.sync.jobs.newsletter;

import org.jooq.DSLContext;

import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterTest;

public class NewsletterItemWriterTest extends AbstractItemWriterTest<PublicNewsletter, NewsletterItemWriter> {

    @Override
    protected NewsletterItemWriter newWriter(DSLContext dslContextMock) {
        return new NewsletterItemWriter(dslContextMock);
    }
}