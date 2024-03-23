package ch.difty.scipamato.core.web.paper.jasper.referenceabstract

import ch.difty.scipamato.core.web.paper.jasper.ExportEntityTest
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields
import nl.jqno.equalsverifier.EqualsVerifier
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldThrow
import org.junit.jupiter.api.Test

internal class PaperReferenceAbstractTest : ExportEntityTest() {

    private val rhf = newReportHeaderFields()

    private lateinit var plr: PaperReferenceAbstract

    private fun newReportHeaderFields() = ReportHeaderFields(
        headerPart = HEADER_PART,
        brand = BRAND,
        numberLabel = NUMBER_LABEL,
        captionLabel = CAPTION,
        pubmedBaseUrl = PUBMED_BASE_URL,
    )

    @Test
    fun degenerateConstruction_withNullPubmedBaseUrl_throws() {
        val rhf2 = ReportHeaderFields(
            headerPart = HEADER_PART,
            brand = BRAND,
            numberLabel = NUMBER_LABEL,
            captionLabel = CAPTION,
        )
        rhf2.pubmedBaseUrl.shouldBeNull()
        invoking { PaperReferenceAbstract(p, rhf2) } shouldThrow IllegalArgumentException::class
    }

    @Test
    fun instantiatingWithValidFieldsAndValidLabels() {
        plr = PaperReferenceAbstract(p, rhf)
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
        plr.abstract shouldBeEqualTo ABSTRACT
        plr.location shouldBeEqualTo LOCATION
        plr.doi shouldBeEqualTo DOI
        plr.pubmedLink shouldBeEqualTo "baseUrl/$PM_ID"
    }

    @Test
    fun pubmedLink_withPaperWithPmIdNull_isBlank() {
        p.pmId = null
        plr = PaperReferenceAbstract(p, rhf)
        plr.pubmedLink shouldBeEqualTo ""
    }

    @Test
    fun number_withPaperWithNumberNull_isBlank() {
        p.number = null
        plr = PaperReferenceAbstract(p, rhf)
        plr.number shouldBeEqualTo ""
    }

    @Test
    fun publicationYear_withPaperWithYearNull_isBlank() {
        p.publicationYear = null
        plr = PaperReferenceAbstract(p, rhf)
        plr.publicationYear shouldBeEqualTo ""
    }

    @Test
    fun equals() {
        EqualsVerifier.simple()
            .forClass(PaperReferenceAbstract::class.java)
            .withRedefinedSuperclass()
            .withOnlyTheseFields("number")
            .verify()
    }

    @Test
    fun testingToString() {
        plr = PaperReferenceAbstract(p, rhf)
        plr.toString() shouldBeEqualTo
            "PaperReferenceAbstract(number=100, authors=authors, publicationYear=2017, title=title, abstract=abstract, " +
            "location=location, doi=doi, pubmedLink=baseUrl/1234, caption=caption, brand=brand, numberLabel=numberLabel)"
    }

    companion object {
        private const val PUBMED_BASE_URL = "baseUrl/"
    }
}
