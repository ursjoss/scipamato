package ch.difty.scipamato.core.sync.houskeeping

import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.common.logger
import lombok.extern.slf4j.Slf4j
import org.jooq.DSLContext
import org.jooq.TableField
import org.jooq.impl.UpdatableRecordImpl
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import java.sql.Timestamp

private val log = logger()

/**
 * [Tasklet] used for housekeeping, i.e. purging records that once were
 * present in scipamato-core and were synchronized to scipamato-public in the past
 * but now are not present anymore in scipamato-core.
 *
 * The synchronization step that inserts/updates records from core to public
 * touches the lastSynchronized timestamp of each record present in scipamato-core.
 * Hence, any record in scipamato-public that has no updated timestamp after the first
 * synchronization step must be missing in core and is eligible for purging.
 *
 * A grace time (in minutes) is applied before purging, i.e. any record which
 * has not been touched within X minutes will be purged. The default value is 30
 * minutes but is configurable (purge_grace_time_in_minutes).
 *
 * @param [jooqPublic] the DSL context for scipamatoPublic
 * @param [lastSynchedField] the the last synched table field
 * @param [dateTimeService] the service providing the current date and time
 * @param [graceTimeInMinutes] records that were touched during synchronization within that many
 * minutes will be excluded from purging
 * @param [entityName] the name of the managed entity
 * @param <R> type of [UpdatableRecordImpl] - the record to purge.
 */
@Slf4j
class HouseKeeper<R : UpdatableRecordImpl<*>?>(
    private val jooqPublic: DSLContext,
    private val lastSynchedField: TableField<R, Timestamp>,
    private val dateTimeService: DateTimeService,
    private val graceTimeInMinutes: Int,
    private val entityName: String
) : Tasklet {

    override fun execute(
        contribution: StepContribution,
        chunkContext: ChunkContext
    ): RepeatStatus {
        val cutOff = Timestamp.valueOf(dateTimeService.currentDateTime.minusMinutes(graceTimeInMinutes.toLong()))

        val result = jooqPublic
            .deleteFrom(lastSynchedField.table)
            .where(lastSynchedField.lessThan(cutOff))
            .execute()

        if (result > 0)
            log.info {
                "Successfully deleted $result $entityName${if (result > 1) "es" else ""} " +
                    "in scipamato-public (not touched since $cutOff)"
            }

        return RepeatStatus.FINISHED
    }
}
