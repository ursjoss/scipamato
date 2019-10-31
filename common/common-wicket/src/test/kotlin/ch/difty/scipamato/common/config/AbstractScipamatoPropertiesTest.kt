package ch.difty.scipamato.common.config

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions

internal class AbstractScipamatoPropertiesTest {

    private val scipamatoPropMock = mock<ScipamatoBaseProperties>()
    private val mavenPropMock = mock<MavenProperties>()
    private val prop =
        object : AbstractScipamatoProperties<ScipamatoBaseProperties>(scipamatoPropMock, mavenPropMock) {}

    @AfterEach
    fun tearDown() {
        verifyNoMoreInteractions(scipamatoPropMock, mavenPropMock)
    }

    @Test
    fun gettingBrand_delegatesToScipamatoProps() {
        whenever(scipamatoPropMock.brand).thenReturn("brand")
        assertThat(prop.brand).isEqualTo("brand")
        verify(scipamatoPropMock).brand
    }

    @Test
    fun gettingTitleOrBrand_withPageTitleDefined_delegatesToScipamatoProps_andReturnsPageTitle() {
        whenever(scipamatoPropMock.pageTitle).thenReturn("pt")
        assertThat(prop.titleOrBrand).isEqualTo("pt")
        verify(scipamatoPropMock).pageTitle
        verify(scipamatoPropMock, never()).brand
    }

    @Test
    fun gettingTitleOrBrand_withPageTitleNotDefined_delegatesToScipamatoProps_andReturnsBrand() {
        whenever(scipamatoPropMock.brand).thenReturn("brand")
        whenever(scipamatoPropMock.pageTitle).thenReturn(null)
        assertThat(prop.titleOrBrand).isEqualTo("brand")
        verify(scipamatoPropMock).pageTitle
        verify(scipamatoPropMock).brand
    }

    @Test
    fun gettingDefaultLocalization_delegatesToScipamatoProps() {
        whenever(scipamatoPropMock.defaultLocalization).thenReturn("dl")
        assertThat(prop.defaultLocalization).isEqualTo("dl")
        verify(scipamatoPropMock).defaultLocalization
    }

    @Test
    fun gettingPubmedBaseUrl_delegatesToScipamatoProps() {
        whenever(scipamatoPropMock.pubmedBaseUrl).thenReturn("pbUrl")
        assertThat(prop.pubmedBaseUrl).isEqualTo("pbUrl")
        verify(scipamatoPropMock).pubmedBaseUrl
    }

    @Test
    fun gettingCmsBaseUrl_delegatesToScipamatoProps() {
        whenever(scipamatoPropMock.cmsUrlSearchPage).thenReturn("cmsUrl")
        assertThat(prop.cmsUrlSearchPage).isEqualTo("cmsUrl")
        verify(scipamatoPropMock).cmsUrlSearchPage
    }

    @Test
    fun gettingBuildVersion_delegatesToMavenProp() {
        whenever(mavenPropMock.version).thenReturn("0.0.1-SNAPSHOT")
        assertThat(prop.buildVersion).isEqualTo("0.0.1-SNAPSHOT")
        verify(mavenPropMock).version
    }

    @Test
    fun gettingRedirectFromPort_delegatesToScipamatoProps() {
        whenever(scipamatoPropMock.redirectFromPort).thenReturn(5678)
        assertThat(prop.redirectFromPort).isEqualTo(5678)
        verify(scipamatoPropMock).redirectFromPort
    }

    @Test
    fun gettingMultiSelectBoxActionBoxWithMoreEntriesThan_delegatesToScipamatoProps() {
        whenever(scipamatoPropMock.multiSelectBoxActionBoxWithMoreEntriesThan).thenReturn(4)
        assertThat(prop.multiSelectBoxActionBoxWithMoreEntriesThan).isEqualTo(4)
        verify(scipamatoPropMock).multiSelectBoxActionBoxWithMoreEntriesThan
    }
}
