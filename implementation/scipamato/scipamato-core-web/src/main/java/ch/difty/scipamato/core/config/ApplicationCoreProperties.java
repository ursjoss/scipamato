package ch.difty.scipamato.core.config;

import ch.difty.scipamato.common.config.ApplicationProperties;

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
    AuthorParserStrategy getAuthorParserStrategy();

    /**
     * @return the lowest paper.number that can be recycled in case of gaps
     */
    long getMinimumPaperNumberToBeRecycled();
}
