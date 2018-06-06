package ch.difty.scipamato.core.persistence.newsletter;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;

import org.jooq.DSLContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class JooqNewsletterTopicRepoTest {

    @Mock
    private DSLContext dslContextMock;

    private JooqNewsletterTopicRepo repo;

    @Before
    public void setUp() {
        repo = new JooqNewsletterTopicRepo(dslContextMock);
    }

    @Test
    public void findingCodesOfClass_withNullLanguageId_throws() {
        assertDegenerateSupplierParameter(() -> repo.findAll(null), "languageCode");
    }
}