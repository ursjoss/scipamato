package ch.difty.scipamato.config;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

public class ScipamatoPropertiesTest {

    private final ScipamatoProperties sp = new ScipamatoProperties("de", "DEFAULT", "SciPaMaTo", 4l);

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
    public void canResolveMinimumPaperNumberToBeRecycled_whenWasNotDefined() {
        ScipamatoProperties sp = new ScipamatoProperties("de", "DEFAULT", "SciPaMaTo", null);
        assertThat(sp.getMinimumPaperNumberToBeRecycled()).isEqualTo(0l);
    }

}
