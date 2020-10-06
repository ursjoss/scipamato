package ch.difty.scipamato.publ.config

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeNull
import org.junit.jupiter.api.Test

internal class ScipamatoPropertiesTest {

    private val sp = ScipamatoProperties()

    @Test
    fun brand_hasDefaultValue() {
        sp.brand shouldBeEqualTo "SciPaMaTo-Public"
    }

    @Test
    fun defaultLocalization_hasDefaultEnglish() {
        sp.defaultLocalization shouldBeEqualTo "en"
    }

    @Test
    fun pubmedBaseUrl_hasDefaultValue() {
        sp.pubmedBaseUrl shouldBeEqualTo "https://www.ncbi.nlm.nih.gov/pubmed/"
    }

    @Test
    fun gettingRedirectPort_hasNoDefaultValue() {
        sp.redirectFromPort.shouldBeNull()
    }

    @Test
    fun isCommercialFontPresent() {
        sp.commercialFontPresent.shouldBeFalse()
    }

    @Test
    fun isLessUsedOverCss() {
        sp.lessUsedOverCss.shouldBeFalse()
    }

    @Test
    fun isNavbarVisibleByDefault() {
        sp.navbarVisibleByDefault.shouldBeFalse()
    }

    @Test
    fun cmsUrlSearchPage() {
        sp.cmsUrlSearchPage.shouldBeNull()
    }

    @Test
    fun cmsUrlNewStudyPage() {
        sp.cmsUrlNewStudyPage.shouldBeNull()
    }

    @Test
    fun authorsAbbreviatedMaxLength() {
        sp.authorsAbbreviatedMaxLength shouldBeEqualTo 0
    }

    @Test
    fun responsiveIFrameSupport_isDisabledByDefault() {
        sp.responsiveIframeSupportEnabled.shouldBeFalse()
    }

    @Test
    fun managementUserName_hasDefaultValue() {
        sp.managementUserName shouldBeEqualTo "admin"
    }

    @Test
    fun managementUserPassword_isPresent() {
        sp.managementUserPassword.shouldBeNull()
    }

    @Test
    fun multiSelectBoxActionBoxWithMoreEntriesThan_hasDefaultValue() {
        sp.multiSelectBoxActionBoxWithMoreEntriesThan shouldBeEqualTo 4
    }
}
