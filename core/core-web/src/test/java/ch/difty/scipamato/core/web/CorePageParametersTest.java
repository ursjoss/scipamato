package ch.difty.scipamato.core.web;

import static ch.difty.scipamato.core.web.CorePageParameters.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CorePageParametersTest {

    @Test
    void values() {
        assertThat(CorePageParameters.values()).containsExactly(SEARCH_ORDER_ID, SHOW_EXCLUDED, USER_ID, MODE,
            TAB_INDEX);
    }

    @Test
    void assertRoleNames() {
        assertThat(SEARCH_ORDER_ID.getName()).isEqualTo("searchOrderId");
        assertThat(SHOW_EXCLUDED.getName()).isEqualTo("showExcluded");
        assertThat(USER_ID.getName()).isEqualTo("userId");
        assertThat(MODE.getName()).isEqualTo("mode");
        assertThat(TAB_INDEX.getName()).isEqualTo("tabIndex");
    }
}
