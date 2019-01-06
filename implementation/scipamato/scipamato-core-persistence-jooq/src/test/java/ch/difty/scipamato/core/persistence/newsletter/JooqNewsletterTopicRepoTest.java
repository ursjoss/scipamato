package ch.difty.scipamato.core.persistence.newsletter;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Collections;

import org.jooq.DSLContext;
import org.jooq.SelectJoinStep;
import org.jooq.impl.DSL;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.core.db.tables.records.NewsletterTopicRecord;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicFilter;
import ch.difty.scipamato.core.persistence.OptimisticLockingException;

@RunWith(MockitoJUnitRunner.class)
public class JooqNewsletterTopicRepoTest {

    @Mock
    private DSLContext      dslContextMock;
    @Mock
    private DateTimeService dateTimeServiceMock;

    private JooqNewsletterTopicRepo repo;

    @Before
    public void setUp() {
        repo = new JooqNewsletterTopicRepo(dslContextMock, dateTimeServiceMock);
    }

    @Test
    public void findingCodesOfClass_withNullLanguageId_throws() {
        assertDegenerateSupplierParameter(() -> repo.findAll(null), "languageCode");
    }

    @Test
    public void insertingNewsletterTopicDefinition_withNullArgument_throws() {
        assertDegenerateSupplierParameter(() -> repo.insert(null), "entity");
    }

    @Test
    public void insertingNewsletterTopicDefinition_withEntityWithNonNullId_throws() {
        NewsletterTopicDefinition ntd = new NewsletterTopicDefinition(1, "de", 1);
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
    public void updatingNewsletterTopicDefinition_withNullArgument_throws() {
        assertDegenerateSupplierParameter(() -> repo.update(null), "entity");
    }

    @Test
    public void updatingNewsletterTopicDefinition_withEntityWithNullId_throws() {
        NewsletterTopicDefinition ntd = new NewsletterTopicDefinition(null, "de", 1);
        assertDegenerateSupplierParameter(() -> repo.update(ntd), "entity.id");
    }

    @Test
    public void applyingWhereCondition_withNullFilter_returnsTrueCondition() {
        SelectJoinStep<NewsletterTopicRecord> selectStep = mock(SelectJoinStep.class);
        repo.applyWhereCondition(null, selectStep);
        verify(selectStep).where(DSL.trueCondition());
    }

    @Test
    public void applyingWhereCondition_withFilterWithNoTitleMask_returnsTrueCondition() {
        SelectJoinStep<NewsletterTopicRecord> selectStep = mock(SelectJoinStep.class);
        NewsletterTopicFilter filter = new NewsletterTopicFilter();
        assertThat(filter.getTitleMask()).isNull();

        repo.applyWhereCondition(filter, selectStep);

        verify(selectStep).where(DSL.trueCondition());
    }

    @Test
    public void handlingUpdatedRecord_withNullRecord_throwsOptimisticLockingException() {
        final NewsletterTopicDefinition entity = new NewsletterTopicDefinition(10, "de", 100);
        final int userId = 5;
        try {
            repo.handleUpdatedRecord(null, entity, userId);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(OptimisticLockingException.class)
                .hasMessage(
                    "Record in table 'newsletter_topic' has been modified prior to the update attempt. Aborting.... [NewsletterTopicDefinition(id=10)]");
        }
    }

    @Test
    public void addingOrThrowing_withNullRecord_throwsOptimisticLockingException() {
        try {
            repo.addOrThrow(null, Collections.emptyList(), "nttObject");
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(OptimisticLockingException.class)
                .hasMessage(
                    "Record in table 'newsletter_topic_tr' has been modified prior to the update attempt. Aborting.... [nttObject]");
        }
    }

    @Test
    public void logingOrThrowing_withDeleteCount0_throws() {
        try {
            repo.logOrThrow(0, 10, "delObj");
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(OptimisticLockingException.class)
                .hasMessage(
                    "Record in table 'newsletter_topic' has been modified prior to the delete attempt. Aborting.... [delObj]");
        }
    }
}