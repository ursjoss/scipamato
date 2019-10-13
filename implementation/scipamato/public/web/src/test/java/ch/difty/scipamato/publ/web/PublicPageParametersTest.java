package ch.difty.scipamato.publ.web;

import static ch.difty.scipamato.publ.web.PublicPageParameters.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PublicPageParametersTest {

    @Test
    void assertParameters() {
        assertThat(PublicPageParameters.values()).containsExactly(SEARCH_ORDER_ID, SHOW_EXCLUDED, SHOW_NAVBAR, NUMBER,
            ISSUE, PARENT_URL);
    }

    @Test
    void assertRoleNames() {
        assertThat(SEARCH_ORDER_ID.getName()).isEqualTo("searchOrderId");
        assertThat(SHOW_EXCLUDED.getName()).isEqualTo("showExcluded");
        assertThat(SHOW_NAVBAR.getName()).isEqualTo("showNavbar");
        assertThat(NUMBER.getName()).isEqualTo("number");
        assertThat(ISSUE.getName()).isEqualTo("issue");
        assertThat(PARENT_URL.getName()).isEqualTo("parentUrl");
    }
}
