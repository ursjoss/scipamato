package ch.difty.scipamato.web;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.scipamato.FinalClassTest;

public class PageParameterNamesTest extends FinalClassTest<PageParameterNames> {

    @Test
    public void assertRoleNames() {
        assertThat(PageParameterNames.SEARCH_ORDER_ID).isEqualTo("searchOrderId");
        assertThat(PageParameterNames.SHOW_EXCLUDED).isEqualTo("showExcluded");
    }

}
