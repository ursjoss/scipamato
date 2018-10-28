package ch.difty.scipamato.core.persistence.keyword;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.jooq.DSLContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.core.entity.keyword.KeywordDefinition;

@RunWith(MockitoJUnitRunner.class)
public class JooqKeywordRepoTest {

    @Mock
    private DSLContext      dslContextMock;
    @Mock
    private DateTimeService dateTimeServiceMock;

    private JooqKeywordRepo repo;

    @Before
    public void setUp() {
        repo = new JooqKeywordRepo(dslContextMock, dateTimeServiceMock);
    }

    @Test
    public void findingCodesOfClass_withNullLanguageId_throws() {
        assertDegenerateSupplierParameter(() -> repo.findAll(null), "languageCode");
    }

    @Test
    public void insertingKeywordDefinition_withNullArgument_throws() {
        assertDegenerateSupplierParameter(() -> repo.insert(null), "entity");
    }

    @Test
    public void insertingKeywordDefinition_withEntityWithNonNullId_throws() {
        KeywordDefinition ntd = new KeywordDefinition(1, "de", 1);
        try {
            repo.insert(ntd);
            fail("Should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("id must be null.");
        }
    }

    @Test
    public void updatingKeywordDefinition_withNullArgument_throws() {
        assertDegenerateSupplierParameter(() -> repo.update(null), "entity");
    }

    @Test
    public void updatingKeywordDefinition_withEntityWithNullId_throws() {
        KeywordDefinition ntd = new KeywordDefinition(null, "de", 1);
        assertDegenerateSupplierParameter(() -> repo.update(ntd), "entity.id");
    }
}