package ch.difty.scipamato.core.web.paper.jasper.summarytable

import ch.difty.scipamato.common.entity.CodeClassId
import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.web.paper.jasper.ExportEntity
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields
import ch.difty.scipamato.core.web.paper.jasper.na

/**
 * DTO to feed the PaperSummaryTableDataSource
 */
class PaperSummaryTable(p: Paper, rhf: ReportHeaderFields) : ExportEntity {

    val number: String = p.number?.toString() ?: ""
    val firstAuthor: String = na(p.firstAuthor)
    val publicationYear: String = if (p.publicationYear != null) p.publicationYear.toString() else ""
    val codesOfClass1: String = p.getCodesOf(CodeClassId.CC1).joinToString(CODE_DELIMITER) { it.code }
    val codesOfClass4: String = p.getCodesOf(CodeClassId.CC4).joinToString(CODE_DELIMITER) { it.code }
    val codesOfClass7: String = p.getCodesOf(CodeClassId.CC7).joinToString(CODE_DELIMITER) { it.code }
    val goals: String = na(p.goals)
    val title: String = na(p.title)
    val result: String = na(p.result)
    val caption: String = na(rhf.captionLabel)
    val brand: String = na(rhf.brand)
    val numberLabel: String = na(rhf.numberLabel)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PaperSummaryTable

        if (number != other.number) return false

        return true
    }

    override fun hashCode(): Int = number.hashCode()

    override fun toString(): String =
        "PaperSummaryTable(" +
            "number=$number, firstAuthor=$firstAuthor, publicationYear=$publicationYear, " +
            "codesOfClass1=$codesOfClass1, codesOfClass4=$codesOfClass4, codesOfClass7=$codesOfClass7, " +
            "goals=$goals, title=$title, result=$result, caption=$caption, brand=$brand, numberLabel=$numberLabel" +
            ")"

    companion object {
        private const val serialVersionUID = 1L
        private const val CODE_DELIMITER = ","
    }
}
