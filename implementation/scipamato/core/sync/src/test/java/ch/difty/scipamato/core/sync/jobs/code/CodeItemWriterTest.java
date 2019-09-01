package ch.difty.scipamato.core.sync.jobs.code;

import static ch.difty.scipamato.common.TestUtilsKt.assertDegenerateSupplierParameter;

import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterTest;

class CodeItemWriterTest extends AbstractItemWriterTest<PublicCode, CodeItemWriter> {

    @Override
    protected CodeItemWriter newWriter(DSLContext dslContextMock) {
        return new CodeItemWriter(dslContextMock);
    }

    @Test
    void degenerateConstruction_withNullDslContext_throws() {
        assertDegenerateSupplierParameter(() -> new CodeItemWriter(null), "jooqDslContextPublic");
    }

}
