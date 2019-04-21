package ch.difty.scipamato.core.sync.jobs.keyword;

import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.common.TestUtils;
import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterTest;

public class KeywordItemWriterTest extends AbstractItemWriterTest<PublicKeyword, KeywordItemWriter> {

    @Override
    protected KeywordItemWriter newWriter(DSLContext dslContextMock) {
        return new KeywordItemWriter(dslContextMock);
    }

    @Test
    public void degenerateConstruction_withNullDslContext_throws() {
        TestUtils.assertDegenerateSupplierParameter(() -> new KeywordItemWriter(null), "jooqDslContextPublic");
    }

}
