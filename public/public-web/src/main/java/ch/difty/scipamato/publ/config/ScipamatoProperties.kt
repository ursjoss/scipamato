package ch.difty.scipamato.publ.config

import ch.difty.scipamato.common.config.ScipamatoBaseProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "scipamato")
data class ScipamatoProperties(
    /**
     * Brand name of the application. Appears e.g. in the Navbar.
     */
    override var brand: String = "SciPaMaTo-Public",

    /**
     * Page Title of the application. Appears in the browser tab.
     */
    override var pageTitle: String? = null,

    /**
     * Default localization. Normally the browser locale is used.
     */
    override var defaultLocalization: String = "en",

    /**
     * The base url used to access the Pubmed API.
     */
    override var pubmedBaseUrl: String = "https://www.ncbi.nlm.nih.gov/pubmed/",

    /**
     * Port from where an unsecured http connection is forwarded to the secured
     * https port (@literal server.port}. Only has an effect if https is configured.
     */
    override var redirectFromPort: Int? = null,

    /**
     * Indicates whether the commercial font MetaOT is present and can be used.
     *
     *
     * SciPaMaTo expects to find the font in the following location:
     *
     * <pre>
     * src/main/resources/ch/difty/scipamato/publ/web/resources/fonts/MetaOT/
    </pre> *
     */
    var commercialFontPresent: Boolean = false,

    /**
     * Indicates whether SciPaMaTo should compile the LESS sources into CSS (if
     * true) or use the pre-compiled CSS classes (if false).
     */
    var lessUsedOverCss: Boolean = false,

    /**
     * Indicates if the Navbar is visible by default. Can be overridden via page
     * parameter.
     */
    var navbarVisibleByDefault: Boolean = false,

    /**
     * The URL of the CMS page that points to the paper search page
     */
    override var cmsUrlSearchPage: String? = null,

    /**
     * The URL of the CMS page that points to the new study page
     */
    var cmsUrlNewStudyPage: String? = null,

    /**
     * The number of characters an abbreviated authors string will have at most. If
     * set to 0: do not abbreviate at all, return full authors string.
     */
    var authorsAbbreviatedMaxLength: Int = 0,

    /**
     * set to true if SciPaMaTo-Public needs to add pym.js to its page headers. This
     * allows embedding scipamato into responsive iframes.
     */
    var responsiveIframeSupportEnabled: Boolean = false,

    /**
     * The user with which you can authenticate to see the actuator end points.
     */
    var managementUserName: String = "admin",

    /**
     * The password of the user with which you can authenticate to see the actuator end points.
     */
    var managementUserPassword: String? = null,

    /**
     * The new study page will list that many newsletters in the archive section (default: 14)
     */
    var numberOfPreviousNewslettersInArchive: Int = 14,

    /**
     * The threshold above which the multi-select box may activate the actionBox (all/none buttons)
     */
    override var multiSelectBoxActionBoxWithMoreEntriesThan: Int = 4,

    ) : ScipamatoBaseProperties {

    companion object {
        private const val serialVersionUID = 1L
    }
}
