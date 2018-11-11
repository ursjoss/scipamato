package ch.difty.scipamato.core.persistence.code;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;

import org.jooq.DSLContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.common.entity.CodeClassId;
import ch.difty.scipamato.core.entity.CodeClass;
import ch.difty.scipamato.core.entity.code.CodeDefinition;
import ch.difty.scipamato.core.persistence.codeclass.CodeClassRepository;

@RunWith(MockitoJUnitRunner.class)
public class JooqCodeRepoTest {

    @Mock
    private DSLContext dslContextMock;

    @Mock
    private DateTimeService dtsMock;

    @Mock
    private CodeClassRepository codeClassRepoMock;

    private JooqCodeRepo repo;

    @Before
    public void setUp() {
        repo = new JooqCodeRepo(dslContextMock, dtsMock, codeClassRepoMock);
    }

    @Test
    public void degenerateConstruction_withNullDsl_throws() {
        assertDegenerateSupplierParameter(() -> new JooqCodeRepo(null, dtsMock, codeClassRepoMock), "dsl");
    }

    @Test
    public void degenerateConstruction_withNullDateTimeService_throws() {
        assertDegenerateSupplierParameter(() -> new JooqCodeRepo(dslContextMock, null, codeClassRepoMock),
            "dateTimeService");
    }

    @Test
    public void degenerateConstruction_withNullCodeClassRepo_throws() {
        assertDegenerateSupplierParameter(() -> new JooqCodeRepo(dslContextMock, dtsMock, null), "codeClassRepo");
    }

    @Test
    public void findingCodesOfClass_withNullCodeClassId_throws() {
        assertDegenerateSupplierParameter(() -> repo.findCodesOfClass(null, "de"), "codeClassId");
    }

    @Test
    public void findingCodesOfClass_withNullLanguageId_throws() {
        assertDegenerateSupplierParameter(() -> repo.findCodesOfClass(CodeClassId.CC1, null), "languageCode");
    }

    @Test
    public void insertingOrUpdating_withNullCodeDefinition_throws() {
        assertDegenerateSupplierParameter(() -> repo.saveOrUpdate(null), "codeDefinition");
    }

    @Test
    public void insertingOrUpdating_withNullCodeClass_throws() {
        CodeDefinition cd = new CodeDefinition("1Z", "de", null, 1, true, 1);
        assertDegenerateSupplierParameter(() -> repo.saveOrUpdate(cd), "codeDefinition.codeClass");
    }

    @Test
    public void insertingOrUpdating_withNullCodeClassId_throws() {
        CodeDefinition cd = new CodeDefinition("1Z", "de", new CodeClass(null, "foo", null), 1, true, 1);
        assertDegenerateSupplierParameter(() -> repo.saveOrUpdate(cd), "codeDefinition.codeClass.id");
    }

}
