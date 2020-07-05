package ch.difty.scipamato.core.config

import ch.difty.scipamato.common.config.MavenProperties
import ch.difty.scipamato.core.logic.exporting.RisExporterStrategy
import ch.difty.scipamato.core.logic.parsing.AuthorParserStrategy
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

@Suppress("SpellCheckingInspection")
internal class ScipamatoCorePropertiesTest {

    private val scipamatoProp = ScipamatoProperties().apply {
        brand = "brand"
        pageTitle = "pt"
        defaultLocalization = "dl"
        pubmedBaseUrl = "pbUrl"
        paperNumberMinimumToBeRecycled = 100
        redirectFromPort = 5678
        pubmedApiKey = "ak"
        risExporter = RisExporterStrategy.DISTILLERSR.name
    }

    private val mavenProp = MavenProperties().apply {
        version = "0.0.1-SNAPSHOT"
    }

    private val prop = ScipamatoCoreProperties(scipamatoProp, mavenProp)

    @Test
    fun gettingBrand_delegatesToScipamatoProps() {
        prop.brand shouldBeEqualTo "brand"
    }

    @Test
    fun gettingTitleOrBrand_withPageTitleDefined_delegatesToScipamatoProps_andReturnsPageTitle() {
        prop.titleOrBrand shouldBeEqualTo "pt"
    }

    @Test
    fun gettingTitleOrBrand_withPageTitleNotDefined_delegatesToScipamatoProps_andReturnsBrand() {
        scipamatoProp.pageTitle = null
        prop.titleOrBrand shouldBeEqualTo "brand"
    }

    @Test
    fun gettingDefaultLocalization_delegatesToScipamatoProps() {
        prop.defaultLocalization shouldBeEqualTo "dl"
    }

    @Test
    fun gettingPubmedBaseUrl_delegatesToScipamatoProps() {
        prop.pubmedBaseUrl shouldBeEqualTo "pbUrl"
    }

    @Test
    fun gettingBuildVersion_delegatesToMavenProp() {
        prop.buildVersion shouldBeEqualTo "0.0.1-SNAPSHOT"
    }

    @Test
    fun gettingAuthorParserStrategy_delegatesToScipamatoProps() {
        prop.authorParserStrategy shouldBeEqualTo AuthorParserStrategy.PUBMED
    }

    @Test
    fun gettingRisExporterStrategy_delegatesToScipamatoProps() {
        prop.risExporterStrategy shouldBeEqualTo RisExporterStrategy.DISTILLERSR
    }

    @Test
    fun gettingPaperNumberMin2BeRecycled_delegatesToScipamatoProps() {
        prop.minimumPaperNumberToBeRecycled shouldBeEqualTo 100
    }

    @Test
    fun gettingRedirectFromPort_delegatesToScipamatoProps() {
        prop.redirectFromPort shouldBeEqualTo 5678
    }

    @Test
    fun gettingPubmedApiKey_() {
        prop.pubmedApiKey shouldBeEqualTo "ak"
    }
}
