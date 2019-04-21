package ch.difty.scipamato.core.persistence.keyword;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;
import static ch.difty.scipamato.core.db.tables.KeywordTr.KEYWORD_TR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;

import java.util.*;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.core.db.tables.records.KeywordTrRecord;
import ch.difty.scipamato.core.entity.keyword.KeywordDefinition;
import ch.difty.scipamato.core.entity.keyword.KeywordTranslation;
import ch.difty.scipamato.core.persistence.OptimisticLockingException;

@ExtendWith(MockitoExtension.class)
public class JooqKeywordRepoTest {

    @Mock
    private DSLContext      dslContextMock;
    @Mock
    private DateTimeService dateTimeServiceMock;

    private JooqKeywordRepo repo;

    @BeforeEach
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

    @Test
    @SuppressWarnings("unchecked")
    public void removingObsoletePersistedRecords() {
        final KeywordTranslation kt = new KeywordTranslation(1, "de", "kw1", 1);
        final Result<KeywordTrRecord> resultMock = mock(Result.class);
        final Iterator itMock = mock(Iterator.class);
        when(resultMock.iterator()).thenReturn(itMock);
        final KeywordTrRecord ktr1 = mock(KeywordTrRecord.class);
        when(ktr1.get(KEYWORD_TR.ID)).thenReturn(1);
        final KeywordTrRecord ktr2 = mock(KeywordTrRecord.class);
        when(ktr2.get(KEYWORD_TR.ID)).thenReturn(2);
        when(itMock.hasNext()).thenReturn(true, true, false);
        when(itMock.next()).thenReturn(ktr1, ktr2);

        repo.removeObsoletePersistedRecordsFor(resultMock, Collections.singletonList(kt));

        verify(resultMock).iterator();
        verify(itMock, times(3)).hasNext();
        verify(itMock, times(2)).next();
        verify(ktr1).get(KEYWORD_TR.ID);
        verify(ktr2).get(KEYWORD_TR.ID);
        verify(ktr2).delete();

        verifyNoMoreInteractions(resultMock, itMock, ktr1, ktr2);
    }

    @Test
    public void removingObsoletePersistedRecords_whenCheckingIfTranslationIsPresentInEntity_doesNotConsiderIdLessEntityTranslations() {
        final KeywordTranslation ct = new KeywordTranslation(null, "de", "1ade", 1);
        final Result<KeywordTrRecord> resultMock = mock(Result.class);
        final Iterator itMock = mock(Iterator.class);
        when(resultMock.iterator()).thenReturn(itMock);
        final KeywordTrRecord ctr1 = mock(KeywordTrRecord.class);
        final KeywordTrRecord ctr2 = mock(KeywordTrRecord.class);
        when(itMock.hasNext()).thenReturn(true, true, false);
        when(itMock.next()).thenReturn(ctr1, ctr2);

        repo.removeObsoletePersistedRecordsFor(resultMock, Arrays.asList(ct));

        verify(resultMock).iterator();
        verify(itMock, times(3)).hasNext();
        verify(itMock, times(2)).next();
        verify(ctr1).delete();
        verify(ctr2).delete();

        verifyNoMoreInteractions(resultMock, itMock, ctr1, ctr2);
    }

    @Test
    public void managingTranslations() {
        final KeywordDefinition entity = new KeywordDefinition(1, "de", 10);

        try {
            repo.manageTranslations(null, entity, Collections.emptyList());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(OptimisticLockingException.class)
                .hasMessage(
                    "Record in table 'keyword' has been modified prior to the update attempt. Aborting.... [KeywordDefinition(id=1, searchOverride=null)]");
        }
    }

    @Test
    public void addingOrUpdatingTranlsation() {
        KeywordTrRecord ktrMock = mock(KeywordTrRecord.class);
        when(ktrMock.get(KEYWORD_TR.ID)).thenReturn(1000);
        when(ktrMock.get(KEYWORD_TR.LANG_CODE)).thenReturn("de");
        when(ktrMock.get(KEYWORD_TR.NAME)).thenReturn("someName");
        when(ktrMock.get(KEYWORD_TR.VERSION)).thenReturn(500);

        repo = new JooqKeywordRepo(dslContextMock, dateTimeServiceMock) {
            @Override
            KeywordTrRecord insertAndGetKeywordTr(final int keywordId, final int userId, final KeywordTranslation kt) {
                return ktrMock;
            }
        };

        final KeywordDefinition entity = new KeywordDefinition(1, "de", 1);
        final int userId = 5;
        final List<KeywordTranslation> translations = new ArrayList<>();
        final KeywordTranslation kt = new KeywordTranslation(null, "de", "trs1", 1);

        assertThat(kt.getId()).isNull();

        repo.addOrUpdateTranslation(kt, entity, userId, translations);

        assertThat(translations).hasSize(1);
        KeywordTranslation translation = translations.get(0);

        assertThat(translation.getId()).isEqualTo(1000);
        assertThat(translation.getLangCode()).isEqualTo("de");
        assertThat(translation.getName()).isEqualTo("someName");
        assertThat(translation.getVersion()).isEqualTo(500);
    }

    @Test
    public void addOrThrow_withNullRecord_throwsOptimisticLockingException() {
        try {
            repo.addOrThrow(null, "trslString", new ArrayList<>());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(OptimisticLockingException.class)
                .hasMessage(
                    "Record in table 'keyword_tr' has been modified prior to the update attempt. Aborting.... [trslString]");
        }
    }

    @Test
    public void loggingOrThrowing_withDeleteCountZero_throws() {
        try {
            repo.logOrThrow(0, 1, new KeywordDefinition(10, "de", 100));
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(OptimisticLockingException.class)
                .hasMessage(
                    "Record in table 'keyword' has been modified prior to the delete attempt. Aborting.... [KeywordDefinition(id=10, searchOverride=null)]");
        }
    }
}