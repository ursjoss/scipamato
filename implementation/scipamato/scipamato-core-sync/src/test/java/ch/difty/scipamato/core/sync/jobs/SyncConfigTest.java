package ch.difty.scipamato.core.sync.jobs;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.jooq.DeleteConditionStep;
import org.jooq.TableField;
import org.jooq.UpdatableRecord;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.support.RunIdIncrementer;

public abstract class SyncConfigTest<R extends UpdatableRecord<R>> {

    protected static final Timestamp CREATED  = Timestamp.valueOf(LocalDateTime
        .now()
        .minusDays(2));
    protected static final Timestamp MODIFIED = Timestamp.valueOf(LocalDateTime
        .now()
        .minusDays(1));

    /**
     * @return the job instance
     */
    protected abstract Job getJob();

    /**
     * @return the select sql string from the SyncConfig
     */
    protected abstract String selectSql();

    /**
     * @return the last sync field
     */
    protected abstract TableField<R, Timestamp> lastSynchedField();

    /**
     * @return the pseudo foreign key ref data integrity enforcement stop from the SysConfig - or null if none applies.
     */
    protected abstract DeleteConditionStep<R> getPseudoFkDcs();

    /**
     * @return the expected job name
     */
    protected abstract String expectedJobName();

    /**
     * @return the expected SQL string for fetching records
     */
    protected abstract String expectedSelectSql();

    /**
     * @return the last sync field
     */
    protected abstract TableField<R, Timestamp> expectedLastSyncField();

    /**
     * @return the expected SQL string for purging obsolete entries - or null if none applies
     */
    public String expectedPseudoFkSql() {
        return null;
    }

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
    public void assertingPurgeLastSynchField() {
        assertThat(lastSynchedField()).isEqualTo(expectedLastSyncField());
    }

    @Test
    public void assertingPseudoRefDataEnforcementDdl() {
        final DeleteConditionStep<R> dcs = getPseudoFkDcs();
        if (dcs != null)
            assertThat(dcs.getSQL()).isEqualTo(expectedPseudoFkSql());
        else
            assertThat(expectedPseudoFkSql()).isNull();
    }
}