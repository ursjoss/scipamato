package ch.difty.scipamato.core.sync.jobs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.sql.DataSource;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;

import ch.difty.scipamato.core.sync.houskeeping.HouseKeeper;

/**
 * Common abstract base class for Sync Configs
 *
 * @author u.joss
 *
 * @param <T> type of sync classes
 * @param <R> related record implementation
 */
public abstract class SyncConfig<T, R extends UpdatableRecordImpl<R>> {

    @Value("${purge_grace_time_in_minutes:30}")
    private int graceTime;

    @Autowired
    @Qualifier("coreDslContext")
    private DSLContext jooqCore;

    @Autowired
    @Qualifier("publicDslContext")
    private DSLContext jooqPublic;

    @Autowired
    @Qualifier("scipamatoCoreDataSource")
    private DataSource scipamatoCoreDataSource;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    private final String topic;
    private final int chunkSize;

    protected SyncConfig(final String topic, final int chunkSize) {
        this.topic = topic;
        this.chunkSize = chunkSize;
    }

    protected StepBuilderFactory getStepBuilderFactory() {
        return stepBuilderFactory;
    }

    protected DSLContext getJooqCore() {
        return jooqCore;
    }

    protected DSLContext getJooqPublic() {
        return jooqPublic;
    }

    protected final Job createJob() {
        // @formatter:off
        return jobBuilderFactory
            .get(getJobName()).incrementer(new RunIdIncrementer())
                .flow(insertingOrUpdatingStep())
                .next(purgingStep())
            .end()
            .build();
        // @formatter:on
    }

    /**
     * @return name of the synchronization job
     */
    protected abstract String getJobName();

    private Step insertingOrUpdatingStep() {
        // @formatter:off
        return getStepBuilderFactory()
            .get(topic + "InsertingOrUpdatingStep")
            .<T, T> chunk(chunkSize)
                .reader(coreReader())
                .writer(publicWriter())
            .build();
        // @formatter:on
    }

    /**
     * @return implementation of the {@link ItemWriter} interface to insert/update type {@literal T}
     */
    protected abstract ItemWriter<? super T> publicWriter();

    private ItemReader<? extends T> coreReader() {
        final JdbcCursorItemReader<T> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(scipamatoCoreDataSource);
        reader.setSql(selectSql());
        reader.setRowMapper(new RowMapper<T>() {
            @Override
            public T mapRow(final ResultSet rs, final int rowNum) throws SQLException {
                return makeEntity(rs);
            }
        });
        return reader;
    }

    /**
     * @return SQL string to fetch the records from scipamato-core
     */
    protected abstract String selectSql();

    /**
     * Translate the {@link ResultSet} into the entity {@literal T}
     * @param rs
     *    the recordset from scipamato-core
     * @return the entity of type {@link T}
     * @throws SQLException
     */
    protected abstract T makeEntity(ResultSet rs) throws SQLException;

    private Step purgingStep() {
        final Timestamp cutOff = Timestamp.valueOf(LocalDateTime.now().minusMinutes(graceTime));
        return stepBuilderFactory.get(topic + "PurgingStep").tasklet(new HouseKeeper<R>(getPurgeDcs(cutOff), graceTime)).build();
    }

    /**
     * 
     * @param cutOff
     * @return
     */
    protected abstract DeleteConditionStep<R> getPurgeDcs(final Timestamp cutOff);

    protected String getString(final TableField<?, String> field, final ResultSet rs) throws SQLException {
        return rs.getString(field.getName());
    }

    protected Integer getInt(final TableField<?, Integer> field, final ResultSet rs) throws SQLException {
        return rs.getInt(field.getName());
    }

    protected Long getLong(final TableField<?, Long> field, final ResultSet rs) throws SQLException {
        return rs.getLong(field.getName());
    }

    protected Timestamp getTimestamp(final TableField<?, Timestamp> field, final ResultSet rs) throws SQLException {
        return rs.getTimestamp(field.getName());
    }

    protected Timestamp getNow() {
        return Timestamp.valueOf(LocalDateTime.now());
    }

}
