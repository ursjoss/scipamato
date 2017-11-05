package ch.difty.scipamato.persistence.codeclass;

import static org.assertj.core.api.Assertions.*;

import org.jooq.DSLContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import ch.difty.scipamato.NullArgumentException;

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
