package ch.difty.sipamato.web;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class PageParameterNamesTest {

    @Test
    public void assertRoleNames() {
        assertThat(PageParameterNames.SEARCH_ORDER_ID).isEqualTo("searchOrderId");
    }
}
