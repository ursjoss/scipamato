package ch.difty.scipamato.publ.config

import ch.difty.scipamato.publ.AbstractIntegrationTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class ScipamatoPropertiesIntegrationTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var sp: ScipamatoProperties

    @Test
    fun brand_hasDefaultValue() {
        sp.brand shouldBeEqualTo "SciPaMaTo"
    }

    @Test
    fun defaultLocalization_hasDefaultEnglish() {
        sp.defaultLocalization shouldBeEqualTo "de"
    }

    @Test
    fun pubmedBaseUrl_hasDefaultValue() {
        sp.pubmedBaseUrl shouldBeEqualTo "https://www.ncbi.nlm.nih.gov/pubmed/"
    }

    @Test
    fun responsiveIFrameSupport_isDisabled() {
        sp.responsiveIframeSupportEnabled.shouldBeFalse()
    }

    @Test
    fun multiSelectBoxActionBoxWithMoreEntriesThan_hasDefault4() {
        sp.multiSelectBoxActionBoxWithMoreEntriesThan shouldBeEqualTo 4
    }
}
