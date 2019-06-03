package ch.difty.scipamato.core.sync.houskeeping;

import java.sql.Timestamp;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.impl.UpdatableRecordImpl;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.DateTimeService;

/**
 * {@link Tasklet} used for house keeping, i.e. purging records that once were
 * present in scipamato-core and were synchronized to scipamato-public in the past
 * but now are not present anymore in scipamato-core.
 * <p>
 * The synchronization step that inserts/updates records from core to public
 * touches the lastSynchronized timestamp of each record present in scipamato-core.
 * Hence any record in scipamato-public that has no updated timestamp after the first
 * synchronization step must be missing in core and is eligible for purging.
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

    private final DSLContext               jooqPublic;
    private final TableField<R, Timestamp> lastSynchedField;
    private final DateTimeService          dateTimeService;
    private final int                      graceTimeInMinutes;
    private final String                   entityName;

    /**
     * @param jooqPublic
     *     the DSL context for scipamatoPublic
     * @param lastSynchedField
     *     the the last synched table field
     * @param dateTimeService
     *     the service providing the current date and time
     * @param graceTimeInMinutes
     *     records that were touched during synchronization within that many
     *     minutes will be excluded from purging
     * @param entityName
     *     the name of the managed entity
     */
    public HouseKeeper(final DSLContext jooqPublic, final TableField<R, Timestamp> lastSynchedField,
        final DateTimeService dateTimeService, final int graceTimeInMinutes, final String entityName) {
        this.jooqPublic = AssertAs.notNull(jooqPublic, "jooqPublic");
        this.lastSynchedField = AssertAs.notNull(lastSynchedField, "lastSynchedField");
        this.dateTimeService = AssertAs.notNull(dateTimeService, "dateTimeService");
        this.graceTimeInMinutes = graceTimeInMinutes;
        this.entityName = AssertAs.notNull(entityName, "entityName");
    }

    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) {
        final Timestamp cutOff = Timestamp.valueOf(dateTimeService
            .getCurrentDateTime()
            .minusMinutes(graceTimeInMinutes));
        final int result = jooqPublic
            .deleteFrom(lastSynchedField.getTable())
            .where(lastSynchedField.lessThan(cutOff))
            .execute();
        if (result > 0)
            log.info("Successfully deleted {} {}{} in scipamato-public (not touched since {})", result, entityName,
                (result > 1 ? "es" : ""), cutOff);
        return RepeatStatus.FINISHED;
    }

}
