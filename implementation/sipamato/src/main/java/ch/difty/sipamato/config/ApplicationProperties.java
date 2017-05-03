package ch.difty.sipamato.config;

/**
 * Manages property based configuration parameters.
 * <ol>
 * <li> the property keys (as constants)
 * <li> default values for non-enum based properties
 * <li> the methods with which the rest of the application can access the type safe property values
 * </ol>
 * <p>
 * <b>Note:</b> Consiously applying the constant interface antipattern, I think it makes sense here....
 * @author u.joss
 */
public interface ApplicationProperties {

    String LOCALIZATION_DEFAULT = "sipamato.localization.default";
    String AUTHOR_PARSER_FACTORY = "sipamato.author.parser";
    String BRAND = "sipamato.brand";
    String PAPER_NUMBER_MIN_TO_RECYCLE = "sipamato.paper.number.minimum-to-be-recycled";

    /**
     * Defines the localization the application starts with.
     *
     * @return
     */
    String getDefaultLocalization();

    /**
     * Defines the strategy how to interpret the author string.
     *
     * @return
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
}
