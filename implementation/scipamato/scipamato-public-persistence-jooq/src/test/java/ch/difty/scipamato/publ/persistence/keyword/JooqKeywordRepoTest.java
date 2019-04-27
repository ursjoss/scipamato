package ch.difty.scipamato.publ.persistence.keyword;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;

import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class JooqKeywordRepoTest {

    @Mock
    private DSLContext dslContextMock;

    private JooqKeywordRepo repo;

    @BeforeEach
    void setUp() {
        repo = new JooqKeywordRepo(dslContextMock);
    }

    @Test
    void findingKeywords_withNullLanguageId_throws() {
        assertDegenerateSupplierParameter(() -> repo.findKeywords(null), "languageCode");
    }
}
