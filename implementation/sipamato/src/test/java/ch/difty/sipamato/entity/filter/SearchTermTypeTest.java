package ch.difty.sipamato.entity.filter;

import static ch.difty.sipamato.entity.filter.SearchTermType.BOOLEAN;
import static ch.difty.sipamato.entity.filter.SearchTermType.INTEGER;
import static ch.difty.sipamato.entity.filter.SearchTermType.STRING;
import static ch.difty.sipamato.entity.filter.SearchTermType.values;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.Test;

public class SearchTermTypeTest {

    @Test
    public void testValues() {
        assertThat(values()).containsExactly(BOOLEAN, INTEGER, STRING);
    }

    @Test
    public void testId() {
        assertThat(BOOLEAN.getId()).isEqualTo(0);
        assertThat(INTEGER.getId()).isEqualTo(1);
        assertThat(STRING.getId()).isEqualTo(2);
    }

    @Test
    public void testById_withValidIds() {
        assertThat(SearchTermType.byId(0)).isEqualTo(BOOLEAN);
        assertThat(SearchTermType.byId(1)).isEqualTo(INTEGER);
        assertThat(SearchTermType.byId(2)).isEqualTo(STRING);
    }

    @Test
    public void testById_withInvalidIds() {
        try {
            SearchTermType.byId(-1);
            fail("should have thrown");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(IllegalArgumentException.class).hasMessage("id -1 is not supported");
        }
        try {
            SearchTermType.byId(3);
            fail("should have thrown");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(IllegalArgumentException.class).hasMessage("id 3 is not supported");
        }
    }
}
