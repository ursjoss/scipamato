package ch.difty.scipamato.core.persistence.codeclass;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;
import static ch.difty.scipamato.core.db.tables.CodeClassTr.CODE_CLASS_TR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.core.db.tables.records.CodeClassTrRecord;
import ch.difty.scipamato.core.entity.code_class.CodeClassTranslation;
import ch.difty.scipamato.core.persistence.OptimisticLockingException;

@ExtendWith(MockitoExtension.class)
class JooqCodeClassRepoTest {

    @Mock
    private DSLContext dslContextMock;

    @Mock
    private DateTimeService dtsMock;

    private JooqCodeClassRepo repo;

    @BeforeEach
    void setUp() {
        repo = new JooqCodeClassRepo(dslContextMock, dtsMock);
    }

    @Test
    void degenerateConstruction_withNullDsl_throws() {
        assertDegenerateSupplierParameter(() -> new JooqCodeClassRepo(null, dtsMock), "dsl");
    }

    @Test
    void degenerateConstruction_withNullDateTimeService_throws() {
        assertDegenerateSupplierParameter(() -> new JooqCodeClassRepo(dslContextMock, null), "dateTimeService");
    }

    @Test
    void finding_withNullLanguageId_throws() {
        assertDegenerateSupplierParameter(() -> repo.find(null), "languageCode");
    }

    @Test
    void insertingOrUpdating_withNullCodeClassDefinition_throws() {
        assertDegenerateSupplierParameter(() -> repo.saveOrUpdate(null), "codeClassDefinition");
    }

    @Test
    void findingCodeClassDefinition_withNullId_throws() {
        assertDegenerateSupplierParameter(() -> repo.findCodeClassDefinition(null), "id");
    }

    @Test
    void deleting_withNullId_throws() {
        assertDegenerateSupplierParameter(() -> repo.delete(null, 1), "id");
    }

    @Test
    @Disabled("Reactivate with mockito-3.0.2+")
    void removingObsoletePersistedRecords() {
        final Integer codeClassId = 1;
        final CodeClassTranslation cct = new CodeClassTranslation(1, "de", "cc1", "", 1);
        final Result<CodeClassTrRecord> resultMock = mock(Result.class);
        final Iterator itMock = mock(Iterator.class);
        when(resultMock.iterator()).thenReturn(itMock);
        final CodeClassTrRecord cctr1 = mock(CodeClassTrRecord.class);
        when(cctr1.get(CODE_CLASS_TR.ID)).thenReturn(1);
        final CodeClassTrRecord cctr2 = mock(CodeClassTrRecord.class);
        when(cctr2.get(CODE_CLASS_TR.ID)).thenReturn(2);
        when(itMock.hasNext()).thenReturn(true, true, false);
        when(itMock.next()).thenReturn(cctr1, cctr2);

        repo.removeObsoletePersistedRecordsFor(resultMock, Collections.singletonList(cct));

        verify(resultMock).iterator();
        verify(itMock, times(3)).hasNext();
        verify(itMock, times(2)).next();
        verify(cctr1).get(CODE_CLASS_TR.ID);
        verify(cctr2).get(CODE_CLASS_TR.ID);
        verify(cctr2).delete();

        verifyNoMoreInteractions(resultMock, itMock, cctr1, cctr2);
    }

    @Test
    void consideringAdding_withNullRecord_throwsOptimisticLockingException() {
        try {
            repo.considerAdding(null, new ArrayList<>(), new CodeClassTranslation(1, "de", "c1", "comm", 10));
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(OptimisticLockingException.class)
                .hasMessage(
                    "Record in table 'code_class_tr' has been modified prior to the update attempt. Aborting.... [CodeClassTranslation(description=comm)]");
        }
    }

    @Test
    void logOrThrow_withDeleteCount0_throws() {
        try {
            repo.logOrThrow(0, 1, "deletedObject");
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(OptimisticLockingException.class)
                .hasMessage(
                    "Record in table 'code_class' has been modified prior to the delete attempt. Aborting.... [deletedObject]");
        }
    }
}