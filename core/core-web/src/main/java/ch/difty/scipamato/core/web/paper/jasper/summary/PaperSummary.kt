package ch.difty.scipamato.core.web.paper.jasper.summary

import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.web.paper.jasper.CoreShortFieldConcatenator
import ch.difty.scipamato.core.web.paper.jasper.PaperSummaryCommon
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields
import ch.difty.scipamato.core.web.paper.jasper.na

/**
 * DTO to feed the PaperSummaryDataSource
 */
class PaperSummary(
    p: Paper,
    shortFieldConcatenator: CoreShortFieldConcatenator,
    rhf: ReportHeaderFields,
) : PaperSummaryCommon(p, shortFieldConcatenator, rhf) {

    val population: String = na(shortFieldConcatenator.populationFrom(p, rhf))
    val result: String = na(shortFieldConcatenator.resultFrom(p, rhf))
    val populationLabel: String = na(rhf.populationLabel, population)
    val resultLabel: String = na(rhf.resultLabel, result)

    companion object {
        private const val serialVersionUID = 1L
    }
}
