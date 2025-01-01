package ch.difty.scipamato.core.sync.jobs.paper


import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.core.db.tables.Code
import ch.difty.scipamato.core.db.tables.Paper
import ch.difty.scipamato.core.db.tables.PaperCode
import ch.difty.scipamato.core.sync.PublicPaper
import ch.difty.scipamato.core.sync.PublicPaperTable
import ch.difty.scipamato.core.sync.code.CodeAggregator
import ch.difty.scipamato.core.sync.jobs.SyncConfig
import ch.difty.scipamato.core.sync.newPublicPaper
import ch.difty.scipamato.publ.db.tables.records.PaperRecord
import org.jooq.DSLContext
import org.jooq.TableField
import org.jooq.impl.DSL
import org.springframework.batch.core.Job
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.item.ItemWriter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.transaction.PlatformTransactionManager
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Timestamp
import javax.sql.DataSource

/**
 * Defines the paper synchronization job, applying two steps:
 *
 *  1. insertingOrUpdating: inserts new records or updates if already present
 *  1. purging: removes records that have not been touched by the first step
 * (within a defined grace time in minutes)
 *
 * @author u.joss
 */
@Configuration
@Profile("!wickettest")
@Suppress("LongParameterList")
open class PaperSyncConfig(
    private val codeAggregator: CodeAggregator,
    @Qualifier("dslContext") jooqCore: DSLContext,
    @Qualifier("publicDslContext") jooqPublic: DSLContext,
    @Qualifier("dataSource") coreDataSource: DataSource,
    jobRepository: JobRepository,
    transactionManager: PlatformTransactionManager,
    dateTimeService: DateTimeService,
    private val shortFieldConcatenator: SyncShortFieldConcatenator,
) : SyncConfig<PublicPaper, PaperRecord>(TOPIC,
    CHUNK_SIZE,
    jooqCore,
    jooqPublic,
    coreDataSource,
    jobRepository,
    transactionManager,
    dateTimeService) {

    init {
        setInternalCodes()
    }

    private fun setInternalCodes() {
        codeAggregator.setInternalCodes(fetchInternalCodesFromDb())
    }

    private fun fetchInternalCodesFromDb(): List<String> =
        jooqCore
            .select()
            .from(Code.CODE)
            .where(Code.CODE.INTERNAL.isTrue)
            .fetch(Code.CODE.CODE_)

    @Bean
    open fun syncPaperJob(): Job = createJob()

    override val jobName: String
        get() = "syncPaperJob"

    override fun publicWriter(): ItemWriter<PublicPaper> = PaperItemWriter(jooqPublic)

    override fun selectSql(): String =
        jooqCore
            .select(C_ID, C_NUMBER, C_PM_ID, C_AUTHORS, C_TITLE, C_LOCATION, C_PUB_YEAR, C_GOALS, C_METHODS,
                C_POPULATION, C_RESULT, C_COMMENT, C_VERSION, C_CREATED, C_LAST_MODIFIED, DSL
                .arrayAgg(PaperCode.PAPER_CODE.CODE)
                .`as`(ALIAS_CODES), C_METHOD_STUDY_DESIGN, C_METHOD_OUTCOME, C_EXPOSURE_POLLUTANT,
                C_EXPOSURE_ASSESSMENT, C_METHOD_STATISTICS, C_METHOD_CONFOUNDERS, C_POPULATION_PLACE,
                C_POPULATION_PARTICIPANTS, C_POPULATION_DURATION, C_RESULT_EXPOSURE_RANGE, C_RESULT_EFFECT_ESTIMATE,
                C_RESULT_MEASURED_OUTCOME, C_CONCLUSION)
            .from(Paper.PAPER)
            .innerJoin(PaperCode.PAPER_CODE)
            .on(Paper.PAPER.ID.eq(PaperCode.PAPER_CODE.PAPER_ID))
            .innerJoin(Code.CODE)
            .on(PaperCode.PAPER_CODE.CODE.eq(Code.CODE.CODE_))
            .groupBy(C_ID, C_NUMBER, C_PM_ID, C_AUTHORS, C_TITLE, C_LOCATION, C_PUB_YEAR, C_GOALS, C_METHODS,
                C_POPULATION, C_RESULT, C_COMMENT, C_VERSION, C_CREATED, C_LAST_MODIFIED, C_METHOD_STUDY_DESIGN,
                C_METHOD_OUTCOME, C_EXPOSURE_POLLUTANT, C_EXPOSURE_ASSESSMENT, C_METHOD_STATISTICS,
                C_METHOD_CONFOUNDERS, C_POPULATION_PLACE, C_POPULATION_PARTICIPANTS, C_POPULATION_DURATION,
                C_RESULT_EXPOSURE_RANGE, C_RESULT_EFFECT_ESTIMATE, C_RESULT_MEASURED_OUTCOME, C_CONCLUSION)
            .sql

    @Throws(SQLException::class)
    override fun makeEntity(rs: ResultSet): PublicPaper {

        val paper: PublicPaper = newPublicPaper(
            id = getLong(C_ID, rs),
            number = getLong(C_NUMBER, rs),
            pmId = getInteger(C_PM_ID, rs),
            authors = getString(C_AUTHORS, rs),
            title = getString(C_TITLE, rs),
            location = getString(C_LOCATION, rs),
            publicationYear = getInteger(C_PUB_YEAR, rs),
            goals = getString(C_GOALS, rs),
            methods = shortFieldConcatenator.methodsFrom(rs),
            population = shortFieldConcatenator.populationFrom(rs),
            result = shortFieldConcatenator.resultFrom(rs),
            comment = getString(C_COMMENT, rs),
            version = getInteger(C_VERSION, rs),
            created = getTimestamp(C_CREATED, rs),
            lastModified = getTimestamp(C_LAST_MODIFIED, rs),
            codesPopulation = null,
            codesStudyDesign = null,
            codes = extractCodes(ALIAS_CODES, rs),
            lastSynched = getNow(),
        )
        codeAggregator.load(paper.codes)
        paper.codesPopulation = codeAggregator.codesPopulation
        paper.codesStudyDesign = codeAggregator.codesStudyDesign
        paper.codes = codeAggregator.aggregatedCodes
        return paper
    }

    @Suppress("SameParameterValue", "UNCHECKED_CAST")
    @Throws(SQLException::class)
    private fun extractCodes(alias: String, rs: ResultSet): Array<String> = rs
        .getArray(alias)
        .array as Array<String>

    override fun lastSynchedField(): TableField<PaperRecord, Timestamp> = PublicPaperTable.PAPER.LAST_SYNCHED

    companion object {
        private const val TOPIC = "paper"
        private const val CHUNK_SIZE = 500
        private const val ALIAS_CODES = "codes"

        // relevant fields of the core Paper record
        private val C_ID = Paper.PAPER.ID
        private val C_NUMBER = Paper.PAPER.NUMBER
        private val C_PM_ID = Paper.PAPER.PM_ID
        private val C_AUTHORS = Paper.PAPER.AUTHORS
        private val C_TITLE = Paper.PAPER.TITLE
        private val C_LOCATION = Paper.PAPER.LOCATION
        private val C_PUB_YEAR = Paper.PAPER.PUBLICATION_YEAR
        private val C_GOALS = Paper.PAPER.GOALS
        private val C_METHODS = Paper.PAPER.METHODS
        private val C_POPULATION = Paper.PAPER.POPULATION
        private val C_RESULT = Paper.PAPER.RESULT
        private val C_COMMENT = Paper.PAPER.COMMENT
        private val C_VERSION = Paper.PAPER.VERSION
        private val C_CREATED = Paper.PAPER.CREATED
        private val C_LAST_MODIFIED = Paper.PAPER.LAST_MODIFIED

        // short fields (Kurzerfassung)
        private val C_METHOD_STUDY_DESIGN = Paper.PAPER.METHOD_STUDY_DESIGN
        private val C_METHOD_OUTCOME = Paper.PAPER.METHOD_OUTCOME
        private val C_EXPOSURE_POLLUTANT = Paper.PAPER.EXPOSURE_POLLUTANT
        private val C_EXPOSURE_ASSESSMENT = Paper.PAPER.EXPOSURE_ASSESSMENT
        private val C_METHOD_STATISTICS = Paper.PAPER.METHOD_STATISTICS
        private val C_METHOD_CONFOUNDERS = Paper.PAPER.METHOD_CONFOUNDERS
        private val C_POPULATION_PLACE = Paper.PAPER.POPULATION_PLACE
        private val C_POPULATION_PARTICIPANTS = Paper.PAPER.POPULATION_PARTICIPANTS
        private val C_POPULATION_DURATION = Paper.PAPER.POPULATION_DURATION
        private val C_RESULT_EXPOSURE_RANGE = Paper.PAPER.RESULT_EXPOSURE_RANGE
        private val C_RESULT_EFFECT_ESTIMATE = Paper.PAPER.RESULT_EFFECT_ESTIMATE
        private val C_RESULT_MEASURED_OUTCOME = Paper.PAPER.RESULT_MEASURED_OUTCOME
        private val C_CONCLUSION = Paper.PAPER.CONCLUSION
    }
}
