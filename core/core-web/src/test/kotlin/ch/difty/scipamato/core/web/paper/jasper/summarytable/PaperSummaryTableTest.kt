package ch.difty.scipamato.core.web.paper.jasper.summarytable

import ch.difty.scipamato.core.web.paper.jasper.JasperEntityTest
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

internal class PaperSummaryTableTest : JasperEntityTest() {

    private val rhf = newReportHeaderFields()
    private lateinit var pst: PaperSummaryTable

    @Test
    fun instantiating() {
        pst = PaperSummaryTable(p, rhf)
        assertPst()
    }

    private fun newReportHeaderFields(): ReportHeaderFields {
        return ReportHeaderFields
            .builder(HEADER_PART, BRAND)
            .captionLabel(CAPTION)
            .methodsLabel(METHODS_LABEL)
            .numberLabel(NUMBER_LABEL)
            .build()
    }

    private fun assertPst() {
        pst.caption shouldBeEqualTo CAPTION
        pst.brand shouldBeEqualTo BRAND
        pst.numberLabel shouldBeEqualTo NUMBER_LABEL
        pst.number shouldBeEqualTo NUMBER.toString()
        pst.firstAuthor shouldBeEqualTo FIRST_AUTHOR
        pst.publicationYear shouldBeEqualTo PUBLICATION_YEAR.toString()
        pst.goals shouldBeEqualTo GOALS
        pst.title shouldBeEqualTo TITLE
        pst.codesOfClass1 shouldBeEqualTo "1F"
        pst.codesOfClass4 shouldBeEqualTo "4A,4C"
        pst.codesOfClass7 shouldBeEqualTo "7B"
    }

    @Test
    fun constructionWithPaperWithNoCodeOfClass7_returnsBlank() {
        p.clearCodes()
        pst = PaperSummaryTable(p, rhf)
        pst.codesOfClass4 shouldBeEqualTo ""
    }

    @Test
    fun paperWithNullNumber_resultsInEmptyNumber() {
        p.number = null
        pst = PaperSummaryTable(p, rhf)
        pst.number.shouldBeEmpty()
    }

    @Test
    fun paperWithNullYear_resultsInEmptyYear() {
        p.publicationYear = null
        pst = PaperSummaryTable(p, rhf)
        pst.publicationYear.shouldBeEmpty()
    }

    @Test
    fun equals() {
        EqualsVerifier
            .forClass(PaperSummaryTable::class.java)
            .withRedefinedSuperclass()
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify()
    }

    @Test
    fun testingToString() {
        pst = PaperSummaryTable(p, rhf)
        pst.toString() shouldBeEqualTo
            "PaperSummaryTable(number=100, firstAuthor=firstAuthor, publicationYear=2017, codesOfClass1=1F, " +
            "codesOfClass4=4A,4C, codesOfClass7=7B, goals=goals, title=title, result=results, caption=caption, " +
            "brand=brand, numberLabel=nl)"
    }

    companion object {
        private const val BRAND = "brand"
        private const val CAPTION = "caption"
        private const val NUMBER_LABEL = "nl"
    }
}
