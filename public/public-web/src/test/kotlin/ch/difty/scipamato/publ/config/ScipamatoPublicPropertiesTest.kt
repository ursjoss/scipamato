package ch.difty.scipamato.publ.config

import ch.difty.scipamato.common.config.MavenProperties
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@Suppress("SpellCheckingInspection")
internal class ScipamatoPublicPropertiesTest {

    private lateinit var prop: ScipamatoPublicProperties

    private lateinit var scipamatoPropMock: ScipamatoProperties
    private lateinit var mavenPropMock: MavenProperties

    @BeforeEach
    fun setUp() {
        scipamatoPropMock = mockk()
        mavenPropMock = mockk()
        prop = ScipamatoPublicProperties(scipamatoPropMock, mavenPropMock)
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
    fun checkingCommercialFontPresence_ifPresent_delegatesToMavenProp() {
        every { scipamatoPropMock.commercialFontPresent } returns true
        prop.isCommercialFontPresent shouldBeEqualTo true
        verify { scipamatoPropMock.commercialFontPresent }
    }

    @Test
    fun checkingCommercialFontPresence_ifNotPresent_delegatesToMavenProp() {
        every { scipamatoPropMock.commercialFontPresent } returns false
        prop.isCommercialFontPresent shouldBeEqualTo false
        verify { scipamatoPropMock.commercialFontPresent }
    }

    @Test
    fun gettingRedirectFromPort_delegatesToScipamatoProp() {
        every { scipamatoPropMock.redirectFromPort } returns 5678
        prop.redirectFromPort shouldBeEqualTo 5678
        verify { scipamatoPropMock.redirectFromPort }
    }

    @Test
    fun gettingBuildVersion_delegatesToMavenProp() {
        every { mavenPropMock.version } returns "0.0.1-SNAPSHOT"
        prop.buildVersion shouldBeEqualTo "0.0.1-SNAPSHOT"
        verify { mavenPropMock.version }
    }

    @Test
    fun checkingLessOverCSS_ifTrue_delegatesToScipamatoProp() {
        every { scipamatoPropMock.lessUsedOverCss } returns true
        prop.isLessUsedOverCss shouldBeEqualTo true
        verify { scipamatoPropMock.lessUsedOverCss }
    }

    @Test
    fun checkingLessOverCSS_ifNotPresent_delegatesToScipamatoProp() {
        every { scipamatoPropMock.lessUsedOverCss } returns false
        prop.isLessUsedOverCss shouldBeEqualTo false
        verify { scipamatoPropMock.lessUsedOverCss }
    }

    @Test
    fun checkingNavbarDefaultVisibility_delegatesToScipamatoProp() {
        every { scipamatoPropMock.navbarVisibleByDefault } returns true
        prop.isNavbarVisibleByDefault shouldBeEqualTo true
        verify { scipamatoPropMock.navbarVisibleByDefault }
    }

    @Test
    fun checkingCssUrlSearchPage_delegatesToScipamatoProp() {
        every { scipamatoPropMock.cmsUrlSearchPage } returns "https://u.sp"
        prop.cmsUrlSearchPage shouldBeEqualTo "https://u.sp"
        verify { scipamatoPropMock.cmsUrlSearchPage }
    }

    @Test
    fun checkingCssUrlNewStudyPage_delegatesToScipamatoProp() {
        every { scipamatoPropMock.cmsUrlNewStudyPage } returns "https://u.nsp"
        prop.cmsUrlNewStudyPage shouldBeEqualTo "https://u.nsp"
        verify { scipamatoPropMock.cmsUrlNewStudyPage }
    }

    @Test
    fun checkingAuthorsAbbreviatedMaxLength() {
        every { scipamatoPropMock.authorsAbbreviatedMaxLength } returns 70
        prop.authorsAbbreviatedMaxLength shouldBeEqualTo 70
        verify { scipamatoPropMock.authorsAbbreviatedMaxLength }
    }

    @Test
    fun checkingManagementUserName_delegatesToScipamatoProp() {
        every { scipamatoPropMock.managementUserName } returns "un"
        prop.managementUserName shouldBeEqualTo "un"
        verify { scipamatoPropMock.managementUserName }
    }

    @Test
    fun checkingManagementPassword_delegatesToScipamatoProp() {
        every { scipamatoPropMock.managementUserPassword } returns "pw"
        prop.managementUserPassword shouldBeEqualTo "pw"
        verify { scipamatoPropMock.managementUserPassword }
    }

    @Test
    fun checkingNumberOfPreviousNewslettersInArchive() {
        every { scipamatoPropMock.numberOfPreviousNewslettersInArchive } returns 14
        prop.numberOfPreviousNewslettersInArchive shouldBeEqualTo 14
        verify { scipamatoPropMock.numberOfPreviousNewslettersInArchive }
    }
}
