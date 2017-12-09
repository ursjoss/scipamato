package ch.difty.scipamato.core.sync.jobs;

import static org.assertj.core.api.Assertions.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.jooq.DeleteConditionStep;
import org.jooq.UpdatableRecord;
import org.junit.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.support.RunIdIncrementer;

public abstract class SyncConfigTest<R extends UpdatableRecord<R>> {

    protected static final Timestamp CREATED = Timestamp.valueOf(LocalDateTime.now().minusDays(2));
    protected static final Timestamp MODIFIED = Timestamp.valueOf(LocalDateTime.now().minusDays(1));

    /**
     * @return the job instance
     */
    protected abstract Job getJob();

    /**
     * @return the select sql string from the SyncConfig
     */
    protected abstract String selectSql();

    /**
     * @return the purge condition step from the SyncConfig
     */
    protected abstract DeleteConditionStep<R> purgeDeleteConditionStep();

    /**
     * @return the expected job name
     */
    protected abstract String expectedJobName();

    /**
     * @return the expected SQL string for fetching records
     */
    protected abstract String expectedSelectSql();

    /**
     * @return the expected SQL string for purging obsolete entries
     */
    protected abstract String expectedPurgeSql();

    @Test
    public void assertingJobIncrementer_toBeRunIdIncrementer() {
        assertThat(getJob().getJobParametersIncrementer()).isInstanceOf(RunIdIncrementer.class);
    }

    @Test
    public void assertingJobName() {
        assertThat(getJob().getName()).isEqualTo(expectedJobName());
    }

    @Test
    public void jobIsRestartable() {
        assertThat(getJob().isRestartable()).isTrue();
    }

    @Test
    public void assertingSql() {
        assertThat(selectSql()).isEqualTo(expectedSelectSql());
    }

    @Test
    public void assertingPurgeDdl() {
        assertThat(purgeDeleteConditionStep().getSQL()).isEqualTo(expectedPurgeSql());
    }

}