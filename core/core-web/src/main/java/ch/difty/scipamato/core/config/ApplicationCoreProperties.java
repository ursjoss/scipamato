package ch.difty.scipamato.core.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.config.ApplicationProperties;
import ch.difty.scipamato.core.logic.exporting.RisExporterStrategy;
import ch.difty.scipamato.core.logic.parsing.AuthorParserStrategy;

/**
 * Extending the generic {@link ApplicationProperties} with SciPaMaTo-Core
 * specific properties.
 *
 * @author Urs Joss
 */
public interface ApplicationCoreProperties extends ApplicationProperties {

    /**
     * @return the strategy how to interpret the author string.
     */
    @NotNull
    AuthorParserStrategy getAuthorParserStrategy();

    /**
     * @return the lowest paper.number that can be recycled in case of gaps
     */
    long getMinimumPaperNumberToBeRecycled();

    /**
     * @return the pubmed api_key or null if none is defined
     */
    @Nullable
    String getPubmedApiKey();

    /**
     * @return the strategy how to export RIS files.
     */
    @NotNull
    RisExporterStrategy getRisExporterStrategy();
}
