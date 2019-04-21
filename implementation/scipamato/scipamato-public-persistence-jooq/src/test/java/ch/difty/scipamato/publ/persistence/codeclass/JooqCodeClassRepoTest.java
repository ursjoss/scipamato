package ch.difty.scipamato.publ.persistence.codeclass;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.jooq.DSLContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JooqCodeClassRepoTest {

    private CodeClassRepository repo;

    @Mock
    private DSLContext dslMock;

    @BeforeEach
    public void setUp() {
        repo = new JooqCodeClassRepo(dslMock);
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(dslMock);
    }

    @Test
    public void finding_withNullLanguageCode_throws() {
        assertDegenerateSupplierParameter(() -> repo.find(null), "languageCode");
    }
}
