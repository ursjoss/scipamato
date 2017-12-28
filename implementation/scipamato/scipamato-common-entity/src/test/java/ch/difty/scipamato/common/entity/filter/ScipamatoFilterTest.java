package ch.difty.scipamato.common.entity.filter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ScipamatoFilterTest {

    @Test
    public void canInstantiate() {
        assertThat(new ScipamatoFilter()).isNotNull();
    }
}
