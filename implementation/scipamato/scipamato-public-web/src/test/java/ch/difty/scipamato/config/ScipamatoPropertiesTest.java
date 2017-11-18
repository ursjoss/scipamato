package ch.difty.scipamato.config;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

public class ScipamatoPropertiesTest {

    private final ScipamatoProperties sp = new ScipamatoProperties("0.6.2-SNAPSHOT", "de", "SciPaMaTo");

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
        assertThat(sp.getAuthorParserStrategy()).isNull();
    }

    @Test
    public void canResolveBrand() {
        assertThat(sp.getBrand()).isEqualTo("SciPaMaTo");
    }

    @Test
    public void canResolveMinimumPaperNumberToBeRecycled() {
        assertThat(sp.getMinimumPaperNumberToBeRecycled()).isEqualTo(0l);
    }
}
