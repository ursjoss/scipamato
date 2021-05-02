package ch.difty.scipamato.core.sync.houskeeping

import ch.difty.scipamato.common.logger
import org.jooq.DeleteConditionStep
import org.jooq.impl.UpdatableRecordImpl
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus

private val log = logger()

/**
 * [Tasklet] used for enforcing pseudo-referential integrity rules. This is necessary
 * since not all SciPaMaTo-Public tables fully normalized, as they simply replicate some of the data
 * that is properly managed within SciPaMaTo-Core (and enforced with proper database referential integrity there).
 *
 * As an example, in SciPaMaTo-Public, the code_class_id column in table code_class is not unique. It is only part
 * of a composite key, together with the languageCode, i.e. there is one translation in each defined language for every
 * logical code class maintained in scipamato-core.
 *
 * With this, we cannot use code_class_id in table code as a foreign key to code_class.code_class_id. Therefore we
 * cannot define a delete cascade rule, as it is not a proper foreign key with a unique constraint. In case a code_class
 * is deleted in scipamato-core, we cannot rely on the database to cascade the delete to any code in scipamato-public
 * that references the code class that is deleted.
 *
 * This tasklet allows to define a delete statement that does just that, where this is appropriate. If a null delete
 * condition step is passed into the constructor of this tasklet, the tasklet will operate as a no-op tasklet, simply
 * doing nothing when #execute is called.
 *
 * @param [ddl] executable jooq delete condition step to delete the records of type T
 * @param [entityName] the name of the managed entity (e.g. 'code')
 * @param [plural] the suffix indicating more than one of the entities (e.g. 's' -&gt; 'code' + 's' = 'codes')
 * @param <R> type of [UpdatableRecordImpl] - the record to purge.
 */
class PseudoForeignKeyConstraintEnforcer<R : UpdatableRecordImpl<*>?>(
    private val ddl: DeleteConditionStep<R>?,
    private val entityName: String,
    private val plural: String = "s",
) : Tasklet {

    override fun execute(
        contribution: StepContribution,
        chunkContext: ChunkContext
    ): RepeatStatus {
        ddl?.let {
            val result = ddl.execute()
            if (result > 0) log.info {
                "Successfully deleted $result $entityName${if (result > 1) plural else ""} in scipamato-public " +
                    "due to pseudo referential integrity constraints"
            }
        }
        return RepeatStatus.FINISHED
    }
}
