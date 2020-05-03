package ch.difty.scipamato.core.web.paper.jasper.literaturereview

import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.web.paper.jasper.JasperEntityTest
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields
import ch.difty.scipamato.core.web.paper.jasper.literaturereview.PaperLiteratureReview
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldThrow
import org.junit.jupiter.api.Test

internal class PaperLiteratureReviewTest : JasperEntityTest() {

    private val rhf = newReportHeaderFields()

    private lateinit var plr: PaperLiteratureReview

    private fun newReportHeaderFields(): ReportHeaderFields = ReportHeaderFields
        .builder(HEADER_PART, BRAND)
        .numberLabel(NUMBER_LABEL)
        .captionLabel(CAPTION)
        .pubmedBaseUrl(PUBMED_BASE_URL)
        .build()

    @Test
    fun degenerateConstruction_withNullPubmedBaseUrl_throws() {
        val rhf2 = ReportHeaderFields
            .builder(HEADER_PART, BRAND)
            .numberLabel(NUMBER_LABEL)
            .captionLabel(CAPTION)
            .build()
        rhf2.pubmedBaseUrl.shouldBeNull()
        invoking { PaperLiteratureReview(Paper(), rhf2) } shouldThrow NullPointerException::class
    }

    @Test
    fun instantiatingWithValidFieldsAndValidLabels() {
        plr = PaperLiteratureReview(p, rhf)
        validateFields()
        plr.caption shouldBeEqualTo CAPTION
        plr.brand shouldBeEqualTo BRAND
        plr.numberLabel shouldBeEqualTo NUMBER_LABEL
    }

    private fun validateFields() {
        plr.number shouldBeEqualTo NUMBER.toString()
        plr.authors shouldBeEqualTo AUTHORS
        plr.publicationYear shouldBeEqualTo PUBLICATION_YEAR.toString()
        plr.title shouldBeEqualTo TITLE
        plr.goals shouldBeEqualTo GOALS
        plr.location shouldBeEqualTo LOCATION
        plr.doi shouldBeEqualTo DOI
        plr.pubmedLink shouldBeEqualTo "baseUrl/$PM_ID"
    }

    @Test
    fun pubmedLink_withPaperWithPmIdNull_isBlank() {
        p.pmId = null
        plr = PaperLiteratureReview(p, rhf)
        plr.pubmedLink shouldBeEqualTo ""
    }

    @Test
    fun number_withPaperWithNumberNull_isBlank() {
        p.number = null
        plr = PaperLiteratureReview(p, rhf)
        plr.number shouldBeEqualTo ""
    }

    @Test
    fun publicationYear_withPaperWithYearNull_isBlank() {
        p.publicationYear = null
        plr = PaperLiteratureReview(p, rhf)
        plr.publicationYear shouldBeEqualTo ""
    }

    @Test
    fun equals() {
        EqualsVerifier
            .forClass(PaperLiteratureReview::class.java)
            .withRedefinedSuperclass()
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify()
    }

    @Test
    fun testingToString() {
        plr = PaperLiteratureReview(p, rhf)
        plr.toString() shouldBeEqualTo
            "PaperLiteratureReview(number=100, authors=authors, publicationYear=2017, title=title, goals=goals, " +
            "location=location, doi=doi, pubmedLink=baseUrl/1234, caption=caption, brand=brand, numberLabel=numberLabel)"
    }

    companion object {
        private const val PUBMED_BASE_URL = "baseUrl/"
    }
}
