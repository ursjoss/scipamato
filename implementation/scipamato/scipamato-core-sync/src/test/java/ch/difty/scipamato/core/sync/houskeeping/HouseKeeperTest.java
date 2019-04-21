package ch.difty.scipamato.core.sync.houskeeping;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.jooq.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;

import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.publ.db.public_.tables.records.CodeClassRecord;

@ExtendWith(MockitoExtension.class)
public class HouseKeeperTest {

    public static final LocalDateTime TS = LocalDateTime.parse("2018-10-28T22:18:00.000");

    private HouseKeeper<CodeClassRecord> hk;

    @Mock
    private DateTimeService                        dateTimeServiceMock;
    @Mock
    private DSLContext                             dslContextMock;
    @Mock
    private Table<CodeClassRecord>                 tableMock;
    @Mock
    private DeleteWhereStep<CodeClassRecord>       deleteWhereStepMock;
    @Mock
    private DeleteConditionStep<CodeClassRecord>   deleteCondStepMock;
    @Mock
    private TableField<CodeClassRecord, Timestamp> lastSynchedField;
    @Mock
    private StepContribution                       contributionMock;
    @Mock
    private ChunkContext                           chunkContextMock;

    @BeforeEach
    public void setUp() {
        hk = new HouseKeeper<>(dslContextMock, lastSynchedField, dateTimeServiceMock, 30, "code_class");

        when(lastSynchedField.getTable()).thenReturn(tableMock);
        when(dslContextMock.deleteFrom(tableMock)).thenReturn(deleteWhereStepMock);
        when(dateTimeServiceMock.getCurrentDateTime()).thenReturn(TS);
        when(deleteWhereStepMock.where(lastSynchedField.lessThan(Timestamp.valueOf(TS)))).thenReturn(
            deleteCondStepMock);
    }

    @Test
    public void degenerateConstruction_withNullStep_throws() {
        assertDegenerateSupplierParameter(
            () -> new HouseKeeper<>(null, lastSynchedField, dateTimeServiceMock, 30, "code_class"), "jooqPublic");
    }

    @Test
    public void degenerateConstruction_withNullLastSynchedField_throws() {
        assertDegenerateSupplierParameter(
            () -> new HouseKeeper<>(dslContextMock, null, dateTimeServiceMock, 30, "code_class"), "lastSynchedField");
    }

    @Test
    public void degenerateConstruction_withNullDateTimeService_throws() {
        assertDegenerateSupplierParameter(
            () -> new HouseKeeper<>(dslContextMock, lastSynchedField, null, 30, "code_class"), "dateTimeService");
    }

    @Test
    public void degenerateConstruction_withNullEntityName_throws() {
        assertDegenerateSupplierParameter(
            () -> new HouseKeeper<>(dslContextMock, lastSynchedField, dateTimeServiceMock, 30, null), "entityName");
    }

    @Test
    public void executing_returnsFinishedStatus() {
        assertThat(hk.execute(contributionMock, chunkContextMock)).isEqualTo(RepeatStatus.FINISHED);
        verify(dateTimeServiceMock).getCurrentDateTime();
    }

    @Test
    public void executing_withSingleModifications_logs() {
        when(deleteCondStepMock.execute()).thenReturn(1);
        hk.execute(contributionMock, chunkContextMock);
        verify(dateTimeServiceMock).getCurrentDateTime();
        verify(deleteCondStepMock).execute();
        // log un-asserted, log format different from the one in the next test (visual
        // assertion)
    }

    @Test
    public void executing_withMultipleModifications_logs() {
        when(deleteCondStepMock.execute()).thenReturn(2);
        hk.execute(contributionMock, chunkContextMock);
        verify(dateTimeServiceMock).getCurrentDateTime();
        verify(deleteCondStepMock).execute();
        // log un-asserted, log format different form that in the previous test (visual
        // assertion)
    }

    @Test
    public void executing_withoutModifications_skipsLog() {
        when(deleteCondStepMock.execute()).thenReturn(0);
        hk.execute(contributionMock, chunkContextMock);
        verify(dateTimeServiceMock).getCurrentDateTime();
        verify(deleteCondStepMock).execute();
        // missing log un-asserted (visual assertion)
    }

    @Test
    public void executing_ignoresContributionMock() {
        hk.execute(contributionMock, chunkContextMock);
        verify(dateTimeServiceMock).getCurrentDateTime();
        verifyNoMoreInteractions(contributionMock);
    }

    @Test
    public void executing_ignoresChunkContextMock() {
        hk.execute(contributionMock, chunkContextMock);
        verify(dateTimeServiceMock).getCurrentDateTime();
        verifyNoMoreInteractions(chunkContextMock);
    }
}
