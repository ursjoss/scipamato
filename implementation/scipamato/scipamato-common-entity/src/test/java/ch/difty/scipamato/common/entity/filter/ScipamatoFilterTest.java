package ch.difty.scipamato.common.entity.filter;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

public class ScipamatoFilterTest {

    @Test
    public void canInstantiate() {
        assertThat(new ScipamatoFilter()).isNotNull();
    }
}
