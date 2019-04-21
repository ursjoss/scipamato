package ch.difty.scipamato.core.sync.jobs.code;

import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.common.TestUtils;
import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterTest;

public class CodeItemWriterTest extends AbstractItemWriterTest<PublicCode, CodeItemWriter> {

    @Override
    protected CodeItemWriter newWriter(DSLContext dslContextMock) {
        return new CodeItemWriter(dslContextMock);
    }

    @Test
    public void degenerateConstruction_withNullDslContext_throws() {
        TestUtils.assertDegenerateSupplierParameter(() -> new CodeItemWriter(null), "jooqDslContextPublic");
    }

}
