package ch.difty.scipamato.publ.persistence.codeclass;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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
