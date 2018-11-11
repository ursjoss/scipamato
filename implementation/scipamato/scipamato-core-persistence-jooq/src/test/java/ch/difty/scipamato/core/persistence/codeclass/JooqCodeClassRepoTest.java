package ch.difty.scipamato.core.persistence.codeclass;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;

import org.jooq.DSLContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ch.difty.scipamato.common.DateTimeService;

@RunWith(MockitoJUnitRunner.class)
public class JooqCodeClassRepoTest {

    @Mock
    private DSLContext dslContextMock;

    @Mock
    private DateTimeService dtsMock;

    private JooqCodeClassRepo repo;

    @Before
    public void setUp() {
        repo = new JooqCodeClassRepo(dslContextMock, dtsMock);
    }

    @Test
    public void degenerateConstruction_withNullDsl_throws() {
        assertDegenerateSupplierParameter(() -> new JooqCodeClassRepo(null, dtsMock), "dsl");
    }

    @Test
    public void degenerateConstruction_withNullDateTimeService_throws() {
        assertDegenerateSupplierParameter(() -> new JooqCodeClassRepo(dslContextMock, null), "dateTimeService");
    }

    @Test
    public void finding_withNullLanguageId_throws() {
        assertDegenerateSupplierParameter(() -> repo.find(null), "languageCode");
    }

    @Test
    public void insertingOrUpdating_withNullCodeClassDefinition_throws() {
        assertDegenerateSupplierParameter(() -> repo.saveOrUpdate(null), "codeClassDefinition");
    }

    @Test
    public void findingCodeClassDefinition_withNullId_throws() {
        assertDegenerateSupplierParameter(() -> repo.findCodeClassDefinition(null), "id");
    }

    @Test
    public void deleting_withNullId_throws() {
        assertDegenerateSupplierParameter(() -> repo.delete(null, 1), "id");
    }

}
