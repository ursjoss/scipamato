package ch.difty.scipamato.common.entity.filter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ScipamatoFilterTest {

    @Test
    void canInstantiate() {
        assertThat(new ScipamatoFilter()).isNotNull();
    }
}
