package ch.difty.scipamato.core.config

import ch.difty.scipamato.common.config.MavenProperties
import ch.difty.scipamato.core.logic.exporting.RisExporterStrategy
import ch.difty.scipamato.core.logic.parsing.AuthorParserStrategy
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class ScipamatoCorePropertiesTest {

    private lateinit var prop: ScipamatoCoreProperties

    @MockK
    private lateinit var scipamatoPropMock: ScipamatoProperties

    @MockK
    private lateinit var mavenPropMock: MavenProperties

    @BeforeEach
    fun setUp() {
        prop = ScipamatoCoreProperties(scipamatoPropMock, mavenPropMock)
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(scipamatoPropMock, mavenPropMock)
    }

    @Test
    fun gettingBrand_delegatesToScipamatoProps() {
        every { scipamatoPropMock.brand } returns "brand"
        prop.brand shouldBeEqualTo "brand"
        verify { scipamatoPropMock.brand }
    }

    @Test
    fun gettingTitleOrBrand_withPageTitleDefined_delegatesToScipamatoProps_andReturnsPageTitle() {
        every { scipamatoPropMock.pageTitle } returns "pt"
        prop.titleOrBrand shouldBeEqualTo "pt"
        verify { scipamatoPropMock.pageTitle }
        verify(exactly = 0) { scipamatoPropMock.brand }
    }

    @Test
    fun gettingTitleOrBrand_withPageTitleNotDefined_delegatesToScipamatoProps_andReturnsBrand() {
        every { scipamatoPropMock.brand } returns "brand"
        every { scipamatoPropMock.pageTitle } returns null
        prop.titleOrBrand shouldBeEqualTo "brand"
        verify { scipamatoPropMock.pageTitle }
        verify { scipamatoPropMock.brand }
    }

    @Test
    fun gettingDefaultLocalization_delegatesToScipamatoProps() {
        every { scipamatoPropMock.defaultLocalization } returns "dl"
        prop.defaultLocalization shouldBeEqualTo "dl"
        verify { scipamatoPropMock.defaultLocalization }
    }

    @Test
    fun gettingPubmedBaseUrl_delegatesToScipamatoProps() {
        every { scipamatoPropMock.pubmedBaseUrl } returns "pbUrl"
        prop.pubmedBaseUrl shouldBeEqualTo "pbUrl"
        verify { scipamatoPropMock.pubmedBaseUrl }
    }

    @Test
    fun gettingBuildVersion_delegatesToMavenProp() {
        every { mavenPropMock.version } returns "0.0.1-SNAPSHOT"
        prop.buildVersion shouldBeEqualTo "0.0.1-SNAPSHOT"
        verify { mavenPropMock.version }
    }

    @Test
    fun gettingAuthorParserStrategy_delegatesToScipamatoProps() {
        every { scipamatoPropMock.authorParserStrategy } returns AuthorParserStrategy.PUBMED
        prop.authorParserStrategy shouldBeEqualTo AuthorParserStrategy.PUBMED
        verify { scipamatoPropMock.authorParserStrategy }
    }

    @Test
    fun gettingRisExporterStrategy_delegatesToScipamatoProps() {
        every { scipamatoPropMock.risExporterStrategy } returns RisExporterStrategy.DISTILLERSR
        prop.risExporterStrategy shouldBeEqualTo RisExporterStrategy.DISTILLERSR
        verify { scipamatoPropMock.risExporterStrategy }
    }

    @Test
    fun gettingPaperNumberMin2BeRecycled_delegatesToScipamatoProps() {
        every { scipamatoPropMock.paperNumberMinimumToBeRecycled } returns 100
        prop.minimumPaperNumberToBeRecycled shouldBeEqualTo 100
        verify { scipamatoPropMock.paperNumberMinimumToBeRecycled }
    }

    @Test
    fun gettingRedirectFromPort_delegatesToScipamatoProps() {
        every { scipamatoPropMock.redirectFromPort } returns 5678
        prop.redirectFromPort shouldBeEqualTo 5678
        verify { scipamatoPropMock.redirectFromPort }
    }

    @Test
    fun gettingPubmedApiKey_() {
        every { scipamatoPropMock.pubmedApiKey } returns "ak"
        prop.pubmedApiKey shouldBeEqualTo "ak"
        verify { scipamatoPropMock.pubmedApiKey }
    }
}
