package ch.difty.scipamato.publ.config;

import ch.difty.scipamato.common.config.ApplicationProperties;

public interface ApplicationPublicProperties extends ApplicationProperties {

    /**
     * @return whether the commercial font MetaOT can and shall be used or not
     */
    boolean isCommercialFontPresent();

    /**
     * @return if true, SciPaMaTo will dynamically compile the LESS files into CSS
     *     classes. This is useful for development.
     */
    boolean isLessUsedOverCss();

    /**
     * @return the default visibility of the Navbar.
     */
    boolean isNavbarVisibleByDefault();

    /**
     * @return the url pointing to the CMS page with the study search page
     */
    String getCmsUrlSearchPage();

    /**
     * @return the url pointing to the CMS page with the new-study page
     */
    String getCmsUrlNewStudyPage();

    /**
     * @return the maximum number of characters the abbreviated authors string uses.
     *     if 0: no abbreviation takes place at all, return full authors string.
     */
    int getAuthorsAbbreviatedMaxLength();

    /**
     * If true, pym.js will be added to the pages to support responsive iframes
     *
     * @return true to support responsive iframes, false to not support them.
     */
    boolean isResponsiveIframeSupportEnabled();

    /**
     * @return the user name that is eligible to see the protected actuator end points.
     */
    String getManagementUserName();

    /**
     * @return the password of the user that is eligible to see the protected actuator end points.
     */
    String getManagementUserPassword();

    /**
     * @return the number of previous newsletters to be listed in the archive section
     *     of the new study list page.
     */
    int getNumberOfPreviousNewslettersInArchive();

}
