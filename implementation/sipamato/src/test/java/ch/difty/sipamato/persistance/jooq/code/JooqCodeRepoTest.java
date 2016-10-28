package ch.difty.sipamato.persistance.jooq.code;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.jooq.DSLContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import ch.difty.sipamato.entity.CodeClassId;
import ch.difty.sipamato.lib.NullArgumentException;

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
        try {
            repo.findCodesOfClass(null, "de");
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("codeClassId must not be null.");
        }
    }

    @Test
    public void findingCodesOfClass_withNullLanguageId_throws() {
        try {
            repo.findCodesOfClass(CodeClassId.CC1, null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("languageCode must not be null.");
        }
    }
}
