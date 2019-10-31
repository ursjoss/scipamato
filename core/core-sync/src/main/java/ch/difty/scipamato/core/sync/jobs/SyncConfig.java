package ch.difty.scipamato.core.sync.jobs;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.DSLContext;
import org.jooq.DeleteConditionStep;
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
import ch.difty.scipamato.common.entity.newsletter.PublicationStatus;
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

    protected static final Integer PUBLICATION_STATUS_PUBLISHED = PublicationStatus.PUBLISHED.getId();

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

    protected SyncConfig(@NotNull final String topic, final int chunkSize,
        @Qualifier("dslContext") @NotNull final DSLContext jooqCore,
        @Qualifier("publicDslContext") @NotNull final DSLContext jooqPublic,
        @Qualifier("dataSource") @NotNull final DataSource coreDataSource,
        @NotNull final JobBuilderFactory jobBuilderFactory, @NotNull final StepBuilderFactory stepBuilderFactory,
        @NotNull final DateTimeService dateTimeService) {
        this.topic = topic;
        this.chunkSize = chunkSize;
        this.jooqCore = jooqCore;
        this.jooqPublic = jooqPublic;
        this.coreDataSource = coreDataSource;
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.dateTimeService = dateTimeService;
    }

    @NotNull
    private StepBuilderFactory getStepBuilderFactory() {
        return stepBuilderFactory;
    }

    @NotNull
    protected DSLContext getJooqCore() {
        return jooqCore;
    }

    @NotNull
    protected DSLContext getJooqPublic() {
        return jooqPublic;
    }

    @NotNull
    private DateTimeService getDateTimeService() {
        return dateTimeService;
    }

    @NotNull
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
    @NotNull
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
    @NotNull
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
    @NotNull
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
    @NotNull
    protected abstract T makeEntity(@NotNull ResultSet rs) throws SQLException;

    private Step purgingStep() {
        return stepBuilderFactory
            .get(topic + "PurgingStep")
            .tasklet(new HouseKeeper<>(getJooqPublic(), lastSynchedField(), getDateTimeService(), graceTime, topic))
            .build();
    }

    @NotNull
    protected abstract TableField<R, Timestamp> lastSynchedField();

    private Step pseudoForeignKeyConstraintStep() {
        return stepBuilderFactory
            .get(topic + "PseudoForeignKeyStep")
            .tasklet(new PseudoForeignKeyConstraintEnforcer<>(getPseudoFkDcs(), topic, getPlural()))
            .build();
    }

    @NotNull
    protected String getPlural() {
        return "s";
    }

    /**
     * Override if some pseudo-foreign key constraint needs to be implemented.
     * In SciPaMaTo-Public, we have some tables that purposely lack proper normalisation, which has the
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
    @Nullable
    public DeleteConditionStep<R> getPseudoFkDcs() {
        return null;
    }

    @Nullable
    protected String getString(@NotNull final TableField<?, String> field, @NotNull final ResultSet rs)
        throws SQLException {
        return rs.getString(field.getName());
    }

    @Nullable
    protected Boolean getBoolean(@NotNull final TableField<?, Boolean> field, @NotNull final ResultSet rs)
        throws SQLException {
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
    @Nullable
    protected Integer getInteger(@NotNull final TableField<?, Integer> field, @NotNull final ResultSet rs)
        throws SQLException {
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
    @Nullable
    protected Long getLong(@NotNull final TableField<?, Long> field, @NotNull final ResultSet rs) throws SQLException {
        final long val = rs.getLong(field.getName());
        return rs.wasNull() ? null : val;
    }

    @Nullable
    protected Timestamp getTimestamp(@NotNull final TableField<?, Timestamp> field, @NotNull final ResultSet rs)
        throws SQLException {
        return rs.getTimestamp(field.getName());
    }

    @Nullable
    protected Timestamp getTimestamp(@NotNull final String alias, @NotNull final ResultSet rs) throws SQLException {
        return rs.getTimestamp(alias);
    }

    @Nullable
    protected Date getDate(@NotNull final TableField<?, Date> field, @NotNull final ResultSet rs) throws SQLException {
        return rs.getDate(field.getName());
    }

    @NotNull
    protected Timestamp getNow() {
        return getDateTimeService().getCurrentTimestamp();
    }
}
