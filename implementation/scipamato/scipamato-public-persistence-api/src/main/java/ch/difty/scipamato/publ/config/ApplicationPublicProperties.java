package ch.difty.scipamato.publ.config;

import ch.difty.scipamato.common.config.ApplicationProperties;

public interface ApplicationPublicProperties extends ApplicationProperties {

    /**
     * @return whether the commercial font MetaOT can and shall be used or not
     */
    boolean isCommercialFontPresent();

    /**
     * @return if true, SciPaMaTo will dynamically compile the LESS files into CSS
     *         classes. This is useful for development.
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
}
