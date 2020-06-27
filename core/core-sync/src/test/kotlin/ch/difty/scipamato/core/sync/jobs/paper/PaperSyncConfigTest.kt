package ch.difty.scipamato.core.sync.jobs.paper

import ch.difty.scipamato.common.FrozenDateTimeService
import ch.difty.scipamato.core.db.Tables.CODE
import ch.difty.scipamato.core.sync.code.CodeAggregator
import io.mockk.every
import io.mockk.mockk
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldBeNull
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.jooq.tools.jdbc.MockConnection
import org.jooq.tools.jdbc.MockDataProvider
import org.jooq.tools.jdbc.MockResult
import org.junit.jupiter.api.Test
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import java.sql.ResultSet
import java.sql.Timestamp
import javax.sql.DataSource

internal class PaperSyncConfigTest {

    private val codeAggregator = mockk<CodeAggregator>(relaxed = true) {
        every { codesPopulation } returns arrayOf(3, 4)
        every { codesStudyDesign } returns arrayOf(5, 6)
        every { aggregatedCodes } returns arrayOf("myaggregatedcodes")
    }
    private val provider = mockk<MockDataProvider> {
        every { execute(any()) } returns arrayOf(MockResult(1, DSL.using(SQLDialect.POSTGRES).newResult(CODE.CODE_)))
    }
    private val connection = MockConnection(provider)
    private val jooqCore: DSLContext = DSL.using(connection, SQLDialect.POSTGRES)
    private val jooqPublic: DSLContext = DSL.using(connection, SQLDialect.POSTGRES)
    private val coreDataSource = mockk<DataSource>()
    private val jobBuilderFactory = mockk<JobBuilderFactory>()
    private val stepBuilderFactory = mockk<StepBuilderFactory>()
    private val dateTimeService = FrozenDateTimeService()
    private val concatenator = mockk<SyncShortFieldConcatenator> {
        every { methodsFrom(any()) } returns ("mymethodsconcat")
        every { populationFrom(any()) } returns ("mypopulationconcat")
        every { resultFrom(any()) } returns ("myresultconcat")
    }

    private val config = PaperSyncConfig(codeAggregator, jooqCore, jooqPublic, coreDataSource, jobBuilderFactory, stepBuilderFactory, dateTimeService, concatenator)

    @Test
    fun jobName() {
        config.jobName shouldBeEqualTo "syncPaperJob"
    }

    @Test
    fun publicWriter() {
        config.publicWriter() shouldBeInstanceOf PaperItemWriter::class
    }

    @Test
    fun lastSynchedField() {
        config.lastSynchedField().toString() shouldBeEqualTo """"public"."paper"."last_synched""""
    }

    @Test
    fun makingEntity() {
        val mockArray = mockk<java.sql.Array> {
            every { array } returns arrayOf("mycodes")
        }
        val resultSet = mockk<ResultSet> {
            every { getLong("id") } returns 1
            every { getLong("number") } returns 2
            every { getInt("pm_id") } returns 3
            every { getString("authors") } returns "myauthors"
            every { getString("title") } returns "mytitle"
            every { getString("location") } returns "mylocation"
            every { getInt("publication_year") } returns 4
            every { getString("goals") } returns "mygoals"
            every { getString("comment") } returns "mycomment"
            every { getArray("codes") } returns mockArray
            every { getInt("version") } returns 5
            every { getTimestamp("created") } returns Timestamp.valueOf("2020-04-04 12:13:14")
            every { getTimestamp("last_modified") } returns Timestamp.valueOf("2020-04-04 13:14:15")
            every { wasNull() } returns false
        }

        val paper = config.makeEntity(resultSet)

        paper.id shouldBeEqualTo 1
        paper.number shouldBeEqualTo 2
        paper.pmId shouldBeEqualTo 3
        paper.authors shouldBeEqualTo "myauthors"
        paper.title shouldBeEqualTo "mytitle"
        paper.location shouldBeEqualTo "mylocation"
        paper.publicationYear shouldBeEqualTo 4
        paper.goals shouldBeEqualTo "mygoals"
        paper.methods shouldBeEqualTo "mymethodsconcat"
        paper.population shouldBeEqualTo "mypopulationconcat"
        paper.result shouldBeEqualTo "myresultconcat"
        paper.comment shouldBeEqualTo "mycomment"
        paper.codes.joinToString(",") shouldBeEqualTo "myaggregatedcodes"
        paper.version shouldBeEqualTo 5
        paper.created.toString() shouldBeEqualTo "2020-04-04 12:13:14.0"
        paper.lastModified.toString() shouldBeEqualTo "2020-04-04 13:14:15.0"
        paper.lastSynched.toString() shouldBeEqualTo "2016-12-09 06:02:13.0"
    }

    @Test
    fun selectSql() {
        config.selectSql() shouldBeEqualTo
            """select "public"."paper"."id", "public"."paper"."number", "public"."paper"."pm_id", "public"."paper"."authors", "public"."paper"."title", "public"."paper"."location", "public"."paper"."publication_year", "public"."paper"."goals", "public"."paper"."methods", "public"."paper"."population", "public"."paper"."result", "public"."paper"."comment", "public"."paper"."version", "public"."paper"."created", "public"."paper"."last_modified", array_agg("public"."paper_code"."code") as "codes", "public"."paper"."method_study_design", "public"."paper"."method_outcome", "public"."paper"."exposure_pollutant", "public"."paper"."exposure_assessment", "public"."paper"."method_statistics", "public"."paper"."method_confounders", "public"."paper"."population_place", "public"."paper"."population_participants", "public"."paper"."population_duration", "public"."paper"."result_exposure_range", "public"."paper"."result_effect_estimate", "public"."paper"."result_measured_outcome", "public"."paper"."conclusion" from "public"."paper" join "public"."paper_code" on "public"."paper"."id" = "public"."paper_code"."paper_id" join "public"."code" on "public"."paper_code"."code" = "public"."code"."code" group by "public"."paper"."id", "public"."paper"."number", "public"."paper"."pm_id", "public"."paper"."authors", "public"."paper"."title", "public"."paper"."location", "public"."paper"."publication_year", "public"."paper"."goals", "public"."paper"."methods", "public"."paper"."population", "public"."paper"."result", "public"."paper"."comment", "public"."paper"."version", "public"."paper"."created", "public"."paper"."last_modified", "public"."paper"."method_study_design", "public"."paper"."method_outcome", "public"."paper"."exposure_pollutant", "public"."paper"."exposure_assessment", "public"."paper"."method_statistics", "public"."paper"."method_confounders", "public"."paper"."population_place", "public"."paper"."population_participants", "public"."paper"."population_duration", "public"."paper"."result_exposure_range", "public"."paper"."result_effect_estimate", "public"."paper"."result_measured_outcome", "public"."paper"."conclusion""""
    }

    @Test
    fun pseudoFkDcs() {
        config.pseudoFkDcs.shouldBeNull()
    }
}
