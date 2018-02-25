package ch.difty.scipamato.core.web;

import static ch.difty.scipamato.core.web.PageParameters.SEARCH_ORDER_ID;
import static ch.difty.scipamato.core.web.PageParameters.SHOW_EXCLUDED;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class PageParametersTest {

    @Test
    public void values() {
        assertThat(PageParameters.values()).containsExactly(SEARCH_ORDER_ID, SHOW_EXCLUDED);
    }

    @Test
    public void assertRoleNames() {
        assertThat(SEARCH_ORDER_ID.getName()).isEqualTo("searchOrderId");
        assertThat(SHOW_EXCLUDED.getName()).isEqualTo("showExcluded");
    }

}
