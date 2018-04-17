package ch.difty.scipamato.core.sync.houskeeping;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DeleteConditionStep;
import org.jooq.impl.UpdatableRecordImpl;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import ch.difty.scipamato.common.AssertAs;

/**
 * {@link Tasklet} used for house keeping, i.e. purging records that once were
 * present in scipamato-core and were synchronized to scipamato-public once but
 * now are not present anymore in scipamato-core.
 * <p>
 * The synchronization step that inserts/updates records from core to public
 * touches the lastSynchronized timestamp of each record that is present in
 * scipamato-core. Hence any record in scipamato-public that has no updated
 * timestamp after the first synchronization step must be missing in core and is
 * eligible for purging.
 * <p>
 * A grace time (in minutes) is applied before purging, i.e. any record which
 * has not been touched within X minutes will be purged. The default value is 30
 * minutes but is configurable ({@literal purge_grace_time_in_minutes}).
 *
 * @param <R>
 *     type of {@link UpdatableRecordImpl} - the record to purge.
 * @author u.joss
 */
@Slf4j
public class HouseKeeper<R extends UpdatableRecordImpl<?>> implements Tasklet {

    private final DeleteConditionStep<R> ddl;

    /**
     * @param deleteDdl
     *     executable jooq delete condition step to delete the records of
     *     type {@literal T}
     * @param graceTimeInMinutes
     *     records that were touched during synchronization within that many
     *     minutes will be excluded from purging
     */
    public HouseKeeper(final DeleteConditionStep<R> deleteDdl, final int graceTimeInMinutes) {
        this.ddl = AssertAs.notNull(deleteDdl, "deleteDdl");
        log.debug("Purging code_classes that have not been synched in the last {} minutes...", graceTimeInMinutes);
    }

    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) {
        final int result = ddl.execute();
        if (result > 0)
            log.info("Successfully deleted {} code_class{} in scipamato-public", result, (result > 1 ? "es" : ""));
        return RepeatStatus.FINISHED;
    }

}
