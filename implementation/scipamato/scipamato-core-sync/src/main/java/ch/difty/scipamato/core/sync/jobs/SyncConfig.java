package ch.difty.scipamato.core.sync.jobs;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.jooq.DSLContext;
import org.jooq.DeleteConditionStep;
import org.jooq.DeleteWhereStep;
import org.jooq.TableField;
import org.jooq.impl.UpdatableRecordImpl;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.core.sync.houskeeping.HouseKeeper;
import ch.difty.scipamato.core.sync.houskeeping.PseudoForeignKeyConstraintEnforcer;

/**
 * Common abstract base class for Sync Configs
 *
 * @param <T>
 *     type of sync classes
 * @param <R>
 *     related record implementation
 * @author u.joss
 */
@SuppressWarnings({ "SameParameterValue", "WeakerAccess" })
public abstract class SyncConfig<T, R extends UpdatableRecordImpl<R>> {

    // has to be in sync with PublicationStatus TODO place PublicationStatus into scipamato-common-utils and reference here
    protected static final Integer PUBLICATION_STATUS_PUBLISHED = 1;

    @Value("${purge_grace_time_in_minutes:30}")
    private int graceTime;

    private final DSLContext         jooqCore;
    private final DSLContext         jooqPublic;
    private final DataSource         coreDataSource;
    private final JobBuilderFactory  jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DateTimeService    dateTimeService;

    private final String topic;
    private final int    chunkSize;

    protected SyncConfig(final String topic, final int chunkSize, @Qualifier("dslContext") DSLContext jooqCore,
        @Qualifier("publicDslContext") DSLContext jooqPublic, @Qualifier("dataSource") DataSource coreDataSource,
        JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, DateTimeService dateTimeService) {
        this.topic = topic;
        this.chunkSize = chunkSize;
        this.jooqCore = jooqCore;
        this.jooqPublic = jooqPublic;
        this.coreDataSource = coreDataSource;
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.dateTimeService = dateTimeService;
    }

    private StepBuilderFactory getStepBuilderFactory() {
        return stepBuilderFactory;
    }

    protected DSLContext getJooqCore() {
        return jooqCore;
    }

    protected DSLContext getJooqPublic() {
        return jooqPublic;
    }

    private DateTimeService getDateTimeService() {
        return dateTimeService;
    }

    protected final Job createJob() {
        return jobBuilderFactory
            .get(getJobName())
            .incrementer(new RunIdIncrementer())
            .flow(insertingOrUpdatingStep())
            .next(purgingStep())
            .next(pseudoForeignKeyConstraintStep())
            .end()
            .build();
    }

    /**
     * @return name of the synchronization job
     */
    protected abstract String getJobName();

    private Step insertingOrUpdatingStep() {
        return getStepBuilderFactory().get(topic + "InsertingOrUpdatingStep").<T, T>chunk(chunkSize)
            .reader(coreReader())
            .writer(publicWriter())
            .build();
    }

    /**
     * @return implementation of the {@link ItemWriter} interface to insert/update
     *     type {@literal T}
     */
    protected abstract ItemWriter<T> publicWriter();

    private ItemReader<? extends T> coreReader() {
        final JdbcCursorItemReader<T> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(coreDataSource);
        reader.setSql(selectSql());
        reader.setRowMapper((rs, rowNum) -> makeEntity(rs));
        return reader;
    }

    /**
     * @return SQL string to fetch the records from scipamato-core
     */
    protected abstract String selectSql();

    /**
     * Translate the {@link ResultSet} into the entity {@literal T}
     *
     * @param rs
     *     the recordset from scipamato-core
     * @return the entity of type {@literal T}
     * @throws SQLException
     *     in case the recordset cannot be evaluated properly
     */
    protected abstract T makeEntity(ResultSet rs) throws SQLException;

    private Step purgingStep() {
        return stepBuilderFactory
            .get(topic + "PurgingStep")
            .tasklet(
                new HouseKeeper<>(getDeleteWhereStep(), lastSynchedField(), getDateTimeService(), graceTime, topic))
            .build();
    }

    protected abstract DeleteWhereStep<R> getDeleteWhereStep();

    protected abstract TableField<R, Timestamp> lastSynchedField();

    private Step pseudoForeignKeyConstraintStep() {
        return stepBuilderFactory
            .get(topic + "PseudoForeignKeyStep")
            .tasklet(new PseudoForeignKeyConstraintEnforcer<>(getPseudoFkDcs(), topic, getPlural()))
            .build();
    }

    protected String getPlural() {
        return "s";
    }

    /**
     * Override if some pseudo-foreign key constraint needs to be implemented.
     * In SciPaMaTo-Public, we have some tables that purpously lack proper normalisation, which has the
     * benefit of simplicity but the disadvantage that normal referential integrity rules on db level do
     * not apply. E.g. code classes have the same code_class_id (from scipamato-core) more than once, each
     * with a different language flag, once for each language. Therefore code_class_id in table code_class
     * is not a unique id but only part of a composite key. It's not possible to use code_class_id as foreign
     * key on db_level with a delete cascade property. We therefore may need to explicitly delete codes that
     * belong to a code class that does not exist anymore in table code_class. That's what I here call a
     * pseudo-foreign key constraint.
     *
     * @return the deleteConditionStep or null if no such enforcement is required. The latter will result in a
     *     no-op operation in the tasklet running the task.
     */
    public DeleteConditionStep<R> getPseudoFkDcs() {
        return null;
    }

    protected String getString(final TableField<?, String> field, final ResultSet rs) throws SQLException {
        return rs.getString(field.getName());
    }

    protected Boolean getBoolean(final TableField<?, Boolean> field, final ResultSet rs) throws SQLException {
        return rs.getBoolean(field.getName());
    }

    /**
     * @param field
     *     the integer field to get the value from
     * @param rs
     *     the resultset providing the values
     * @return returns null if the column was null, the boxed integer value
     *     otherwise
     * @throws SQLException
     *     if the columnLabel is not valid; if a database access error occurs or this method is called on a closed result set
     */
    protected Integer getInteger(final TableField<?, Integer> field, final ResultSet rs) throws SQLException {
        final int val = rs.getInt(field.getName());
        return rs.wasNull() ? null : val;
    }

    /**
     * @param field
     *     the long field to get the value from
     * @param rs
     *     the resultset providing the values
     * @return returns null if the column was null, the boxed long value otherwise
     * @throws SQLException
     *     if the columnLabel is not valid; if a database access error occurs or this method is called on a closed result set
     */
    protected Long getLong(final TableField<?, Long> field, final ResultSet rs) throws SQLException {
        final long val = rs.getLong(field.getName());
        return rs.wasNull() ? null : val;
    }

    protected Timestamp getTimestamp(final TableField<?, Timestamp> field, final ResultSet rs) throws SQLException {
        return rs.getTimestamp(field.getName());
    }

    protected Date getDate(final TableField<?, Date> field, final ResultSet rs) throws SQLException {
        return rs.getDate(field.getName());
    }

    protected Timestamp getNow() {
        return getDateTimeService().getCurrentTimestamp();
    }

}
