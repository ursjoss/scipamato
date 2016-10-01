package ch.difty.sipamato.config;

/**
 * Manages property based configuration parameters.
 * <ol>
 * <li> the property keys (as constants)
 * <li> default values for non-enum based properties
 * <li> the methods with which the rest of the application can access the type safe property values
 * </ol>
 * @author u.joss
 */
public interface ApplicationProperties {

    String AUTHOR_PARSER_FACTORY = "sipamato.author.parser";
    String PAPER_AUTO_SAVE_INTERVAL = "sipamato.paper.autosave.interval";

    int DEFAULT_PAPER_AUTO_SAVE_INTERVAL_IN_SECONDS = 0;

    /**
     * Defines the strategy how to interpret the author string.
     *
     * @return
     */
    AuthorParserStrategy getAuthorParserStrategy();

    /**
     * The auto save interval on the paper edit page.
     * <ul>
     * <li>   0: no auto saving enabled -> resort to submit based saving
     * <li> >=1: auto saving every x seconds (if there are changes)
     * </ul>
     * @return auto save interval in seconds
     */
    int getPaperAutoSaveInterval();

    /**
     * @return shall papers be auto saved - or not?
     */
    boolean isPaperAutoSaveMode();

}
