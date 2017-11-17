package ch.difty.scipamato.persistence.code;

import static ch.difty.scipamato.TestUtils.*;

import org.jooq.DSLContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import ch.difty.scipamato.entity.CodeClassId;

public class JooqCodeRepoTest {

    @Mock
    private DSLContext dslContextMock;

    private JooqCodeRepo repo;

    @Before
    public void setUp() {
        repo = new JooqCodeRepo(dslContextMock);
    }

    @Test
    public void findingCodesOfClass_withNullCodeClassId_throws() {
        assertDegenerateSupplierParameter(() -> repo.findCodesOfClass(null, "de"), "codeClassId");
    }

    @Test
    public void findingCodesOfClass_withNullLanguageId_throws() {
        assertDegenerateSupplierParameter(() -> repo.findCodesOfClass(CodeClassId.CC1, null), "languageCode");
    }
}
