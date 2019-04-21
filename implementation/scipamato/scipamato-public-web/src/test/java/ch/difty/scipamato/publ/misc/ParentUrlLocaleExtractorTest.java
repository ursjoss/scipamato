package ch.difty.scipamato.publ.misc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.difty.scipamato.publ.config.ScipamatoPublicProperties;

@ExtendWith(MockitoExtension.class)
public class ParentUrlLocaleExtractorTest {

    private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    private LocaleExtractor localeExtractor;

    @Mock
    private ScipamatoPublicProperties propertiesMock;

    @BeforeEach
    public void setUp() {
        when(propertiesMock.getDefaultLocalization()).thenReturn("en");
        localeExtractor = new ParentUrlLocaleExtractor(propertiesMock);
    }

    @Test
    public void givenNullInput_returnsDefaultLocale() {
        assertThat(localeExtractor.extractLocaleFrom(null)).isEqualTo(DEFAULT_LOCALE);
    }

    @Test
    public void givenGarbledInput_returnsDefaultLocale() {
        String input = "foobar";
        assertThat(localeExtractor.extractLocaleFrom(input)).isEqualTo(DEFAULT_LOCALE);
    }

    @Test
    public void givenGermanParentUrl_returnsDe() {
        String input = "https://www.foo.ch/de/projects/ludok/datenbank/";
        assertThat(localeExtractor.extractLocaleFrom(input)).isEqualTo(Locale.GERMAN);
    }

    @Test
    public void givenEnglishParentUrl_returnsEn() {
        String input = "https://www.foo.ch/en/projects/ludok/datenbank/";
        assertThat(localeExtractor.extractLocaleFrom(input)).isEqualTo(Locale.ENGLISH);
    }

    @Test
    public void givenFrenchParentUrl_returnsFr() {
        String input = "https://www.foo.ch/fr/projects/page-daccueil/datenbank/";
        assertThat(localeExtractor.extractLocaleFrom(input)).isEqualTo(Locale.FRENCH);
    }

    @Test
    public void canHandleHttpInsteadOfHttps() {
        String input = "http://www.foo.swisstph.ch/fr/projects/page-daccueil/datenbank/";
        assertThat(localeExtractor.extractLocaleFrom(input)).isEqualTo(Locale.FRENCH);
    }

    @Test
    public void canHandleCaseVariance() {
        String input = "htTps://www.foo.swisstPh.ch/fR/projects/page-Daccueil/datenbank/";
        assertThat(localeExtractor.extractLocaleFrom(input)).isEqualTo(Locale.FRENCH);
    }

    @Test
    public void canHandleSingleSlash() {
        String input = "https:/www.foo.swisstph.ch/fr/projects/page-daccueil/datenbank/";
        assertThat(localeExtractor.extractLocaleFrom(input)).isEqualTo(Locale.FRENCH);
    }

}