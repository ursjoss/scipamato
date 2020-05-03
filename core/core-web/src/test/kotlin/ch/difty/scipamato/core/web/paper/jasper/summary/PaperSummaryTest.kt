package ch.difty.scipamato.core.web.paper.jasper.summary

import ch.difty.scipamato.core.web.paper.jasper.CoreShortFieldConcatenator
import ch.difty.scipamato.core.web.paper.jasper.CoreShortFieldWithEmptyMainFieldConcatenator
import ch.difty.scipamato.core.web.paper.jasper.JasperEntityTest
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

internal class PaperSummaryTest : JasperEntityTest() {

    private val shortFieldConcatenator: CoreShortFieldConcatenator = CoreShortFieldWithEmptyMainFieldConcatenator()
    private lateinit var ps: PaperSummary
    private var rhf = newReportHeaderFields()
    private fun newReportHeaderFields(): ReportHeaderFields {
        return ReportHeaderFields
            .builder(HEADER_PART, BRAND)
            .populationLabel(POPULATION_LABEL)
            .goalsLabel(GOALS_LABEL)
            .methodsLabel(METHODS_LABEL)
            .resultLabel(RESULT_LABEL)
            .commentLabel(COMMENT_LABEL)
            .build()
    }

    @Test
    fun instantiating() {
        ps = PaperSummary(p, shortFieldConcatenator, rhf)
        assertPaperSummary()
    }

    private fun assertPaperSummary() {
        ps.number shouldBeEqualTo NUMBER.toString()
        ps.authors shouldBeEqualTo AUTHORS
        ps.title shouldBeEqualTo TITLE
        ps.location shouldBeEqualTo LOCATION
        ps.goals shouldBeEqualTo GOALS
        ps.population shouldBeEqualTo POPULATION
        ps.methods shouldBeEqualTo METHODS
        ps.result shouldBeEqualTo RESULT
        ps.comment shouldBeEqualTo COMMENT
        ps.populationLabel shouldBeEqualTo POPULATION_LABEL
        ps.methodsLabel shouldBeEqualTo METHODS_LABEL
        ps.resultLabel shouldBeEqualTo RESULT_LABEL
        ps.commentLabel shouldBeEqualTo COMMENT_LABEL
        ps.header shouldBeEqualTo "$HEADER_PART $NUMBER"
        ps.brand shouldBeEqualTo BRAND
        ps.createdBy shouldBeEqualTo CREATED_BY
    }

    @Test
    fun populationLabelIsBlankIfPopulationIsBlank() {
        p.population = ""
        ps = newPaperSummary()
        ps.population shouldBeEqualTo ""
        ps.populationLabel shouldBeEqualTo ""
    }

    private fun newPaperSummary(): PaperSummary = PaperSummary(p, shortFieldConcatenator, rhf)

    @Test
    fun methodsLabelIsBlankIfMethodsIsBlank() {
        p.methods = ""
        ps = newPaperSummary()
        ps.methods shouldBeEqualTo ""
        ps.methodsLabel shouldBeEqualTo ""
    }

    @Test
    fun resultLabelIsBlankIfResultIsBlank() {
        p.result = ""
        ps = newPaperSummary()
        ps.result shouldBeEqualTo ""
        ps.resultLabel shouldBeEqualTo ""
    }

    @Test
    fun commentLabelIsBlankIfCommentIsBlank() {
        p.comment = ""
        ps = newPaperSummary()
        ps.comment shouldBeEqualTo ""
        ps.commentLabel shouldBeEqualTo ""
    }

    @Test
    fun headerHasNoNumberIfNumberIsNull() {
        p.number = null
        ps = newPaperSummary()
        ps.header shouldBeEqualTo "headerPart"
    }

    @Test
    fun headerOnlyShowsIdIfHeaderPartIsBlank() {
        rhf = ReportHeaderFields
            .builder("", BRAND)
            .populationLabel(POPULATION_LABEL)
            .goalsLabel(GOALS_LABEL)
            .methodsLabel(METHODS_LABEL)
            .resultLabel(RESULT_LABEL)
            .commentLabel(COMMENT_LABEL)
            .build()
        p.number = 5L
        ps = newPaperSummary()
        ps.header shouldBeEqualTo "5"
    }

    @Test
    fun equalsVerify() {
        EqualsVerifier
            .forClass(PaperSummary::class.java)
            .withRedefinedSuperclass()
            .suppress(Warning.STRICT_INHERITANCE)
            .verify()
    }
}
