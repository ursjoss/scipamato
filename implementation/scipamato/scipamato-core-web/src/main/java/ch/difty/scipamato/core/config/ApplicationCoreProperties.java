package ch.difty.scipamato.core.config;

import ch.difty.scipamato.common.config.ApplicationProperties;

public interface ApplicationCoreProperties extends ApplicationProperties {

    String AUTHOR_PARSER_FACTORY       = "scipamato.author-parser";
    String PAPER_NUMBER_MIN_TO_RECYCLE = "scipamato.paper-number-minimum-to-be-recycled";

    /**
     * @return the strategy how to interpret the author string.
     */
    AuthorParserStrategy getAuthorParserStrategy();

    /**
     * @return the lowest paper.number that can be recycled in case of gaps
     */
    long getMinimumPaperNumberToBeRecycled();
}
