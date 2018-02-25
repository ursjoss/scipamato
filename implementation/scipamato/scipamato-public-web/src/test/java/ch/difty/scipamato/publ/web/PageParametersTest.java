package ch.difty.scipamato.publ.web;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class PageParametersTest {

    @Test
    public void assertRoleNames() {
        assertThat(PageParameters.SEARCH_ORDER_ID.getName()).isEqualTo("searchOrderId");
        assertThat(PageParameters.SHOW_EXCLUDED.getName()).isEqualTo("showExcluded");
    }

}
