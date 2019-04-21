package ch.difty.scipamato.publ.persistence.code;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;

import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import ch.difty.scipamato.common.entity.CodeClassId;

public class JooqCodeRepoTest {

    @Mock
    private DSLContext dslContextMock;

    private JooqCodeRepo repo;

    @BeforeEach
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
