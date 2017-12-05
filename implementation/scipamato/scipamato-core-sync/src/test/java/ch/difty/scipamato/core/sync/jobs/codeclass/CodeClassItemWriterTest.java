package ch.difty.scipamato.core.sync.jobs.codeclass;

import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.jooq.DSLContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.difty.scipamato.TestUtils;

@RunWith(MockitoJUnitRunner.class)
public class CodeClassItemWriterTest {

    private CodeClassItemWriter writer;

    @Mock
    private DSLContext dslContextMock;

    @Before
    public void setUp() {
        writer = new CodeClassItemWriter(dslContextMock);
    }

    @Test
    public void degenerateConstruction() {
        TestUtils.assertDegenerateSupplierParameter(() -> new CodeClassItemWriter(null), "jooqDslContextPublic");
    }

    @Test
    public void writingEmptyList_doesNotInteractWithJooq() throws Exception {
        writer.write(new ArrayList<PublicCodeClass>());
        verifyNoMoreInteractions(dslContextMock);
    }

}
