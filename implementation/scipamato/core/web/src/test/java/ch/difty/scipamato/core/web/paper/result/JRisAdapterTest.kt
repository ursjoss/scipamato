package ch.difty.scipamato.core.web.paper.result

import ch.difty.scipamato.core.entity.Paper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class JRisAdapterTest {

    private val adapter = JRisAdapter(
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
    fun canParseSimplePaperWithOneAuthorAndSipleButNonParseableLocation() {
        val expected =
                """TY  - JOUR
                  |A1  - Bond,J.
                  |AB  - original abstract
                  |DB  - scipamato
                  |DO  - 10.1016/abcde.2017.07.063
                  |ID  - 123456
                  |J1  - location
                  |L1  - https://scipamato.ch/paper/number/1111
                  |LK  - http://localhost:8080/123456
                  |M1  - 1111
                  |M2  - goals
                  |PY  - 2019
                  |T1  - title
                  |ER  - 
                  |
              """.trimMargin()
        assertThat(adapter.build(listOf(paper))).isEqualTo(expected)
    }

    @Test
    fun canParseVariousAuthors() {
        val p = paper.apply { authors = "Bond J, Künzli N, Kutlar Joss M, Probst-Hensch N, D'Agostino RB Sr, Nhung NTT, Some Institute." }
        val expectedParts = setOf(
                "A1  - Bond,J.",
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
                "J1  - Int J Public Health",
                "VL  - 62",
                "IS  - 4",
                "SP  - 453",
                "EP  - 462"
        )
        val unexpectedParts = setOf(
                "J1  - $loc"
        )
        val ris = adapter.build(listOf(p))
        assertThat(ris.split("\n")).containsAll(expectedParts)
        assertThat(ris.split("\n")).doesNotContainAnyElementsOf(unexpectedParts)
    }

    @Test
    fun canParseLocationContainingTwoComponentIssue() {
        val p = paper.apply { location = "Int J Hyg Environ Health. 2016; 219 (4-5): 356-363." }
        val expectedParts = setOf(
                "J1  - Int J Hyg Environ Health",
                "VL  - 219",
                "IS  - 4-5",
                "SP  - 356",
                "EP  - 363"
        )
        val unexpectedParts = setOf(
                "J1  - Int J Hyg Environ Health. 2016; 219 (4-5): 356-363."
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
                "J1  - Part Fibre Toxicol",
                "VL  - 12",
                "SP  - 6"
        )
        val unexpectedParts = setOf(
                "J1  - $loc"
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
                "J1  - J Pediatr",
                "VL  - 177",
                "SP  - 179",
                "EP  - 183"
        )
        val unexpectedParts = setOf(
                "J1  - $loc"
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
                "J1  - PLoS One",
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
                "J1  - Sci Total Environ",
                "VL  - 427-428",
                "SP  - 191",
                "EP  - 202"
        )
        val unexpectedParts = setOf(
                "J1  - $loc"
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
                "J1  - PLoS One",
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
                "J1  - Environ Pollut",
                "VL  - 230",
                "SP  - 1000",
                "EP  - 1008"
        )
        val unexpectedParts = setOf(
                "IS  -",
                "J1  - $loc"
        )
        val ris = adapter.build(listOf(p))
        assertThat(ris.split("\n")).containsAll(expectedParts)
        assertThat(ris.split("\n")).doesNotContainAnyElementsOf(unexpectedParts)
    }

    @Test
    fun withoutInternalUrl_doesNotBuildLK() {
        val adapter = JRisAdapter(
                dbName = "scipamato",
                internalUrl = null,
                publicUrl = "https://scipamato.ch/"
        )
        val expected =
                """TY  - JOUR
                  |A1  - Bond,J.
                  |AB  - original abstract
                  |DB  - scipamato
                  |DO  - 10.1016/abcde.2017.07.063
                  |ID  - 123456
                  |J1  - location
                  |L1  - https://scipamato.ch/paper/number/1111
                  |M1  - 1111
                  |M2  - goals
                  |PY  - 2019
                  |T1  - title
                  |ER  - 
                  |
              """.trimMargin()
        assertThat(adapter.build(listOf(paper))).isEqualTo(expected)
    }


    @Test
    fun withoutPublicUrl_doesNotBuildL1() {
        val adapter = JRisAdapter(
                dbName = "scipamato",
                internalUrl = "https://localhost:8081/",
                publicUrl = null
        )
        val expected =
                """TY  - JOUR
                  |A1  - Bond,J.
                  |AB  - original abstract
                  |DB  - scipamato
                  |DO  - 10.1016/abcde.2017.07.063
                  |ID  - 123456
                  |J1  - location
                  |LK  - https://localhost:8081/123456
                  |M1  - 1111
                  |M2  - goals
                  |PY  - 2019
                  |T1  - title
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
                  |A1  - Bond,J.
                  |AB  - original abstract
                  |DB  - scipamato
                  |ID  - 123456
                  |J1  - location
                  |L1  - https://scipamato.ch/paper/number/1111
                  |LK  - http://localhost:8080/123456
                  |M1  - 1111
                  |PY  - 2019
                  |T1  - title
                  |ER  - 
                  |
              """.trimMargin()
        assertThat(adapter.build(listOf(paper))).isEqualTo(expected)
    }
}