@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.core.logic.export

import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.logic.exporting.DefaultRisAdapter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class DefaultRisAdapterTest {

    private val adapter = DefaultRisAdapter(
        dbName = "scipamato",
        internalUrl = "http://localhost:8080/",
        publicUrl = "https://scipamato.ch/"
    )

    val paper = Paper().apply {
        number = 1111
        pmId = 123456
        doi = "10.1016/abcde.2017.07.063"
        title = "title"
        authors = "Bond J."
        publicationYear = 2019
        location = "location"
        goals = "goals"
        originalAbstract = "original abstract"
    }

    @Test
    fun buildingWithExplicitlyBlankSortOrder_sortsMostlyAlphabetically() {
        // TY always first, ER always last
        val expected =
            """TY  - JOUR
                  |AB  - original abstract
                  |AU  - Bond,J.
                  |DB  - scipamato
                  |DO  - 10.1016/abcde.2017.07.063
                  |ID  - 123456
                  |JO  - location
                  |L1  - https://scipamato.ch/paper/number/1111
                  |L2  - http://localhost:8080/123456
                  |M1  - 1111
                  |M2  - goals
                  |PY  - 2019
                  |TI  - title
                  |ER  - 
                  |
              """.trimMargin()
        assertThat(adapter.build(listOf(paper), listOf())).isEqualTo(expected)
    }

    @Test
    fun canParseSimplePaperWithOneAuthorAndSipleButNonParseableLocation() {
        val expected =
            """TY  - JOUR
                  |AU  - Bond,J.
                  |PY  - 2019
                  |TI  - title
                  |JO  - location
                  |ID  - 123456
                  |DO  - 10.1016/abcde.2017.07.063
                  |M1  - 1111
                  |M2  - goals
                  |AB  - original abstract
                  |DB  - scipamato
                  |L1  - https://scipamato.ch/paper/number/1111
                  |L2  - http://localhost:8080/123456
                  |ER  - 
                  |
              """.trimMargin()
        assertThat(adapter.build(listOf(paper))).isEqualTo(expected)
    }

    @Test
    fun canParseVariousAuthors() {
        val p = paper.apply {
            authors = "Bond J, Künzli N, Kutlar Joss M, Probst-Hensch N, D'Agostino RB Sr, Nhung NTT, Some Institute."
        }
        val expectedParts = setOf(
            "AU  - Bond,J.",
            "AU  - Künzli,N.",
            "AU  - Kutlar Joss,M.",
            "AU  - Probst-Hensch,N.",
            "AU  - D'Agostino,RB.,Sr.",
            "AU  - Nhung,NTT.",
            "AU  - Some Institute."
        )
        val ris = adapter.build(listOf(p))
        assertThat(ris.split("\n")).containsAll(expectedParts)
    }

    @Test
    fun canParseLocationContainingIssue() {
        val loc = "Int J Public Health. 2017; 62 (4): 453-462."
        val p = paper.apply { location = loc }
        val expectedParts = setOf(
            "JO  - Int J Public Health",
            "VL  - 62",
            "IS  - 4",
            "SP  - 453",
            "EP  - 462"
        )
        val unexpectedParts = setOf(
            "JO  - $loc"
        )
        val ris = adapter.build(listOf(p))
        assertThat(ris.split("\n")).containsAll(expectedParts)
        assertThat(ris.split("\n")).doesNotContainAnyElementsOf(unexpectedParts)
    }

    @Test
    fun canParseLocationContainingTwoComponentIssue() {
        val p = paper.apply { location = "Int J Hyg Environ Health. 2016; 219 (4-5): 356-363." }
        val expectedParts = setOf(
            "JO  - Int J Hyg Environ Health",
            "VL  - 219",
            "IS  - 4-5",
            "SP  - 356",
            "EP  - 363"
        )
        val unexpectedParts = setOf(
            "JO  - Int J Hyg Environ Health. 2016; 219 (4-5): 356-363."
        )
        val ris = adapter.build(listOf(p))
        assertThat(ris.split("\n")).containsAll(expectedParts)
        assertThat(ris.split("\n")).doesNotContainAnyElementsOf(unexpectedParts)
    }

    @Test
    fun canParseLocationWithOnlyStartPageAndNoIssue() {
        val loc = "Part Fibre Toxicol. 2015; 12: 6."
        val p = paper.apply { location = loc }
        val expectedParts = setOf(
            "JO  - Part Fibre Toxicol",
            "VL  - 12",
            "SP  - 6"
        )
        val unexpectedParts = setOf(
            "JO  - $loc"
        )
        val ris = adapter.build(listOf(p))
        assertThat(ris.split("\n")).containsAll(expectedParts)
        assertThat(ris.split("\n")).doesNotContainAnyElementsOf(unexpectedParts)
        assertThat(ris).doesNotContain("EP -  ")
    }

    @Test
    fun canParseLocationWithAdditionalStuffAfterPageRange() {
        val loc = "J Pediatr. 2016; 177: 179-183. e1."
        val p = paper.apply { location = loc }
        val expectedParts = setOf(
            "JO  - J Pediatr",
            "VL  - 177",
            "SP  - 179",
            "EP  - 183"
        )
        val unexpectedParts = setOf(
            "JO  - $loc"
        )
        val ris = adapter.build(listOf(p))
        assertThat(ris.split("\n")).containsAll(expectedParts)
        assertThat(ris.split("\n")).doesNotContainAnyElementsOf(unexpectedParts)
    }

    @Test
    fun canParseLocationWithOnlyElectronicPagination() {
        val loc = "PLoS One. 2013; 8 (9): e75001."
        val p = paper.apply { location = loc }
        val expectedParts = setOf(
            "JO  - PLoS One",
            "VL  - 8",
            "IS  - 9"
        )
        val unexpectedParts = setOf(
            "J1 -  $loc"
        )
        val ris = adapter.build(listOf(p))
        assertThat(ris.split("\n")).containsAll(expectedParts)
        assertThat(ris.split("\n")).doesNotContainAnyElementsOf(unexpectedParts)
        assertThat(ris).doesNotContain("EP -  ")
    }

    @Test
    fun canParseLocationWithVolumeRange() {
        val loc = "Sci Total Environ. 2012; 427-428: 191-202."
        val p = paper.apply { location = loc }
        val expectedParts = setOf(
            "JO  - Sci Total Environ",
            "VL  - 427-428",
            "SP  - 191",
            "EP  - 202"
        )
        val unexpectedParts = setOf(
            "JO  - $loc"
        )
        val ris = adapter.build(listOf(p))
        assertThat(ris.split("\n")).containsAll(expectedParts)
        assertThat(ris.split("\n")).doesNotContainAnyElementsOf(unexpectedParts)
        assertThat(ris).doesNotContain("IS -  ")
    }

    @Test
    fun canParseLocationWithOnlyElectronicPaginationx() {
        val p = paper.apply { location = "PLoS One. 2013; 8 (9): e75001." }
        val expectedParts = setOf(
            "JO  - PLoS One",
            "VL  - 8",
            "IS  - 9"
        )
        val unexpectedParts = setOf(
            "PLoS One. 2013; 8 (9): e75001."
        )
        val ris = adapter.build(listOf(p))
        assertThat(ris.split("\n")).containsAll(expectedParts)
        assertThat(ris.split("\n")).doesNotContainAnyElementsOf(unexpectedParts)
        assertThat(ris).doesNotContain("EP -  ")
    }

    @Test
    fun canParseLocationNotContainingIssue() {
        val loc = "Environ Pollut. 2017; 230: 1000-1008."
        val p = paper.apply { location = loc }
        val expectedParts = setOf(
            "JO  - Environ Pollut",
            "VL  - 230",
            "SP  - 1000",
            "EP  - 1008"
        )
        val unexpectedParts = setOf(
            "IS  -",
            "JO  - $loc"
        )
        val ris = adapter.build(listOf(p))
        assertThat(ris.split("\n")).containsAll(expectedParts)
        assertThat(ris.split("\n")).doesNotContainAnyElementsOf(unexpectedParts)
    }

    @Test
    fun withoutInternalUrl_doesNotBuildLK() {
        val adapter = DefaultRisAdapter(
            dbName = "scipamato",
            internalUrl = null,
            publicUrl = "https://scipamato.ch/"
        )
        val expected =
            """TY  - JOUR
                  |AU  - Bond,J.
                  |PY  - 2019
                  |TI  - title
                  |JO  - location
                  |ID  - 123456
                  |DO  - 10.1016/abcde.2017.07.063
                  |M1  - 1111
                  |M2  - goals
                  |AB  - original abstract
                  |DB  - scipamato
                  |L1  - https://scipamato.ch/paper/number/1111
                  |ER  - 
                  |
              """.trimMargin()
        assertThat(adapter.build(listOf(paper))).isEqualTo(expected)
    }

    @Test
    fun withoutPublicUrl_doesNotBuildL1() {
        val adapter = DefaultRisAdapter(
            dbName = "scipamato",
            internalUrl = "https://localhost:8081/",
            publicUrl = null
        )
        val expected =
            """TY  - JOUR
                  |AU  - Bond,J.
                  |PY  - 2019
                  |TI  - title
                  |JO  - location
                  |ID  - 123456
                  |DO  - 10.1016/abcde.2017.07.063
                  |M1  - 1111
                  |M2  - goals
                  |AB  - original abstract
                  |DB  - scipamato
                  |L2  - https://localhost:8081/123456
                  |ER  - 
                  |
              """.trimMargin()
        assertThat(adapter.build(listOf(paper))).isEqualTo(expected)
    }

    @Test
    fun canParseDoiNullOrGoalsNull() {
        val p = paper.apply {
            doi = null
            goals = null
        }
        val expected =
            """TY  - JOUR
                  |AU  - Bond,J.
                  |PY  - 2019
                  |TI  - title
                  |JO  - location
                  |ID  - 123456
                  |M1  - 1111
                  |AB  - original abstract
                  |DB  - scipamato
                  |L1  - https://scipamato.ch/paper/number/1111
                  |L2  - http://localhost:8080/123456
                  |ER  - 
                  |
              """.trimMargin()
        assertThat(adapter.build(listOf(p))).isEqualTo(expected)
    }

    @Test
    fun complexPaper() {
        val paper = Paper().apply {
            number = 1111
            pmId = 123456
            doi = "10.1016/abcde.2017.07.063"
            title = "title"
            authors = "Bond J, Bourne J."
            publicationYear = 2019
            location = "Whatever Journal. 2019; 34 (3): 10-20."
            goals = "goals"
            originalAbstract = "original abstract"
        }
        val expected =
            """TY  - JOUR
                  |AU  - Bond,J.
                  |AU  - Bourne,J.
                  |PY  - 2019
                  |TI  - title
                  |JO  - Whatever Journal
                  |SP  - 10
                  |EP  - 20
                  |VL  - 34
                  |IS  - 3
                  |ID  - 123456
                  |DO  - 10.1016/abcde.2017.07.063
                  |M1  - 1111
                  |M2  - goals
                  |AB  - original abstract
                  |DB  - scipamato
                  |L1  - https://scipamato.ch/paper/number/1111
                  |L2  - http://localhost:8080/123456
                  |ER  - 
                  |
              """.trimMargin()
        assertThat(adapter.build(listOf(paper))).isEqualTo(expected)
    }

    @Test
    fun truncatesToMaxLength() {
        val p = Paper().apply {
            authors = "Pan P."
            location = "0123456789".repeat(30)
        }
        val expected =
            """
            |TY  - JOUR
            |AU  - Pan,P.
            |JO  - 012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234
            |DB  - scipamato
            |L1  - https://scipamato.ch/paper/number/null
            |L2  - http://localhost:8080/null
            |ER  - 
            |""".trimMargin()
        assertThat(adapter.build(listOf(p))).isEqualTo(expected)
    }
}
