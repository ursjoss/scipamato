package ch.difty.scipamato.common.config.core;

/**
 * Manages property based configuration parameters.
 * <ol>
 * <li> the property keys (as constants)
 * <li> default values for non-enum based properties
 * <li> the methods with which the rest of the application can access the type safe property values
 * </ol>
 * <p>
 * <b>Note:</b> Consciously applying the constant interface antipattern, I think it makes sense here....
 * @author u.joss
 */
public interface ApplicationProperties {

    String BUILD_VERSION = "build.version";
    String LOCALIZATION_DEFAULT = "scipamato.localization.default";
    String AUTHOR_PARSER_FACTORY = "scipamato.author.parser";
    String BRAND = "scipamato.brand";
    String PAPER_NUMBER_MIN_TO_RECYCLE = "scipamato.paper.number.minimum-to-be-recycled";
    String PUBMED_BASE_URL = "scipamato.pubmed-base-url";

    /**
     * @return the build version of the application.
     */
    String getBuildVersion();

    /**
     * @return the localization the application starts with.
     */
    String getDefaultLocalization();

    /**
     * @return the strategy how to interpret the author string.
     */
    AuthorParserStrategy getAuthorParserStrategy();

    /**
     * @return the brand name
     */
    String getBrand();

    /**
     * @return the lowest paper.number that can be recycled in case of gaps
     */
    long getMinimumPaperNumberToBeRecycled();

    /**
     * @return The base url in PubMed to query by pm_id.
     */
    String getPubmedBaseUrl();
}
