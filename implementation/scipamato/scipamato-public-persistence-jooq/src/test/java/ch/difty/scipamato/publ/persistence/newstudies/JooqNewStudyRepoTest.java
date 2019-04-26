package ch.difty.scipamato.publ.persistence.newstudies;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;

import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class JooqNewStudyRepoTest {

    @Mock
    private DSLContext dslContextMock;

    private JooqNewStudyRepo repo;

    @BeforeEach
    void setUp() {
        repo = new JooqNewStudyRepo(dslContextMock);
    }

    @Test
    void findingCodesOfClass_withNullCodeClassId_throws() {
        assertDegenerateSupplierParameter(() -> repo.findNewStudyTopicsForNewsletter(1, null), "languageCode");
    }

    @Test
    void findingIdOfNewsletterWithIssue_withNullIssue() {
        assertDegenerateSupplierParameter(() -> repo.findIdOfNewsletterWithIssue(null), "issue");
    }

    @Test
    void findingArchivedNewsletters_withNullLanguageCode() {
        assertDegenerateSupplierParameter(() -> repo.findArchivedNewsletters(14, null), "languageCode");
    }

    @Test
    void findingNewStudyPageLinks_withNullLanguageCode() {
        assertDegenerateSupplierParameter(() -> repo.findNewStudyPageLinks(null), "languageCode");
    }
}