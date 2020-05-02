package ch.difty.scipamato.publ.misc

import ch.difty.scipamato.publ.config.ScipamatoPublicProperties
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.Locale

@ExtendWith(MockKExtension::class)
internal class ParentUrlLocaleExtractorTest {

    private lateinit var localeExtractor: LocaleExtractor

    @MockK
    private lateinit var propertiesMock: ScipamatoPublicProperties

    @BeforeEach
    fun setUp() {
        every { propertiesMock.defaultLocalization } returns "en"
        localeExtractor = ParentUrlLocaleExtractor(propertiesMock)
    }

    @Test
    fun givenNullInput_returnsDefaultLocale() {
        localeExtractor.extractLocaleFrom(null) shouldBeEqualTo DEFAULT_LOCALE
    }

    @Test
    fun givenGarbledInput_returnsDefaultLocale() {
        val input = "foobar"
        localeExtractor.extractLocaleFrom(input) shouldBeEqualTo DEFAULT_LOCALE
    }

    @Test
    fun givenGermanParentUrl_returnsDe() {
        val input = "https://www.foo.ch/de/projects/ludok/datenbank/"
        localeExtractor.extractLocaleFrom(input) shouldBeEqualTo Locale.GERMAN
    }

    @Test
    fun givenEnglishParentUrl_returnsEn() {
        val input = "https://www.foo.ch/en/projects/ludok/datenbank/"
        localeExtractor.extractLocaleFrom(input) shouldBeEqualTo Locale.ENGLISH
    }

    @Test
    fun givenFrenchParentUrl_returnsFr() {
        val input = "https://www.foo.ch/fr/projects/page-daccueil/datenbank/"
        localeExtractor.extractLocaleFrom(input) shouldBeEqualTo Locale.FRENCH
    }

    @Test
    fun canHandleHttpInsteadOfHttps() {
        val input = "http://www.foo.swisstph.ch/fr/projects/page-daccueil/datenbank/"
        localeExtractor.extractLocaleFrom(input) shouldBeEqualTo Locale.FRENCH
    }

    @Test
    fun canHandleCaseVariance() {
        val input = "htTps://www.foo.swisstPh.ch/fR/projects/page-Daccueil/datenbank/"
        localeExtractor.extractLocaleFrom(input) shouldBeEqualTo Locale.FRENCH
    }

    @Test
    fun canHandleSingleSlash() {
        val input = "https:/www.foo.swisstph.ch/fr/projects/page-daccueil/datenbank/"
        localeExtractor.extractLocaleFrom(input) shouldBeEqualTo Locale.FRENCH
    }

    companion object {
        private val DEFAULT_LOCALE = Locale.ENGLISH
    }
}