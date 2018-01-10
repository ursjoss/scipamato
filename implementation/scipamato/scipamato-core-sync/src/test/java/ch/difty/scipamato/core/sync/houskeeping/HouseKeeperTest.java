package ch.difty.scipamato.core.sync.houskeeping;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.jooq.DeleteConditionStep;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;

import ch.difty.scipamato.public_.db.public_.tables.records.CodeClassRecord;

@RunWith(MockitoJUnitRunner.class)
public class HouseKeeperTest {

    private HouseKeeper<CodeClassRecord>         hk;
    @Mock
    private DeleteConditionStep<CodeClassRecord> stepMock;
    @Mock
    private StepContribution                     contributionMock;
    @Mock
    private ChunkContext                         chunkContextMock;

    @Before
    public void setUp() {
        hk = new HouseKeeper<>(stepMock, 30);
    }

    @Test
    public void degenerateConstruction_throws() {
        assertDegenerateSupplierParameter(
            () -> new HouseKeeper<CodeClassRecord>((DeleteConditionStep<CodeClassRecord>) null, 30), "deleteDdl");
    }

    @Test
    public void executing_returnsFinishedStatus() throws Exception {
        assertThat(hk.execute(contributionMock, chunkContextMock)).isEqualTo(RepeatStatus.FINISHED);
    }

    @Test
    public void executing_withSingleModifications_logs() throws Exception {
        when(stepMock.execute()).thenReturn(1);
        hk.execute(contributionMock, chunkContextMock);
        verify(stepMock).execute();
        // log unasserted, log format different from the one in the next test (visual
        // assertion)
    }

    @Test
    public void executing_withMultipleModifications_logs() throws Exception {
        when(stepMock.execute()).thenReturn(2);
        hk.execute(contributionMock, chunkContextMock);
        verify(stepMock).execute();
        // log unasserted, log format different form that in the previous test (visual
        // assertion)
    }

    @Test
    public void executing_withoutModifications_skipsLog() throws Exception {
        when(stepMock.execute()).thenReturn(0);
        hk.execute(contributionMock, chunkContextMock);
        verify(stepMock).execute();
        // missing log unasserted (visual assertion)
    }

    @Test
    public void executing_ignoresContributionMock() throws Exception {
        hk.execute(contributionMock, chunkContextMock);
        verifyNoMoreInteractions(contributionMock);
    }

    @Test
    public void executing_ignoresChunkContexMock() throws Exception {
        hk.execute(contributionMock, chunkContextMock);
        verifyNoMoreInteractions(chunkContextMock);
    }
}
