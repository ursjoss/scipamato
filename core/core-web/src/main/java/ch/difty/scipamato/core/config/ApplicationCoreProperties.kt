package ch.difty.scipamato.core.config

import ch.difty.scipamato.common.config.ApplicationProperties
import ch.difty.scipamato.core.logic.exporting.RisExporterStrategy
import ch.difty.scipamato.core.logic.parsing.AuthorParserStrategy

/**
 * Extending the generic [ApplicationProperties] with SciPaMaTo-Core specific properties.
 */
interface ApplicationCoreProperties : ApplicationProperties {
    /** @return the strategy how to interpret the author string. */
    val authorParserStrategy: AuthorParserStrategy

    /** @return the lowest paper.number that can be recycled in case of gaps */
    val minimumPaperNumberToBeRecycled: Long

    /** @return the pubmed api_key or null if none is defined */
    val pubmedApiKey: String?

    /** @return the strategy how to export RIS files. */
    val risExporterStrategy: RisExporterStrategy
}
