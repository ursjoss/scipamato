package ch.difty.scipamato.public_.persistence.codeclass;

import static ch.difty.scipamato.common.TestUtils.*;
import static org.mockito.Mockito.*;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JooqCodeClassRepoTest {

    private CodeClassRepository repo;

    @Mock
    private DSLContext dslMock;

    @Before
    public void setUp() {
        repo = new JooqCodeClassRepo(dslMock);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(dslMock);
    }

    @Test
    public void finding_withNullLanguageCode_throws() {
        assertDegenerateSupplierParameter(() -> repo.find(null), "languageCode");
    }
}
