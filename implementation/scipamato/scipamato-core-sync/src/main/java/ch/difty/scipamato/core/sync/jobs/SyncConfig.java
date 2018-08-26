package ch.difty.scipamato.core.sync.jobs;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

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
import ch.difty.scipamato.core.sync.houskeeping.HouseKeeper;

/**
 * Common abstract base class for Sync Configs
 *
 * @param <T>
 *     type of sync classes
 * @param <R>
 *     related record implementation
 * @author u.joss
 */
public abstract class SyncConfig<T, R extends UpdatableRecordImpl<R>> {

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
        final Timestamp cutOff = Timestamp.valueOf(getDateTimeService()
            .getCurrentDateTime()
            .minusMinutes(graceTime));
        return stepBuilderFactory
            .get(topic + "PurgingStep")
            .tasklet(new HouseKeeper<>(getPurgeDcs(cutOff), graceTime, topic))
            .build();
    }

    protected abstract DeleteConditionStep<R> getPurgeDcs(final Timestamp cutOff);

    protected String getString(final TableField<?, String> field, final ResultSet rs) throws SQLException {
        return rs.getString(field.getName());
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
