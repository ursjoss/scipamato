package ch.difty.scipamato.common.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MavenPropertiesTest {

    private final MavenProperties mp = new MavenProperties();

    @Test
    void noDefaultValues() {
        assertThat(mp.getVersion()).isNull();
        assertThat(mp.getTimestamp()).isNull();
    }
}
