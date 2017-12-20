package ch.difty.scipamato.common.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class MavenPropertiesTest {

    private final MavenProperties mp = new MavenProperties();

    @Test
    public void noDefaultValues() {
        assertThat(mp.getVersion()).isNull();
        assertThat(mp.getTimestamp()).isNull();
    }
}
