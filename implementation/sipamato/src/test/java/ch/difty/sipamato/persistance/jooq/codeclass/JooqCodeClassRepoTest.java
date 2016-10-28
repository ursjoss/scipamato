package ch.difty.sipamato.persistance.jooq.codeclass;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.jooq.DSLContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import ch.difty.sipamato.lib.NullArgumentException;

public class JooqCodeClassRepoTest {

    @Mock
    private DSLContext dslContextMock;

    private JooqCodeClassRepo repo;

    @Before
    public void setUp() {
        repo = new JooqCodeClassRepo(dslContextMock);
    }

    @Test
    public void findingCodesOfClass_withNullLanguageId_throws() {
        try {
            repo.find(null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("languageCode must not be null.");
        }
    }
}
