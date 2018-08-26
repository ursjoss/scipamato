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
 * {@link Tasklet} used for enforcing pseudo-referential integrity rules. This is necessary
 * since not all SciPaMaTo-Public tables fully normalized, as they simply replicate some of the data
 * that is properly managed within SciPaMaTo-Core (and enforced with proper database referential integrity there).
 * <p>
 * As an example, in SciPaMaTo-Public, the code_class_id column in table code_class is not unique. It is only part
 * of a composite key, together with the languageCode, i.e. there is one translation in each defined language for every
 * logical code class maintained in scipamato-core.
 * <p>
 * With this, we cannot use code_class_id in table code as a foreign key to code_class.code_class_id. Therefore we
 * cannot define a delete cascade rule, as it is not a proper foreign key with a unique constraint. In case a code_class
 * is deleted in scipamato-core, we cannot rely on the database to cascade the delete to any code in scipamato-public
 * that references the code class that is deleted.
 * <p>
 * This tasklet allows to define a delete statement that does just that, where this is appropriate. If a null delete
 * condition step is passed into the constructor of this tasklet, the tasklet will operate as a no-op tasklet, simply
 * doing nothing when #execute is called.
 *
 * @param <R>
 *     type of {@link UpdatableRecordImpl} - the record to purge.
 * @author u.joss
 */
@Slf4j
public class PseudoForeignKeyConstraintEnforcer<R extends UpdatableRecordImpl<?>> implements Tasklet {

    private final DeleteConditionStep<R> ddl;
    private final String                 entityName;
    private final String                 plural;

    /**
     * @param deleteDdl
     *     executable jooq delete condition step to delete the records of
     *     type {@literal T}
     * @param entityName
     *     the name of the managed entity (e.g. 'code')
     * @param plural
     *     the suffix indicating more than one of the entities (e.g. 's' -> 'code' + 's' = 'codes')
     */
    public PseudoForeignKeyConstraintEnforcer(final DeleteConditionStep<R> deleteDdl, final String entityName,
        final String plural) {
        this.ddl = deleteDdl;
        this.entityName = AssertAs.notNull(entityName, "entityName");
        this.plural = plural != null ? plural : "s";
    }

    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) {
        if (ddl != null) {
            final int result = ddl.execute();
            if (result > 0)
                log.info(
                    "Successfully deleted {} {}{} in scipamato-public due to pseudo referential integrity constraints",
                    result, entityName, (result > 1 ? plural : ""));
        }
        return RepeatStatus.FINISHED;
    }

}
