package ch.difty.scipamato.publ.config

import ch.difty.scipamato.publ.AbstractIntegrationTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldMatch
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

/**
 * Note, this test class currently derives the configured values from
 * <literal>application.properties</literal>.
 */
internal class ScipamatoPublicPropertiesIntegrationTest : AbstractIntegrationTest() {

    @Autowired
    lateinit var appProperties: ApplicationPublicProperties

    @Test
    fun gettingBuildVersion() {
        appProperties.buildVersion?.shouldMatch("""\d+\.\d+\.\d+.*""")
    }

    @Test
    fun assertDefaultLocalization() {
        appProperties.defaultLocalization shouldBeEqualTo "de"
    }

    @Test
    fun assertBrand() {
        appProperties.brand shouldBeEqualTo "SciPaMaTo"
    }

    @Test
    fun assertPubmedBaseUrl() {
        appProperties.pubmedBaseUrl shouldBeEqualTo "https://www.ncbi.nlm.nih.gov/pubmed/"
    }

    @Test
    fun assertPresenceOfCommercialFont() {
        appProperties.isCommercialFontPresent.shouldBeFalse()
    }

    @Test
    fun assertNavbarVisibleByDefault() {
        appProperties.isNavbarVisibleByDefault.shouldBeFalse()
    }

    @Test
    fun assertCmsUrlSearchPage() {
        appProperties.cmsUrlSearchPage shouldBeEqualTo "http://localhost:8081/"
    }

    @Test
    fun assertCmsUrlNewStudyPage() {
        appProperties.cmsUrlNewStudyPage shouldBeEqualTo "http://localhost:8081/new-studies"
    }

    @Test
    fun authorsAbbreviatedMaxLength() {
        appProperties.authorsAbbreviatedMaxLength shouldBeEqualTo 60
    }

    @get:Test
    val isResponsiveIframeSupportEnabled: Unit
        get() {
            appProperties.isResponsiveIframeSupportEnabled.shouldBeFalse()
        }

    @Test
    fun numberOfPreviousNewslettersInArchive() {
        appProperties.numberOfPreviousNewslettersInArchive shouldBeEqualTo 14
    }
}
