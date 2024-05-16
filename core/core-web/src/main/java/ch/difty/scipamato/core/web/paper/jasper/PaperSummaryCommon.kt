package ch.difty.scipamato.core.web.paper.jasper

import ch.difty.scipamato.core.entity.Paper

/**
 * Common base class for PaperSummary entities.
 */
@Suppress("LongParameterList")
abstract class PaperSummaryCommon private constructor(
    number: Long?, authors: String?, title: String?, location: String?,
    goals: String?, methods: String?, comment: String?, goalsLabel: String,
    methodsLabel: String, commentLabel: String, headerPart: String, brand: String?,
    createdBy: String?,
) : ExportEntity {
    val number: String = number?.toString() ?: ""
    val authors: String = na(authors)
    val title: String = na(title)
    val location: String = na(location)
    val goalsLabel: String = na2(goalsLabel, goals)
    val goals: String = na(goals)
    val methodsLabel: String = na2(methodsLabel, methods)
    val methods: String = na(methods)
    val commentLabel: String = na2(commentLabel, comment)
    val comment: String = na(comment)
    val header: String = makeHeader(number, headerPart)
    val brand: String = na(brand)
    val createdBy: String = na(createdBy)

    /**
     * Instantiation with a [Paper] and the [ReportHeaderFields]
     *
     * @param p the paper with the relevant fields
     * @param rhf the reportHeaderFields with the localized field headers
     */
    protected constructor(
        p: Paper,
        shortFieldConcatenator: CoreShortFieldConcatenator?, rhf: ReportHeaderFields,
    ) : this(p.number, p.authors, p.title, p.location, p.goals,
        shortFieldConcatenator?.methodsFrom(p, rhf) ?: p.methods,
        p.comment, rhf.goalsLabel!!, rhf.methodsLabel!!, rhf.commentLabel!!, rhf.headerPart,
        rhf.brand, p.createdByName ?: "n.a")

    private fun makeHeader(number: Long?, headerPart: String): String =
        StringBuilder().apply {
            append(headerPart)
            if (number != null) {
                if (isNotEmpty()) append(" ")
                append(number)
            }
        }.run { toString() }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PaperSummaryCommon

        return number == other.number
    }

    override fun hashCode(): Int = number.hashCode()

    companion object {
        private const val serialVersionUID = 1L
    }
}
