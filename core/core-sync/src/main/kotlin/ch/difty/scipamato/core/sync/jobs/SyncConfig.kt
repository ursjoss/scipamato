package ch.difty.scipamato.core.sync.jobs

import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.common.entity.newsletter.PublicationStatus
import ch.difty.scipamato.core.sync.houskeeping.HouseKeeper
import ch.difty.scipamato.core.sync.houskeeping.PseudoForeignKeyConstraintEnforcer
import org.jooq.DSLContext
import org.jooq.DeleteConditionStep
import org.jooq.TableField
import org.jooq.impl.UpdatableRecordImpl
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JdbcCursorItemReader
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import java.sql.Date
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Timestamp
import javax.sql.DataSource

/**
 * Common abstract base class for Sync Configs
 *
 * [T] type of sync classes
 * [R] related record implementation
 */
@Suppress("LongParameterList", "TooManyFunctions")
abstract class SyncConfig<T, R : UpdatableRecordImpl<R>?>(
    private val topic: String,
    private val chunkSize: Int,
    @Qualifier("dslContext") protected val jooqCore: DSLContext,
    @Qualifier("publicDslContext") protected val jooqPublic: DSLContext,
    @Qualifier("dataSource") private val coreDataSource: DataSource,
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val dateTimeService: DateTimeService
) {

    @Value("\${purge_grace_time_in_minutes:30}")
    private val graceTime = 0

    fun createJob(): Job =
        jobBuilderFactory[jobName]
            .incrementer(RunIdIncrementer())
            .flow(insertingOrUpdatingStep())
            .next(purgingStep())
            .next(pseudoForeignKeyConstraintStep())
            .end()
            .build()

    /**
     * Return name of the synchronization job
     */
    abstract val jobName: String

    private fun insertingOrUpdatingStep(): Step =
        stepBuilderFactory["${topic}InsertingOrUpdatingStep"].chunk<T, T>(chunkSize)
            .reader(coreReader())
            .writer(publicWriter())
            .build()

    /**
     * Return implementation of the [ItemWriter] interface to insert/update type [T]
     */
    abstract fun publicWriter(): ItemWriter<T>

    private fun coreReader(): ItemReader<out T> =
        JdbcCursorItemReader<T>().apply {
            dataSource = coreDataSource
            sql = selectSql()
            setRowMapper { rs: ResultSet, _: Int -> makeEntity(rs) }
        }

    /**
     * Returns SQL string to fetch the records from scipamato-core
     */
    abstract fun selectSql(): String

    /**
     * Translate the [ResultSet] [rs] into the entity [T]
     *
     * @throws SQLException in case the recordset cannot be evaluated properly
     */
    @Throws(SQLException::class)
    abstract fun makeEntity(rs: ResultSet): T

    private fun purgingStep(): Step =
        stepBuilderFactory[topic + "PurgingStep"]
            .tasklet(HouseKeeper(jooqPublic, lastSynchedField(), dateTimeService, graceTime, topic))
            .build()

    abstract fun lastSynchedField(): TableField<R, Timestamp>

    private fun pseudoForeignKeyConstraintStep(): Step =
        stepBuilderFactory[topic + "PseudoForeignKeyStep"]
        .tasklet(PseudoForeignKeyConstraintEnforcer(pseudoFkDcs, topic, plural))
        .build()

    private val plural: String
        get() = "s"

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
     * no-op operation in the tasklet running the task.
     */
    open val pseudoFkDcs: DeleteConditionStep<R>?
        get() = null

    @Throws(SQLException::class)
    fun getString(field: TableField<*, String?>, rs: ResultSet): String? = rs.getString(field.name)

    @Throws(SQLException::class)
    fun getBoolean(field: TableField<*, Boolean?>, rs: ResultSet): Boolean = rs.getBoolean(field.name)

    /**
     * @param [field] the integer field to get the value from
     * @param [rs] the [ResultSet] providing the values
     * Returns `null` if the column was null, the boxed integer value otherwise
     * @throws [SQLException] if the columnLabel is not valid;
     *         if a database access error occurs or this method is called on a closed result set
     */
    @Throws(SQLException::class)
    fun getInteger(field: TableField<*, Int?>, rs: ResultSet): Int? {
        val value = rs.getInt(field.name)
        return if (rs.wasNull()) null else value
    }

    /**
     * @param [field] the long field to get the value from
     * @param [rs] the [ResultSet] providing the values
     * Returns `null` if the column was null, the boxed long value otherwise
     * @throws SQLException if the columnLabel is not valid;
     *         if a database access error occurs or this method is called on a closed result set
     */
    @Throws(SQLException::class)
    fun getLong(field: TableField<*, Long?>, rs: ResultSet): Long? {
        val value = rs.getLong(field.name)
        return if (rs.wasNull()) null else value
    }

    @Throws(SQLException::class)
    fun getTimestamp(field: TableField<*, Timestamp?>, rs: ResultSet): Timestamp? = rs.getTimestamp(field.name)

    @Throws(SQLException::class)
    fun getTimestamp(alias: String, rs: ResultSet): Timestamp? = rs.getTimestamp(alias)

    @Throws(SQLException::class)
    fun getDate(field: TableField<*, Date?>, rs: ResultSet): Date? = rs.getDate(field.name)

    fun getNow(): Timestamp = dateTimeService.currentTimestamp

    companion object {
        val PUBLICATION_STATUS_PUBLISHED = PublicationStatus.PUBLISHED.id
    }
}
