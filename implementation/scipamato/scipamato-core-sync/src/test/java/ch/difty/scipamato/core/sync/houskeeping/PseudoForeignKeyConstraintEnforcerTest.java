package ch.difty.scipamato.core.sync.houskeeping;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.jooq.DeleteConditionStep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;

import ch.difty.scipamato.publ.db.public_.tables.records.CodeRecord;

@ExtendWith(MockitoExtension.class)
class PseudoForeignKeyConstraintEnforcerTest {

    private PseudoForeignKeyConstraintEnforcer<CodeRecord> fkce;

    @Mock
    private DeleteConditionStep<CodeRecord> stepMock;
    @Mock
    private StepContribution                contributionMock;
    @Mock
    private ChunkContext                    chunkContextMock;

    @BeforeEach
    void setUp() {
        fkce = new PseudoForeignKeyConstraintEnforcer<>(stepMock, "code", "s");
    }

    @Test
    void executing_withNullStep_doesNotThrow() {
        fkce = new PseudoForeignKeyConstraintEnforcer<>(null, "code", "s");
        assertThat(fkce.execute(contributionMock, chunkContextMock)).isEqualTo(RepeatStatus.FINISHED);
    }

    @Test
    void degenerateConstruction_withNullEntityName_throws() {
        assertDegenerateSupplierParameter(() -> new PseudoForeignKeyConstraintEnforcer<>(stepMock, null, "s"),
            "entityName");
    }

    @Test
    void executing_returnsFinishedStatus() {
        assertThat(fkce.execute(contributionMock, chunkContextMock)).isEqualTo(RepeatStatus.FINISHED);
        verify(stepMock).execute();
    }

    @Test
    void executing_withNullPlural_returnsFinishedStatus_asIfPluralWereS() {
        fkce = new PseudoForeignKeyConstraintEnforcer<>(stepMock, "code", null);
        assertThat(fkce.execute(contributionMock, chunkContextMock)).isEqualTo(RepeatStatus.FINISHED);
        verify(stepMock).execute();
    }

    @Test
    void executing_withSingleModifications_logs() {
        when(stepMock.execute()).thenReturn(1);
        fkce.execute(contributionMock, chunkContextMock);
        verify(stepMock).execute();
        // log un-asserted, log format different from the one in the next test (visual
        // assertion)
    }

    @Test
    void executing_withMultipleModifications_logs() {
        when(stepMock.execute()).thenReturn(2);
        fkce.execute(contributionMock, chunkContextMock);
        verify(stepMock).execute();
        // log un-asserted, log format different form that in the previous test (visual
        // assertion)
    }

    @Test
    void executing_withoutModifications_skipsLog() {
        when(stepMock.execute()).thenReturn(0);
        fkce.execute(contributionMock, chunkContextMock);
        verify(stepMock).execute();
        // missing log un-asserted (visual assertion)
    }

    @Test
    void executing_ignoresContributionMock() {
        fkce.execute(contributionMock, chunkContextMock);
        verifyNoMoreInteractions(contributionMock);
    }

    @Test
    void executing_ignoresChunkContextMock() {
        fkce.execute(contributionMock, chunkContextMock);
        verifyNoMoreInteractions(chunkContextMock);
    }
}