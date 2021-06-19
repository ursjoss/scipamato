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
        mapper.map(filter).toString() shouldBeEqualTo makeWhereClause(pattern, "first_author", "authors")
    }

    @Test
    fun creatingWhereCondition_withMethodsMask_searchesExposureAndMethodFields() {
        val pattern = "m"
        filter.methodsMask = pattern
        mapper.map(filter).toString() shouldBeEqualTo makeWhereClause(
            pattern,
            "exposure_pollutant",
            "exposure_assessment",
            "methods",
            "method_study_design",
            "method_outcome",
            "method_statistics",
            "method_confounders",
            "population_place"
        )
    }

    @Test
    fun creatingWhereCondition_withSearchMask_searchesRemainingTextFields() {
        val pattern = "foo"
        filter.searchMask = pattern
        mapper.map(filter).toString() shouldBeEqualTo makeWhereClause(
            pattern,
            "doi",
            "location",
            "title",
            "goals",
            "population",
            "population_participants",
            "population_duration",
            "result",
            "result_exposure_range",
            "result_effect_estimate",
            "result_measured_outcome",
            "conclusion",
            "comment",
            "intern",
            "original_abstract"
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
