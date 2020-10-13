package ch.difty.scipamato.common.config

import java.io.Serializable

/**
 * Provides access to property based configuration parameters.
 *
 * @author u.joss
 */
interface ApplicationProperties : Serializable {
    /**
     * @return the build version of the application.
     */
    val buildVersion: String?

    /**
     * @return the localization the application starts with.
     */
    val defaultLocalization: String

    /**
     * @return the brand name
     */
    val brand: String

    /**
     * @return the pageTitle if present - otherwise brand name
     */
    val titleOrBrand: String

    /**
     * @return The base url in PubMed to query by pm_id.
     */
    val pubmedBaseUrl: String

    /**
     * @return The port from where an unsecured http connection is forward to the secured port.
     */
    val redirectFromPort: Int?

    /**
     * @return the threshold above which a multiselectbox
     * may activate the action box to select all/none entries
     */
    val multiSelectBoxActionBoxWithMoreEntriesThan: Int

    /**
     * @return the url pointing to the CMS page with the study search page
     */
    val cmsUrlSearchPage: String?
}
