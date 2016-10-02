package ch.difty.sipamato.config;

import ch.difty.sipamato.web.pages.AutoSaveAwarePage;

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
    String AUTO_SAVE_INTERVAL = "sipamato.autosave.interval.seconds";

    int DEFAULT_AUTO_SAVE_INTERVAL_IN_SECONDS = 0;
    String AUTO_SAVE_HINT = "0: auto-saving disabled, >=1: save interval in seconds";

    /**
     * Defines the strategy how to interpret the author string.
     *
     * @return
     */
    AuthorParserStrategy getAuthorParserStrategy();

    /**
     * The auto-save interval for pages extending {@link AutoSaveAwarePage}s.
     * <ul>
     * <li>   0: auto saving disabled -> resort to pure submit based saving
     * <li> >=1: auto saving enabled every x seconds - provided the form is dirty, i.e. there are changes
     * </ul>
     * @return auto save interval in seconds
     */
    int getAutoSaveIntervalInSeconds();

    /**
     * @return shall pages extending {@link AutoSaveAwarePage} activate auto-saving - or not?
     */
    boolean isAutoSavingEnabled();

}
