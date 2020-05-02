package ch.difty.scipamato.common.config

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

internal class AbstractScipamatoPropertiesTest {

    private val scipamatoPropMock = mockk<ScipamatoBaseProperties>()
    private val mavenPropMock = mockk<MavenProperties>()
    private val prop =
        object : AbstractScipamatoProperties<ScipamatoBaseProperties>(scipamatoPropMock, mavenPropMock) {}

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
    fun gettingCmsBaseUrl_delegatesToScipamatoProps() {
        every { scipamatoPropMock.cmsUrlSearchPage } returns "cmsUrl"
        prop.cmsUrlSearchPage shouldBeEqualTo "cmsUrl"
        verify { scipamatoPropMock.cmsUrlSearchPage }
    }

    @Test
    fun gettingBuildVersion_delegatesToMavenProp() {
        every { mavenPropMock.version } returns "0.0.1-SNAPSHOT"
        prop.buildVersion shouldBeEqualTo "0.0.1-SNAPSHOT"
        verify { mavenPropMock.version }
    }

    @Test
    fun gettingRedirectFromPort_delegatesToScipamatoProps() {
        every { scipamatoPropMock.redirectFromPort } returns 5678
        prop.redirectFromPort shouldBeEqualTo 5678
        verify { scipamatoPropMock.redirectFromPort }
    }

    @Test
    fun gettingMultiSelectBoxActionBoxWithMoreEntriesThan_delegatesToScipamatoProps() {
        every { scipamatoPropMock.multiSelectBoxActionBoxWithMoreEntriesThan } returns 4
        prop.multiSelectBoxActionBoxWithMoreEntriesThan shouldBeEqualTo 4
        verify { scipamatoPropMock.multiSelectBoxActionBoxWithMoreEntriesThan }
    }
}
