package ch.difty.scipamato.core.sync.jobs.keyword;

import static ch.difty.scipamato.common.TestUtilsKt.assertDegenerateSupplierParameter;

import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterTest;

class KeywordItemWriterTest extends AbstractItemWriterTest<PublicKeyword, KeywordItemWriter> {

    @Override
    protected KeywordItemWriter newWriter(DSLContext dslContextMock) {
        return new KeywordItemWriter(dslContextMock);
    }

    @Test
    void degenerateConstruction_withNullDslContext_throws() {
        assertDegenerateSupplierParameter(() -> new KeywordItemWriter(null), "jooqDslContextPublic");
    }

}
