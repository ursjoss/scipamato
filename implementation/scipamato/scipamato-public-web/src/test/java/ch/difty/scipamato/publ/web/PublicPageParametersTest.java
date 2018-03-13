package ch.difty.scipamato.publ.web;

import static ch.difty.scipamato.publ.web.PublicPageParameters.NUMBER;
import static ch.difty.scipamato.publ.web.PublicPageParameters.SEARCH_ORDER_ID;
import static ch.difty.scipamato.publ.web.PublicPageParameters.SHOW_EXCLUDED;
import static ch.difty.scipamato.publ.web.PublicPageParameters.SHOW_NAVBAR;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class PublicPageParametersTest {

    @Test
    public void assertParameters() {
        assertThat(PublicPageParameters.values()).containsExactly(SEARCH_ORDER_ID, SHOW_EXCLUDED, SHOW_NAVBAR, NUMBER);
    }

    @Test
    public void assertRoleNames() {
        assertThat(SEARCH_ORDER_ID.getName()).isEqualTo("searchOrderId");
        assertThat(SHOW_EXCLUDED.getName()).isEqualTo("showExcluded");
        assertThat(SHOW_NAVBAR.getName()).isEqualTo("showNavbar");
        assertThat(NUMBER.getName()).isEqualTo("number");
    }

}
