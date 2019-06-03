package ch.difty.scipamato.core.web.autoconfiguration;

import static org.assertj.core.api.Assertions.assertThat;

import de.agilecoders.wicket.themes.markup.html.bootswatch.BootswatchTheme;
import org.junit.jupiter.api.Test;

class BootstrapPropertiesTest {

    private final BootstrapProperties bp = new BootstrapProperties();

    @Test
    void standardTheme_isSandstone() {
        assertThat(bp.getTheme()).isEqualTo(BootswatchTheme.Sandstone);
    }

    @Test
    void canModifyTheme() {
        bp.setTheme(BootswatchTheme.Cerulean);
        assertThat(bp.getTheme()).isEqualTo(BootswatchTheme.Cerulean);
    }

    @Test
    void isEnabledByDefault() {
        assertThat(bp.isEnabled()).isTrue();
    }

    @Test
    void canDisable() {
        bp.setEnabled(false);
        assertThat(bp.isEnabled()).isFalse();
    }

}
