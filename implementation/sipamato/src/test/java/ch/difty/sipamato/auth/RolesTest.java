package ch.difty.sipamato.auth;

import static ch.difty.sipamato.auth.Roles.ADMIN;
import static ch.difty.sipamato.auth.Roles.USER;
import static ch.difty.sipamato.auth.Roles.VIEWER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class RolesTest {

    @Test
    public void assertRoleNames() {
        assertThat(USER).isEqualTo("ROLE_USER");
        assertThat(ADMIN).isEqualTo("ROLE_ADMIN");
        assertThat(VIEWER).isEqualTo("ROLE_VIEWER");
    }
}
