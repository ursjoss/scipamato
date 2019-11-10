package ch.difty.scipamato.common.config;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Provides access to property based configuration parameters.
 *
 * @author u.joss
 */
public interface ApplicationProperties extends Serializable {

    /**
     * @return the build version of the application.
     */
    @Nullable
    String getBuildVersion();

    /**
     * @return the localization the application starts with.
     */
    @NotNull
    String getDefaultLocalization();

    /**
     * @return the brand name
     */
    @NotNull
    String getBrand();

    /**
     * @return the pageTitle if present - otherwise brand name
     */
    @NotNull
    String getTitleOrBrand();

    /**
     * @return The base url in PubMed to query by pm_id.
     */
    @NotNull
    String getPubmedBaseUrl();

    /**
     * @return The base url in PubMed to query by pm_id.
     */
    @Nullable
    Integer getRedirectFromPort();

    /**
     * @return the threshold above which a multiselectbox
     *     may activate the action box to select all/none entries
     */
    @SuppressWarnings("SpellCheckingInspection")
    int getMultiSelectBoxActionBoxWithMoreEntriesThan();

    /**
     * @return the url pointing to the CMS page with the study search page
     */
    @Nullable
    String getCmsUrlSearchPage();
}
