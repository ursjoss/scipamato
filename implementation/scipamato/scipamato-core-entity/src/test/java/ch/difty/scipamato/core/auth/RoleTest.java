package ch.difty.scipamato.core.auth;

import static ch.difty.scipamato.core.auth.Role.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RoleTest {

    @Test
    void hasAllValues() {
        assertThat(Role.values()).containsExactly(ADMIN, USER, VIEWER);
    }

    @Test
    void assertIds() {
        assertThat(extractProperty("id").from(Role.values())).containsExactly(1, 2, 3);
    }

    @Test
    void assertKeys() {
        assertThat(extractProperty("key").from(Role.values())).containsExactly(Roles.ADMIN, Roles.USER, Roles.VIEWER);
    }

    @Test
    void assertDescriptions() {
        assertThat(extractProperty("description").from(Role.values())).containsExactly("System Administration",
            "Main SciPaMaTo Users", "Read-only Viewer");
    }

    @Test
    void assertToString() {
        assertThat(ADMIN.toString()).isEqualTo("ROLE_ADMIN");
        assertThat(USER.toString()).isEqualTo("ROLE_USER");
        assertThat(VIEWER.toString()).isEqualTo("ROLE_VIEWER");
    }

    @Test
    void of_withExistingId() {
        assertThat(Role.of(1)).isEqualTo(ADMIN);
    }

    @Test
    void of_withNotExistingId_throws() {
        try {
            Role.of(0);
            fail("Should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No matching type for id 0");
        }
    }

    @Test
    void of_withNullId_returnsNull() {
        assertThat(Role.of(null)).isNull();
    }

}
