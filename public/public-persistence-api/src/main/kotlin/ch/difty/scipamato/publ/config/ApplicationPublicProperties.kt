package ch.difty.scipamato.publ.config

import ch.difty.scipamato.common.config.ApplicationProperties

interface ApplicationPublicProperties : ApplicationProperties {

    /**
     * @return whether the commercial font MetaOT can and shall be used or not
     */
    val isCommercialFontPresent: Boolean

    /**
     * @return if true, SciPaMaTo will dynamically compile the SASS files into CSS
     * classes. This is useful for development.
     */
    val isSassUsedOverCss: Boolean

    /**
     * @return the default visibility of the Navbar.
     */
    val isNavbarVisibleByDefault: Boolean

    /**
     * @return the url pointing to the CMS page with the new-study page
     */
    val cmsUrlNewStudyPage: String?

    /**
     * @return the maximum number of characters the abbreviated authors string uses.
     * if 0: no abbreviation takes place at all, return full authors string.
     */
    val authorsAbbreviatedMaxLength: Int

    /**
     * If true, pym.js will be added to the pages to support responsive iframes
     *
     * @return true to support responsive iframes, false to not support them.
     */
    val isResponsiveIframeSupportEnabled: Boolean

    /**
     * @return the username that is eligible to see the protected actuator end points.
     */
    val managementUserName: String

    /**
     * @return the password of the user that is eligible to see the protected actuator end points.
     */
    val managementUserPassword: String?

    /**
     * @return the number of previous newsletters to be listed in the archive section
     * of the new study list page.
     */
    val numberOfPreviousNewslettersInArchive: Int
}
