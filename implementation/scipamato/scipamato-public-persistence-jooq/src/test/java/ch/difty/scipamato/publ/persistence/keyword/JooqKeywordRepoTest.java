package ch.difty.scipamato.publ.persistence.keyword;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;

import org.jooq.DSLContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class JooqKeywordRepoTest {

    @Mock
    private DSLContext dslContextMock;

    private JooqKeywordRepo repo;

    @Before
    public void setUp() {
        repo = new JooqKeywordRepo(dslContextMock);
    }

    @Test
    public void findingKeywords_withNullLanguageId_throws() {
        assertDegenerateSupplierParameter(() -> repo.findKeywords(null), "languageCode");
    }
}
