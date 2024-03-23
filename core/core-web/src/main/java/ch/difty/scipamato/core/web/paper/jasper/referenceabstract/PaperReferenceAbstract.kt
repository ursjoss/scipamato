package ch.difty.scipamato.core.web.paper.jasper.referenceabstract

import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.web.paper.jasper.ExportEntity
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields
import ch.difty.scipamato.core.web.paper.jasper.na

/**
 * DTO to feed the PaperReferenceAbstractDataSource
 */
class PaperReferenceAbstract(p: Paper, rhf: ReportHeaderFields) : ExportEntity {

    init {
        requireNotNull(rhf.pubmedBaseUrl)
    }

    val number: String = na(p.number?.toString())
    val authors: String = na(p.authors)
    val publicationYear: String = if (p.publicationYear != null) p.publicationYear.toString() else ""
    val title: String = na(p.title)
    val abstract: String = na(p.originalAbstract)
    val location: String = na(p.location)
    val doi: String = na(p.doi)
    val pubmedLink: String = makePubmedLink(rhf.pubmedBaseUrl ?: "", p.pmId)
    val caption: String = na(rhf.captionLabel)
    val brand: String = na(rhf.brand)
    val numberLabel: String = na(rhf.numberLabel)

    private fun makePubmedLink(pubmedBaseUrl: String, pmId: Int?) =
        if (pmId != null) "$pubmedBaseUrl$pmId" else ""

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PaperReferenceAbstract

        if (number != other.number) return false

        return true
    }

    override fun hashCode(): Int = number.hashCode()
    override fun toString(): String = "PaperReferenceAbstract(" +
        "number=$number, authors=$authors, publicationYear=$publicationYear, title=$title, abstract=$abstract, location=$location, " +
        "doi=$doi, pubmedLink=$pubmedLink, caption=$caption, brand=$brand, numberLabel=$numberLabel" +
        ")"

    companion object {
        private const val serialVersionUID = 1L
    }

}
