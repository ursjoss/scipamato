package ch.difty.sipamato.auth;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class RolesTest implements Roles {

    @Test
    public void assertRoleNames() {
        assertThat(USER).isEqualTo("ROLE_USER");
        assertThat(ADMIN).isEqualTo("ROLE_ADMIN");
        assertThat(VIEWER).isEqualTo("ROLE_VIEWER");
    }
}
