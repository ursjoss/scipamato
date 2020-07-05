@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.core.persistence.paper

import ch.difty.scipamato.common.persistence.FilterConditionMapperTest
import ch.difty.scipamato.core.db.tables.Paper
import ch.difty.scipamato.core.db.tables.Paper.PAPER
import ch.difty.scipamato.core.db.tables.records.PaperRecord
import ch.difty.scipamato.core.entity.search.PaperFilter
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

internal class PaperFilterConditionMapperTest : FilterConditionMapperTest<PaperRecord, Paper, PaperFilter>() {

    override val mapper = PaperFilterConditionMapper()

    override val filter = PaperFilter()

    override val table: Paper = PAPER

    @Test
    fun creatingWhereCondition_withNumber_searchesNumber() {
        val number = 17L
        filter.number = number
        mapper.map(filter).toString() shouldBeEqualTo """"public"."paper"."number" = 17"""
    }

    @Test
    fun creatingWhereCondition_withAuthorMask_searchesFirstAuthorAndAuthors() {
        val pattern = "am"
        filter.authorMask = pattern
        mapper.map(filter).toString().toLowerCase() shouldBeEqualTo makeWhereClause(pattern, "FIRST_AUTHOR", "AUTHORS")
    }

    @Test
    fun creatingWhereCondition_withMethodsMask_searchesExposureAndMethodFields() {
        val pattern = "m"
        filter.methodsMask = pattern
        mapper.map(filter).toString().toLowerCase() shouldBeEqualTo makeWhereClause(
            pattern,
            "EXPOSURE_POLLUTANT",
            "EXPOSURE_ASSESSMENT",
            "METHODS",
            "METHOD_STUDY_DESIGN",
            "METHOD_OUTCOME",
            "METHOD_STATISTICS",
            "METHOD_CONFOUNDERS",
            "POPULATION_PLACE"
        )
    }

    @Test
    fun creatingWhereCondition_withSearchMask_searchesRemainingTextFields() {
        val pattern = "foo"
        filter.searchMask = pattern
        mapper.map(filter).toString().toLowerCase() shouldBeEqualTo makeWhereClause(
            pattern,
            "DOI",
            "LOCATION",
            "TITLE",
            "GOALS",
            "POPULATION",
            "POPULATION_PARTICIPANTS",
            "POPULATION_DURATION",
            "RESULT",
            "RESULT_EXPOSURE_RANGE",
            "RESULT_EFFECT_ESTIMATE",
            "RESULT_MEASURED_OUTCOME",
            "CONCLUSION",
            "COMMENT",
            "INTERN",
            "ORIGINAL_ABSTRACT"
        )
    }

    @Test
    fun creatingWhereCondition_withPublicationYearFrom_searchesPublicationYear() {
        filter.publicationYearFrom = 2016
        mapper.map(filter).toString() shouldBeEqualTo
            """"public"."paper"."publication_year" >= 2016"""
    }

    @Test
    fun creatingWhereCondition_withPublicationYearUntil_searchesPublicationYear() {
        filter.publicationYearUntil = 2016
        mapper.map(filter).toString() shouldBeEqualTo
            """"public"."paper"."publication_year" <= 2016"""
    }

    @Test
    fun creatingWhereCondition_withNewsletterId() {
        filter.newsletterId = 10
        mapper.map(filter).toString() shouldBeEqualTo
            """exists (
                  |  select 1 "one"
                  |  from "public"."paper_newsletter"
                  |  where (
                  |    "public"."paper_newsletter"."paper_id" = "public"."paper"."id"
                  |    and "public"."paper_newsletter"."newsletter_id" = 10
                  |  )
                  |)""".trimMargin()
    }
}
