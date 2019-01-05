package ch.difty.scipamato.core.persistence.newsletter;

import static ch.difty.scipamato.core.db.tables.Newsletter.NEWSLETTER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;

import org.jooq.TableField;
import org.junit.Test;
import org.mockito.Mock;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.db.tables.records.NewsletterRecord;
import ch.difty.scipamato.core.entity.newsletter.Newsletter;
import ch.difty.scipamato.core.entity.newsletter.NewsletterFilter;
import ch.difty.scipamato.core.persistence.EntityRepository;
import ch.difty.scipamato.core.persistence.JooqEntityRepoTest;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class JooqNewsletterRepoTest extends
    JooqEntityRepoTest<NewsletterRecord, Newsletter, Integer, ch.difty.scipamato.core.db.tables.Newsletter, NewsletterRecordMapper, NewsletterFilter> {

    private static final Integer SAMPLE_ID = 2;

    private JooqNewsletterRepo repo;

    @Mock
    private Newsletter             unpersistedEntity;
    @Mock
    private Newsletter             persistedEntity;
    @Mock
    private NewsletterRecord       persistedRecord;
    @Mock
    private NewsletterRecordMapper mapperMock;
    @Mock
    private NewsletterFilter       filterMock;
    @Mock
    private Newsletter             newsletterMock;
    @Mock
    private PaginationContext      pageableMock;

    @Override
    protected void testSpecificSetUp() {
        when(unpersistedEntity.getVersion()).thenReturn(0);
        when(persistedEntity.getVersion()).thenReturn(0);
        when(persistedRecord.getVersion()).thenReturn(0);
    }

    @Override
    protected EntityRepository<Newsletter, Integer, NewsletterFilter> makeRepoSavingReturning(
        final NewsletterRecord returning) {
        return new JooqNewsletterRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(),
            getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getApplicationProperties()) {
            @Override
            protected NewsletterRecord doSave(final Newsletter entity, final String languageCode) {
                return returning;
            }
        };
    }

    @Override
    protected Integer getSampleId() {
        return SAMPLE_ID;
    }

    @Override
    protected EntityRepository<Newsletter, Integer, NewsletterFilter> getRepo() {
        if (repo == null) {
            repo = new JooqNewsletterRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(),
                getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getApplicationProperties());
        }
        return repo;
    }

    @Override
    protected EntityRepository<Newsletter, Integer, NewsletterFilter> makeRepoFindingEntityById(
        final Newsletter entity) {
        return new JooqNewsletterRepo(getDsl(), getMapper(), getSortMapper(), getFilterConditionMapper(),
            getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(), getApplicationProperties()) {

            @Override
            public Newsletter findById(Integer id, int version) {
                return entity;
            }
        };
    }

    @Override
    protected Newsletter getPersistedEntity() {
        return persistedEntity;
    }

    @Override
    protected Newsletter getUnpersistedEntity() {
        return unpersistedEntity;
    }

    @Override
    protected NewsletterRecord getPersistedRecord() {
        return persistedRecord;
    }

    @Override
    protected NewsletterRecordMapper getMapper() {
        return mapperMock;
    }

    @Override
    protected ch.difty.scipamato.core.db.tables.Newsletter getTable() {
        return NEWSLETTER;
    }

    @Override
    protected TableField<NewsletterRecord, Integer> getTableId() {
        return NEWSLETTER.ID;
    }

    @Override
    protected void expectEntityIdsWithValues() {
        when(unpersistedEntity.getId()).thenReturn(SAMPLE_ID);
        when(unpersistedEntity.getVersion()).thenReturn(0);
        when(persistedRecord.getId()).thenReturn(SAMPLE_ID);
        when(persistedRecord.getVersion()).thenReturn(1);
    }

    @Override
    protected void expectUnpersistedEntityIdNull() {
        when(unpersistedEntity.getId()).thenReturn(null);
        when(unpersistedEntity.getVersion()).thenReturn(0);
    }

    @Override
    protected void verifyUnpersistedEntityId() {
        verify(getUnpersistedEntity()).getId();
    }

    @Override
    protected void verifyPersistedRecordId() {
        verify(persistedRecord).getId();
    }

    @Override
    protected NewsletterFilter getFilter() {
        return filterMock;
    }

    @Override
    protected TableField<NewsletterRecord, Integer> getRecordVersion() {
        return NEWSLETTER.VERSION;
    }

    @Test
    public void mergingPaperIntoNewsletter_withInsertAttemptNotSucceeding_returnsEmptyOptional() {
        JooqNewsletterRepo repo = new JooqNewsletterRepo(getDsl(), getMapper(), getSortMapper(),
            getFilterConditionMapper(), getDateTimeService(), getInsertSetStepSetter(), getUpdateSetStepSetter(),
            getApplicationProperties()) {
            @Override
            int tryInserting(final int newsletterId, final long paperId, final Integer newsletterTopicId,
                final Timestamp ts) {
                return 0;
            }
        };
        assertThat(repo.mergePaperIntoNewsletter(1, 2L, 3, "en")).isNotPresent();
    }
}