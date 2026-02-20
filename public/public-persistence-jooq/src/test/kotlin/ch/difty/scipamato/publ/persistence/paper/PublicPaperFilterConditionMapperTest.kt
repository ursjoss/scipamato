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
import org.junit.jupiter.api.Disabled
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
        filter.copy(number = number).run {
            mapper.map(this).toString() shouldBeEqualTo """"public"."paper"."number" = 17"""
        }
    }

    @Test
    fun creatingWhereCondition_withAuthorMask_searchesAuthors() {
        val pattern = "am"
        filter.copy(authorMask = pattern).run {
            mapper.map(this).toString() shouldBeEqualTo makeWhereClause(pattern, "authors")
        }
    }

    @Test
    fun creatingWhereCondition_withTitleMask_searchesTitle() {
        val pattern = "tm"
        filter.copy(titleMask = pattern).run {
            mapper.map(this).toString() shouldBeEqualTo makeWhereClause(pattern, "title")
        }
    }

    @Test
    fun creatingWhereCondition_withAuthorMaskHoldingMultipleAuthors_searchesForPapersWithBothAuthorsInAnyOrder() {
        val pattern = "foo  bar"
        filter.copy(authorMask = pattern).run {
            mapper.map(this).toString() shouldBeEqualTo
                """(
                   |  "public"."paper"."authors" ilike '%foo%'
                   |  and "public"."paper"."authors" ilike '%bar%'
                   |)""".trimMargin()
        }
    }

    @Test
    fun creatingWhereCondition_withMethodsMask_searchesMethodFields() {
        val pattern = "m"
        filter.copy(methodsMask = pattern).run {
            mapper.map(this).toString() shouldBeEqualTo makeWhereClause(pattern, "methods")
        }
    }

    @Test
    fun creatingWhereCondition_withMethodsMaskHoldingMultipleKeywords__searchesMethodFieldsWithAllKeywordsInAnyOrder() {
        val pattern = "m1 m2 m3"
        filter.copy(methodsMask = pattern).run {
            mapper.map(this).toString() shouldBeEqualTo
                """(
                |  "public"."paper"."methods" ilike '%m1%'
                |  and "public"."paper"."methods" ilike '%m2%'
                |  and "public"."paper"."methods" ilike '%m3%'
                |)""".trimMargin()
        }
    }

    @Test
    fun creatingWhereCondition_withPublicationYearFrom_anBlankYearUntil_searchesExactPublicationYear() {
        filter.copy(publicationYearFrom = 2016).run {
            publicationYearUntil.shouldBeNull()
            mapper.map(this).toString() shouldBeEqualTo
                """"public"."paper"."publication_year" = 2016"""
        }
    }

    @Test
    fun creatingWhereCondition_withPublicationYearFrom_andPublicationYearUntil_searchesRange() {
        filter.copy(publicationYearFrom = 2016, publicationYearUntil = 2017).run {
            mapper.map(this).toString() shouldBeEqualTo """"public"."paper"."publication_year" between 2016 and 2017"""
        }
    }

    @Test
    fun creatingWhereCondition_withIdenticalPublicationYearFromAndTo_searchesExactPublicationYear() {
        filter.copy(publicationYearFrom = 2016, publicationYearUntil = 2016).run {
            mapper.map(this).toString() shouldBeEqualTo """"public"."paper"."publication_year" = 2016"""
        }
    }

    @Test
    fun creatingWhereCondition_withPublicationYearUntil_searchesUpToPublicationYear() {
        filter.publicationYearFrom.shouldBeNull()
        filter.copy(publicationYearUntil = 2016).run {
            mapper.map(this).toString() shouldBeEqualTo """"public"."paper"."publication_year" <= 2016"""
        }
    }

    @Test
    fun creatingWhereCondition_withPopulationCodes_searchesPopulationCodes() {
        filter.copy(populationCodes = listOf(PopulationCode.CHILDREN)).run {
            mapper.map(this).toString() shouldBeEqualTo
                """"public"."paper"."codes_population" like (('%' || cast(array[1] as varchar)) || '%') escape '!'"""
        }
    }

    @Test
    fun creatingWhereCondition_withMethodStudyDesignCodes_searchesStudyDesignCodes() {
        filter.copy(
            studyDesignCodes = listOf(StudyDesignCode.EPIDEMIOLOGICAL, StudyDesignCode.OVERVIEW_METHODOLOGY)
        ).run {
            mapper.map(this).toString() shouldBeEqualTo
                """"public"."paper"."codes_study_design" like (('%' || cast(array[2, 3] as varchar)) || '%') escape '!'"""
        }
    }

    @Test
    fun creatingWhereCondition_withCodesOfCodeClass1_searchesCodeClasses() {
        filter.copy(codesOfClass1 = listOf(
            Code(code = "c1"),
            Code(code = "c2"),
        )).run {
            assertBasicCodeMappingC1C2()
        }
    }

    private fun PublicPaperFilter.assertBasicCodeMappingC1C2() {
        // Due to bug https://github.com/jOOQ/jOOQ/issues/4754
        // mapper.map(filter).toString() shouldBeEqualTo ""public"."paper"."codes" @> array['c1', 'c2']");
        mapper.map(this).toString() shouldBeEqualTo
            """"public"."paper"."codes" like (('%' || cast(
                |  array[
                |    cast('c1' as clob),
                |    cast('c2' as clob)
                |  ]
                |  as varchar
                |)) || '%') escape '!'""".trimMargin()
    }

    @Test
    fun creatingWhereCondition_withCodesOfCodeClass2_searchesCodeClasses() {
        filter.copy(codesOfClass2 = listOf(
            Code(code = "c1"),
            Code(code = "c2"),
        )).run {
            assertBasicCodeMappingC1C2()
        }
    }

    @Test
    fun creatingWhereCondition_withCodesOfCodeClass3_searchesCodeClasses() {
        filter.copy(codesOfClass3 = listOf(
            Code(code = "c1"),
            Code(code = "c2"),
        )).run {
            assertBasicCodeMappingC1C2()
        }
    }

    @Test
    fun creatingWhereCondition_withCodesOfCodeClass4_searchesCodeClasses() {
        filter.copy(codesOfClass4 = listOf(
            Code(code = "c1"),
            Code(code = "c2"),
        )).run {
            assertBasicCodeMappingC1C2()
        }
    }

    @Test
    fun creatingWhereCondition_withCodesOfCodeClass5_searchesCodeClasses() {
        filter.copy(codesOfClass5 = listOf(
            Code(code = "c1"),
            Code(code = "c2"),
        )).run {
            assertBasicCodeMappingC1C2()
        }
    }

    @Test
    fun creatingWhereCondition_withCodesOfCodeClass6_searchesCodeClasses() {
        filter.copy(codesOfClass6 = listOf(
            Code(code = "c1"),
            Code(code = "c2"),
        )).run {
            assertBasicCodeMappingC1C2()
        }
    }

    @Test
    fun creatingWhereCondition_withCodesOfCodeClass7_searchesCodeClasses() {
        filter.copy(codesOfClass7 = listOf(
            Code(code = "c1"),
            Code(code = "c2"),
        )).run {
            assertBasicCodeMappingC1C2()
        }
    }

    @Test
    fun creatingWhereCondition_withCodesOfCodeClass8_searchesCodeClasses() {
        filter.copy(codesOfClass8 = listOf(
            Code(code = "c1"),
            Code(code = "c2"),
        )).run {
            assertBasicCodeMappingC1C2()
        }
    }

    @Test
    fun creatingWhereCondition_withCodesOfAllCodeClasses_searchesCodeClasses() {
        filter.copy(
            codesOfClass1 = listOf(Code(code = "1A")),
            codesOfClass2 = listOf(Code(code = "2B")),
            codesOfClass3 = listOf(Code(code = "3C")),
            codesOfClass4 = listOf(Code(code = "4D")),
            codesOfClass5 = listOf(Code(code = "5E")),
            codesOfClass6 = listOf(Code(code = "6F")),
            codesOfClass7 = listOf(Code(code = "7G")),
            codesOfClass8 = listOf(Code(code = "8H")),
        ).run {
            mapper.map(this).toString() shouldBeEqualTo
                """"public"."paper"."codes" like (('%' || cast(
                |  array[
                |    cast('1A' as clob),
                |    cast('2B' as clob),
                |    cast('3C' as clob),
                |    cast('4D' as clob),
                |    cast('5E' as clob),
                |    cast('6F' as clob),
                |    cast('7G' as clob),
                |    cast('8H' as clob)
                |  ]
                |  as varchar
                |)) || '%') escape '!'""".trimMargin()
        }
    }

    @Test
    @Disabled
    fun creatingWhereCondition_withSetButThenClearedCodes_doesNotFilterByCodes() {
        filter.copy(codesOfClass1 = mutableListOf(Code(code = "1A"))).run {
//        filter.codesOfClass1.clear()
            mapper.map(this).toString() shouldBeEqualTo "true"
        }
    }

    @Test
    fun creatingWhereCondition_withAuthorMaskHoldingMultipleQuotedAuthors_searchesForPapersWithBothAuthorsInAnyOrder() {
        val pattern = """"Last F" "Other S""""
        filter.copy(authorMask = pattern).run {
            mapper.map(this).toString() shouldBeEqualTo
                """(
                |  "public"."paper"."authors" ilike '%Last F%'
                |  and "public"."paper"."authors" ilike '%Other S%'
                |)""".trimMargin()
        }
    }
}
