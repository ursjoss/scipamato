package ch.difty.scipamato.core.web;

import static ch.difty.scipamato.core.web.CorePageParameters.SEARCH_ORDER_ID;
import static ch.difty.scipamato.core.web.CorePageParameters.SHOW_EXCLUDED;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class CorePageParametersTest {

    @Test
    public void values() {
        assertThat(CorePageParameters.values()).containsExactly(SEARCH_ORDER_ID, SHOW_EXCLUDED);
    }

    @Test
    public void assertRoleNames() {
        assertThat(SEARCH_ORDER_ID.getName()).isEqualTo("searchOrderId");
        assertThat(SHOW_EXCLUDED.getName()).isEqualTo("showExcluded");
    }

}
