package ch.difty.scipamato.core.persistence.code;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;
import static ch.difty.scipamato.core.db.tables.CodeTr.CODE_TR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.common.entity.CodeClassId;
import ch.difty.scipamato.core.db.tables.records.CodeTrRecord;
import ch.difty.scipamato.core.entity.CodeClass;
import ch.difty.scipamato.core.entity.code.CodeDefinition;
import ch.difty.scipamato.core.entity.code.CodeTranslation;
import ch.difty.scipamato.core.persistence.OptimisticLockingException;
import ch.difty.scipamato.core.persistence.codeclass.CodeClassRepository;

@RunWith(MockitoJUnitRunner.class)
public class JooqCodeRepoTest {

    @Mock
    private DSLContext dslContextMock;

    @Mock
    private DateTimeService dtsMock;

    @Mock
    private CodeClassRepository codeClassRepoMock;

    private JooqCodeRepo repo;

    @Before
    public void setUp() {
        repo = new JooqCodeRepo(dslContextMock, dtsMock, codeClassRepoMock);
    }

    @Test
    public void degenerateConstruction_withNullDsl_throws() {
        assertDegenerateSupplierParameter(() -> new JooqCodeRepo(null, dtsMock, codeClassRepoMock), "dsl");
    }

    @Test
    public void degenerateConstruction_withNullDateTimeService_throws() {
        assertDegenerateSupplierParameter(() -> new JooqCodeRepo(dslContextMock, null, codeClassRepoMock),
            "dateTimeService");
    }

    @Test
    public void degenerateConstruction_withNullCodeClassRepo_throws() {
        assertDegenerateSupplierParameter(() -> new JooqCodeRepo(dslContextMock, dtsMock, null), "codeClassRepo");
    }

    @Test
    public void findingCodesOfClass_withNullCodeClassId_throws() {
        assertDegenerateSupplierParameter(() -> repo.findCodesOfClass(null, "de"), "codeClassId");
    }

    @Test
    public void findingCodesOfClass_withNullLanguageId_throws() {
        assertDegenerateSupplierParameter(() -> repo.findCodesOfClass(CodeClassId.CC1, null), "languageCode");
    }

    @Test
    public void insertingOrUpdating_withNullCodeDefinition_throws() {
        assertDegenerateSupplierParameter(() -> repo.saveOrUpdate(null), "codeDefinition");
    }

    @Test
    public void insertingOrUpdating_withNullCodeClass_throws() {
        CodeDefinition cd = new CodeDefinition("1Z", "de", null, 1, true, 1);
        assertDegenerateSupplierParameter(() -> repo.saveOrUpdate(cd), "codeDefinition.codeClass");
    }

    @Test
    public void insertingOrUpdating_withNullCodeClassId_throws() {
        CodeDefinition cd = new CodeDefinition("1Z", "de", new CodeClass(null, "foo", null), 1, true, 1);
        assertDegenerateSupplierParameter(() -> repo.saveOrUpdate(cd), "codeDefinition.codeClass.id");
    }

    @Test
    public void removingObsoletePersistedRecords() {
        final CodeTranslation ct = new CodeTranslation(1, "de", "1ade", "", 1);
        final Result<CodeTrRecord> resultMock = mock(Result.class);
        final Iterator itMock = mock(Iterator.class);
        when(resultMock.iterator()).thenReturn(itMock);
        final CodeTrRecord ctr1 = mock(CodeTrRecord.class);
        when(ctr1.get(CODE_TR.ID)).thenReturn(1);
        final CodeTrRecord ctr2 = mock(CodeTrRecord.class);
        when(ctr2.get(CODE_TR.ID)).thenReturn(2);
        when(itMock.hasNext()).thenReturn(true, true, false);
        when(itMock.next()).thenReturn(ctr1, ctr2);

        repo.removeObsoletePersistedRecordsFor(resultMock, Arrays.asList(ct));

        verify(resultMock).iterator();
        verify(itMock, times(3)).hasNext();
        verify(itMock, times(2)).next();
        verify(ctr1).get(CODE_TR.ID);
        verify(ctr2).get(CODE_TR.ID);
        verify(ctr2).delete();

        verifyNoMoreInteractions(resultMock, itMock, ctr1, ctr2);
    }

    @Test
    public void removingObsoletePersistedRecords_whenCheckingIfTranslationIsPresentInEntity_doesNotConsiderIdLessEntityTranslations() {
        final CodeTranslation ct = new CodeTranslation(null, "de", "1ade", "", 1);
        final Result<CodeTrRecord> resultMock = mock(Result.class);
        final Iterator itMock = mock(Iterator.class);
        when(resultMock.iterator()).thenReturn(itMock);
        final CodeTrRecord ctr1 = mock(CodeTrRecord.class);
        final CodeTrRecord ctr2 = mock(CodeTrRecord.class);
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
    public void consideringAdding_withNullRecord_throwsOptimisticLockingException() {
        try {
            repo.considerAdding(null, new ArrayList<>(), new CodeTranslation(1, "de", "c1", "comm", 10));
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(OptimisticLockingException.class)
                .hasMessage(
                    "Record in table 'code_tr' has been modified prior to the update attempt. Aborting.... [CodeTranslation(comment=comm)]");
        }
    }

    @Test
    public void logOrThrow_withDeleteCount0_throws() {
        try {
            repo.logOrThrow(0, "1A", "deletedObject");
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(OptimisticLockingException.class)
                .hasMessage(
                    "Record in table 'code' has been modified prior to the delete attempt. Aborting.... [deletedObject]");
        }
    }
}
