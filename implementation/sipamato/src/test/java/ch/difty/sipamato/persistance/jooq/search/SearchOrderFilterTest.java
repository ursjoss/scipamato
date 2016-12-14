package ch.difty.sipamato.persistance.jooq.search;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class SearchOrderFilterTest {

    @Test
    public void canInstantiate() {
        SearchOrderFilter f = new SearchOrderFilter();
        f.setNameMask("foo");
        f.setOwner(3);
        f.setOwnerIncludingGlobal(4);
        f.setGlobal(true);

        assertThat(f.getNameMask()).isEqualTo("foo");
        assertThat(f.getOwner()).isEqualTo(3);
        assertThat(f.getOwnerIncludingGlobal()).isEqualTo(4);
        assertThat(f.getGlobal()).isEqualTo(true);
    }
}
