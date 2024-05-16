package ch.difty.scipamato.core.config

import ch.difty.scipamato.common.config.ScipamatoBaseProperties
import ch.difty.scipamato.core.logic.exporting.RisExporterStrategy
import ch.difty.scipamato.core.logic.parsing.AuthorParserStrategy
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "scipamato")
data class ScipamatoProperties(
    /**
     * Brand name of the application. Appears e.g. in the Navbar.
     */
    override var brand: String = "SciPaMaTo-Core",

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
     * The author parser used for parsing Author Strings. Currently only
     * DEFAULT is implemented.
     */
    var authorParser: String = "DEFAULT",

    /**
     * The ris export adapter used for exporting studies into RIS format. Currently only
     * DEFAULT and DISTILLERSR is implemented.
     */
    var risExporter: String = "DEFAULT",

    /**
     * Any free number below this threshold will not be reused.
     */
    var paperNumberMinimumToBeRecycled: Int = 0,

    /**
     * DB Schema.
     */
    var dbSchema: String = "public",

    /**
     * Port from where an unsecured http connection is forwarded to the secured
     * https port `server.port`. Only has an effect if https is configured.
     */
    override var redirectFromPort: Int? = null,

    /**
     * The URL of the CMS page that points to the paper search page
     */
    override var cmsUrlSearchPage: String? = null,

    /**
     * @return the author parser strategy used for interpreting the authors string.
     */
    var authorParserStrategy: AuthorParserStrategy =
        AuthorParserStrategy.fromProperty(authorParser, AUTHOR_PARSER_PROPERTY_KEY),

    /**
     * @return the strategy for exporting studies into RIS file format.
     */
    var risExporterStrategy: RisExporterStrategy =
        RisExporterStrategy.fromProperty(risExporter, RIS_EXPORTER_PROPERTY_KEY),

    /**
     * The threshold above which the multi-select box may (if configured) show the
     * action box providing the select all/select none buttons
     */
    override var multiSelectBoxActionBoxWithMoreEntriesThan: Int = 4,

    /**
     * The API Key used for accessing pubmed papers.
     *
     *
     * https://ncbiinsights.ncbi.nlm.nih.gov/2017/11/02/new-api-keys-for-the-e-utilities/
     */
    var pubmedApiKey: String? = null,

    ) : ScipamatoBaseProperties {

    companion object {
        private const val serialVersionUID = 1L
        private const val AUTHOR_PARSER_PROPERTY_KEY = "scipamato.author-parser"
        private const val RIS_EXPORTER_PROPERTY_KEY = "scipamato.ris-exporter"
    }
}
