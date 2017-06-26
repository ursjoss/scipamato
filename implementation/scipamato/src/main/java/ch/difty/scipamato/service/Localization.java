package ch.difty.scipamato.service;

import java.io.Serializable;

/**
 * Bean to provide access (getting/setting) of the localization of the application.
 * If nothing is set, the bean will be initiated with a default localization defined externally.
 *
 * @author u.joss
 */
public interface Localization extends Serializable {

    /**
     * @return the default localization as initialized during application startup
     */
    String getDefaultLocalization();

    /**
     * Sets the localization during the application run time.
     *
     * @param localization two letter String, e.g. "de"
     */
    void setLocalization(String localization);

    /**
     * @return the localization either as initialized during startup or as specified during the application life time.
     */
    String getLocalization();

}
