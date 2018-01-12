package ch.difty.scipamato.common.config;

/**
 * Manages property based configuration parameters.
 * <ol>
 * <li>the property keys (as constants)</li>
 * <li>default values for non-enum based properties</li>
 * <li>the methods with which the rest of the application can access the type
 * safe property values</li>
 * </ol>
 * <p>
 * <b>Note:</b> Consciously applying the constant interface anti-pattern, I
 * think it makes sense here....
 *
 * @author u.joss
 */
public interface ApplicationProperties {

    String BUILD_VERSION        = "build.version";
    String LOCALIZATION_DEFAULT = "scipamato.default-localization";
    String BRAND                = "scipamato.brand";
    String PUBMED_BASE_URL      = "scipamato.pubmed-base-url";
    String REDIRECT_FROM_PORT   = "scipamato.redirect-from-port";

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
