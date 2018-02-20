package ch.difty.scipamato.common.config;

/**
 * Provides access to property based configuration parameters.
 *
 * @author u.joss
 */
public interface ApplicationProperties {

    /**
     * @return the build version of the application.
     */
    String getBuildVersion();

    /**
     * @return the localization the application starts with.
     */
    String getDefaultLocalization();

    /**
     * @return the brand name
     */
    String getBrand();

    /**
     * @return The base url in PubMed to query by pm_id.
     */
    String getPubmedBaseUrl();

    /**
     * @return The base url in PubMed to query by pm_id.
     */
    Integer getRedirectFromPort();

}
