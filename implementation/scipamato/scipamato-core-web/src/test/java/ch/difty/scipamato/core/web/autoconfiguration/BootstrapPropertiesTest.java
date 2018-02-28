package ch.difty.scipamato.core.web.autoconfiguration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.scipamato.core.web.autoconfiguration.BootstrapProperties;
import de.agilecoders.wicket.themes.markup.html.bootswatch.BootswatchTheme;

public class BootstrapPropertiesTest {

    private final BootstrapProperties bp = new BootstrapProperties();

    @Test
    public void standardTheme_isSandstone() {
        assertThat(bp.getTheme()).isEqualTo(BootswatchTheme.Sandstone);
    }

    @Test
    public void canModifyTheme() {
        bp.setTheme(BootswatchTheme.Cerulean);
        assertThat(bp.getTheme()).isEqualTo(BootswatchTheme.Cerulean);
    }

    @Test
    public void isEnabledByDefault() {
        assertThat(bp.isEnabled()).isTrue();
    }

    @Test
    public void canDisable() {
        bp.setEnabled(false);
        assertThat(bp.isEnabled()).isFalse();
    }

}
