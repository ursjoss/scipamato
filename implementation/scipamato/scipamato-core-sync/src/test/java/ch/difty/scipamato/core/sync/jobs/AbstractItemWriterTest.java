package ch.difty.scipamato.core.sync.jobs;

import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.ArrayList;

import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.item.ItemWriter;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractItemWriterTest<T, W extends ItemWriter<T>> {

    private W writer;

    @Mock
    private DSLContext dslContextMock;

    @BeforeEach
    public final void setUp() {
        writer = newWriter(dslContextMock);
    }

    protected abstract W newWriter(DSLContext dslContextMock);

    @Test
    public void writingEmptyList_doesNotInteractWithJooq() throws Exception {
        writer.write(new ArrayList<>());
        verifyNoMoreInteractions(dslContextMock);
    }

}
