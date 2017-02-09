package ch.difty.sipamato.web;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.sipamato.FinalClassTest;

public class PageParameterNamesTest extends FinalClassTest<PageParameterNames> {

    @Test
    public void assertRoleNames() {
        assertThat(PageParameterNames.SEARCH_ORDER_ID).isEqualTo("searchOrderId");
    }

}
