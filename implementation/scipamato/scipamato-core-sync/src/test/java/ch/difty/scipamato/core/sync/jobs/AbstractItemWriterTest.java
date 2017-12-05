package ch.difty.scipamato.core.sync.jobs;

import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.jooq.DSLContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.batch.item.ItemWriter;

import ch.difty.scipamato.TestUtils;
import ch.difty.scipamato.core.sync.jobs.code.CodeItemWriter;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractItemWriterTest<T, W extends ItemWriter<T>> {

    private W writer;

    @Mock
    private DSLContext dslContextMock;

    @Before
    public final void setUp() {
        writer = newWriter(dslContextMock);
    }

    protected abstract W newWriter(DSLContext dslContextMock);

    @Test
    public void degenerateConstruction_withNullDslContext_throws() {
        TestUtils.assertDegenerateSupplierParameter(() -> new CodeItemWriter(null), "jooqDslContextPublic");
    }

    @Test
    public void writingEmptyList_doesNotInteractWithJooq() throws Exception {
        writer.write(new ArrayList<T>());
        verifyNoMoreInteractions(dslContextMock);
    }

}
