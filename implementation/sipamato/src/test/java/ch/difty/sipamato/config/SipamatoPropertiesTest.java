package ch.difty.sipamato.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class SipamatoPropertiesTest {

    private final SipamatoProperties sp = new SipamatoProperties("de", "DEFAULT", "SiPaMaTo", 4l);

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
        assertThat(sp.getBrand()).isEqualTo("SiPaMaTo");
    }

    @Test
    public void canResolveMinimumPaperNumberToBeRecycled() {
        assertThat(sp.getMinimumPaperNumberToBeRecycled()).isEqualTo(4l);
    }

    @Test
    public void canResolveMinimumPaperNumberToBeRecycled_whenWasNotDefined() {
        SipamatoProperties sp = new SipamatoProperties("de", "DEFAULT", "SiPaMaTo", null);
        assertThat(sp.getMinimumPaperNumberToBeRecycled()).isEqualTo(0l);
    }

}
