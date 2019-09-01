package ch.difty.scipamato.core.auth;

import static ch.difty.scipamato.core.auth.Roles.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class RolesTest {

    @Test
    void assertRoleName_User() {
        assertThat(USER).isEqualTo("ROLE_USER");
    }

    @Test
    void assertRoleName_Admin() {
        assertThat(ADMIN).isEqualTo("ROLE_ADMIN");
    }

    @Test
    void assertRoleName_Viewer() {
        assertThat(VIEWER).isEqualTo("ROLE_VIEWER");
    }

    @Test
    void dummyTest() {
        assertThat(dummyMethod()).isNull();
    }

}
