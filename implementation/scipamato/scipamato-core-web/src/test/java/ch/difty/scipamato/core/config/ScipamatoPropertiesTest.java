package ch.difty.scipamato.core.config;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

import ch.difty.scipamato.common.config.core.AuthorParserStrategy;

public class ScipamatoPropertiesTest {

    private final ScipamatoProperties sp = new ScipamatoProperties("0.6.2-SNAPSHOT", "de", "DEFAULT", "SciPaMaTo", 4l, "pubmedBaseUrl/");

    @Test
    public void canResolveBuildVersion() {
        assertThat(sp.getBuildVersion()).isEqualTo("0.6.2-SNAPSHOT");
    }

    @Test
    public void canResolveDefaultLocalization() {
        assertThat(sp.getDefaultLocalization()).isEqualTo("de");
    }

    @Test
    public void canResolveAuthorParserStrategy() {
        assertThat(sp.getAuthorParserStrategy()).isEqualTo(AuthorParserStrategy.DEFAULT);
    }

    @Test
    public void canResolveBrand() {
        assertThat(sp.getBrand()).isEqualTo("SciPaMaTo");
    }

    @Test
    public void canResolveMinimumPaperNumberToBeRecycled() {
        assertThat(sp.getMinimumPaperNumberToBeRecycled()).isEqualTo(4l);
    }

    @Test
    public void canResolvePubmedBaseUrl() {
        assertThat(sp.getPubmedBaseUrl()).isEqualTo("pubmedBaseUrl/");
    }

    @Test
    public void canResolveMinimumPaperNumberToBeRecycled_whenWasNotDefined() {
        ScipamatoProperties sp = new ScipamatoProperties("xy", "de", "DEFAULT", "SciPaMaTo", null, "pubmedBaseUrl/");
        assertThat(sp.getMinimumPaperNumberToBeRecycled()).isEqualTo(0l);
    }

}
