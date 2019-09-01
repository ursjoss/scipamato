package ch.difty.scipamato.publ.persistence.codeclass;

import static ch.difty.scipamato.common.TestUtilsKt.assertDegenerateSupplierParameter;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.jooq.DSLContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JooqCodeClassRepoTest {

    private CodeClassRepository repo;

    @Mock
    private DSLContext dslMock;

    @BeforeEach
    void setUp() {
        repo = new JooqCodeClassRepo(dslMock);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(dslMock);
    }

    @Test
    void finding_withNullLanguageCode_throws() {
        assertDegenerateSupplierParameter(() -> repo.find(null), "languageCode");
    }
}
