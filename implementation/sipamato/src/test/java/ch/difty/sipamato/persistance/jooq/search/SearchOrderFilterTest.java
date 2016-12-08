package ch.difty.sipamato.persistance.jooq.search;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class SearchOrderFilterTest {

    @Test
    public void canInstantiate() {
        SearchOrderFilter f = new SearchOrderFilter();
        assertThat(f).isNotNull();
    }
}
