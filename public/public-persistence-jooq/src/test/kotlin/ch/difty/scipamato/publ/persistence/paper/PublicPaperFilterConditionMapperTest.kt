package ch.difty.scipamato.publ.persistence.paper

import ch.difty.scipamato.common.persistence.FilterConditionMapperTest
import ch.difty.scipamato.publ.db.tables.Paper
import ch.difty.scipamato.publ.db.tables.Paper.PAPER
import ch.difty.scipamato.publ.db.tables.records.PaperRecord
import ch.difty.scipamato.publ.entity.Code
import ch.difty.scipamato.publ.entity.PopulationCode
import ch.difty.scipamato.publ.entity.StudyDesignCode
import ch.difty.scipamato.publ.entity.filter.PublicPaperFilter
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.junit.jupiter.api.Test

class PublicPaperFilterConditionMapperTest : FilterConditionMapperTest<PaperRecord, Paper, PublicPaperFilter>() {

    private val m = PublicPaperFilterConditionMapper()

    override val filter = PublicPaperFilter()

    override val table: Paper
        get() = PAPER

    override val mapper = m

    @Test
    fun creatingWhereCondition_withNumber_searchesNumber() {
        val number = 17L
        filter.number = number
        mapper.map(filter).toString().toUpperCase() shouldBeEqualTo """"PUBLIC"."PAPER"."NUMBER" = 17"""
    }

    @Test
    fun creatingWhereCondition_withAuthorMask_searchesAuthors() {
        val pattern = "am"
        filter.authorMask = pattern
        mapper.map(filter).toString().toLowerCase() shouldBeEqualTo makeWhereClause(pattern, "AUTHORS")
    }

    @Test
    fun creatingWhereCondition_withTitleMask_searchesTitle() {
        val pattern = "tm"
        filter.titleMask = pattern
        mapper.map(filter).toString().toLowerCase() shouldBeEqualTo makeWhereClause(pattern, "TITLE")
    }

    @Test
    fun creatingWhereCondition_withAuthorMaskHoldingMultipleAuthors_searchesForPapersWithBothAuthorsInAnyOrder() {
        val pattern = "foo  bar"
        filter.authorMask = pattern
        mapper.map(filter).toString() shouldBeEqualTo
            """(
                   |  lower("public"."paper"."authors") like lower('%foo%')
                   |  and lower("public"."paper"."authors") like lower('%bar%')
                   |)""".trimMargin()
    }

    @Test
    fun creatingWhereCondition_withMethodsMask_searchesMethodFields() {
        val pattern = "m"
        filter.methodsMask = pattern
        mapper.map(filter).toString().toLowerCase() shouldBeEqualTo makeWhereClause(pattern, "METHODS")
    }

    @Test
    fun creatingWhereCondition_withMethodsMaskHoldingMultipleKeywords__searchesMethodFieldsWithAllKeywordsInAnyOrder() {
        val pattern = "m1 m2 m3"
        filter.methodsMask = pattern
        mapper.map(filter).toString() shouldBeEqualTo
            """(
                |  lower("public"."paper"."methods") like lower('%m1%')
                |  and lower("public"."paper"."methods") like lower('%m2%')
                |  and lower("public"."paper"."methods") like lower('%m3%')
                |)""".trimMargin()
    }

    @Test
    fun creatingWhereCondition_withPublicationYearFrom_anBlankYearUntil_searchesExactPublicationYear() {
        filter.publicationYearFrom = 2016
        filter.publicationYearUntil.shouldBeNull()
        mapper.map(filter).toString() shouldBeEqualTo
            """"public"."paper"."publication_year" = 2016"""
    }

    @Test
    fun creatingWhereCondition_withPublicationYearFrom_andPublicationYearUntil_searchesRange() {
        filter.publicationYearFrom = 2016
        filter.publicationYearUntil = 2017
        mapper.map(filter).toString() shouldBeEqualTo
            """"public"."paper"."publication_year" between 2016 and 2017"""
    }

    @Test
    fun creatingWhereCondition_withIdenticalPublicationYearFromAndTo_searchesExactPublicationYear() {
        filter.publicationYearFrom = 2016
        filter.publicationYearUntil = 2016
        mapper.map(filter).toString() shouldBeEqualTo
            """"public"."paper"."publication_year" = 2016"""
    }

    @Test
    fun creatingWhereCondition_withPublicationYearUntil_searchesUpToPublicationYear() {
        filter.publicationYearFrom.shouldBeNull()
        filter.publicationYearUntil = 2016
        mapper.map(filter).toString() shouldBeEqualTo
            """"public"."paper"."publication_year" <= 2016"""
    }

    @Test
    fun creatingWhereCondition_withPopulationCodes_searchesPopulationCodes() {
        filter.populationCodes = listOf(PopulationCode.CHILDREN)
        mapper.map(filter).toString() shouldBeEqualTo
            """"public"."paper"."codes_population" @> array[1]"""
    }

    @Test
    fun creatingWhereCondition_withMethodStudyDesignCodes_searchesStudyDesignCodes() {
        filter.studyDesignCodes = listOf(StudyDesignCode.EPIDEMIOLOGICAL, StudyDesignCode.OVERVIEW_METHODOLOGY)
        mapper.map(filter).toString() shouldBeEqualTo
            """"public"."paper"."codes_study_design" @> array[2, 3]"""
    }

    @Test
    fun creatingWhereCondition_withCodesOfCodeClass1_searchesCodeClasses() {
        filter.codesOfClass1 = listOf(
            Code.builder().code("c1").build(),
            Code.builder().code("c2").build())
        assertBasicCodeMappingC1C2()
    }

    private fun assertBasicCodeMappingC1C2() {
        // Due to bug https://github.com/jOOQ/jOOQ/issues/4754
        // mapper.map(filter).toString().toUpperCase() shouldBeEqualTo""PUBLIC"."PAPER"."CODES"
        // @> array['c1', 'c2']");
        mapper.map(filter).toString() shouldBeEqualTo
            """"public"."paper"."codes" @> array[
                        |  cast('c1' as clob), 
                        |  cast('c2' as clob)
                        |]""".trimMargin()
    }

    @Test
    fun creatingWhereCondition_withCodesOfCodeClass2_searchesCodeClasses() {
        filter.codesOfClass2 = listOf(
            Code.builder().code("c1").build(),
            Code.builder().code("c2").build())
        assertBasicCodeMappingC1C2()
    }

    @Test
    fun creatingWhereCondition_withCodesOfCodeClass3_searchesCodeClasses() {
        filter.codesOfClass3 = listOf(
            Code.builder().code("c1").build(),
            Code.builder().code("c2").build())
        assertBasicCodeMappingC1C2()
    }

    @Test
    fun creatingWhereCondition_withCodesOfCodeClass4_searchesCodeClasses() {
        filter.codesOfClass4 = listOf(
            Code.builder().code("c1").build(),
            Code.builder().code("c2").build()
        )
        assertBasicCodeMappingC1C2()
    }

    @Test
    fun creatingWhereCondition_withCodesOfCodeClass5_searchesCodeClasses() {
        filter.codesOfClass5 = listOf(
            Code.builder().code("c1").build(),
            Code.builder().code("c2").build())
        assertBasicCodeMappingC1C2()
    }

    @Test
    fun creatingWhereCondition_withCodesOfCodeClass6_searchesCodeClasses() {
        filter.codesOfClass6 = listOf(
            Code.builder().code("c1").build(),
            Code.builder().code("c2").build())
        assertBasicCodeMappingC1C2()
    }

    @Test
    fun creatingWhereCondition_withCodesOfCodeClass7_searchesCodeClasses() {
        filter.codesOfClass7 = listOf(
            Code.builder().code("c1").build(),
            Code.builder().code("c2").build())
        assertBasicCodeMappingC1C2()
    }

    @Test
    fun creatingWhereCondition_withCodesOfCodeClass8_searchesCodeClasses() {
        filter.codesOfClass8 = listOf(
            Code.builder().code("c1").build(),
            Code.builder().code("c2").build())
        assertBasicCodeMappingC1C2()
    }

    @Test
    fun creatingWhereCondition_withCodesOfAllCodeClasses_searchesCodeClasses() {
        filter.codesOfClass1 = listOf(Code.builder().code("1A").build())
        filter.codesOfClass2 = listOf(Code.builder().code("2B").build())
        filter.codesOfClass3 = listOf(Code.builder().code("3C").build())
        filter.codesOfClass4 = listOf(Code.builder().code("4D").build())
        filter.codesOfClass5 = listOf(Code.builder().code("5E").build())
        filter.codesOfClass6 = listOf(Code.builder().code("6F").build())
        filter.codesOfClass7 = listOf(Code.builder().code("7G").build())
        filter.codesOfClass8 = listOf(Code.builder().code("8H").build())
        // Due to bug https://github.com/jOOQ/jOOQ/issues/4754
        // mapper.map(filter).toString().toUpperCase() shouldBeEqualTo""PUBLIC"."PAPER"."CODES"
        // @> array['1A', '2B', '3C', '4D', '5E', '6F', '7G', '8H']");
        mapper.map(filter).toString() shouldBeEqualTo
            """"public"."paper"."codes" @> array[
                |  cast('1A' as clob), 
                |  cast('2B' as clob), 
                |  cast('3C' as clob), 
                |  cast('4D' as clob), 
                |  cast('5E' as clob), 
                |  cast('6F' as clob), 
                |  cast('7G' as clob), 
                |  cast('8H' as clob)
                |]""".trimMargin()
    }

    @Test
    fun creatingWhereCondition_withSetButThenClearedCodes_doesNotFilterByCodes() {
        filter.codesOfClass1 = mutableListOf(Code.builder().code("1A").build())
        filter.codesOfClass1.clear()
        mapper.map(filter).toString() shouldBeEqualTo "1 = 1"
    }

    @Test
    fun creatingWhereCondition_withAuthorMaskHoldingMultipleQuotedAuthors_searchesForPapersWithBothAuthorsInAnyOrder() {
        val pattern = """"Last F" "Other S""""
        filter.authorMask = pattern
        mapper.map(filter).toString() shouldBeEqualTo
            """(
                |  lower("public"."paper"."authors") like lower('%Last F%')
                |  and lower("public"."paper"."authors") like lower('%Other S%')
                |)""".trimMargin()
    }
}
