package ch.difty.scipamato.auth;

import static ch.difty.scipamato.auth.Roles.ADMIN;
import static ch.difty.scipamato.auth.Roles.USER;
import static ch.difty.scipamato.auth.Roles.VIEWER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.scipamato.FinalClassTest;

public class RolesTest extends FinalClassTest<Roles> {

    @Test
    public void assertRoleName_User() {
        assertThat(USER).isEqualTo("ROLE_USER");
    }

    @Test
    public void assertRoleName_Admin() {
        assertThat(ADMIN).isEqualTo("ROLE_ADMIN");
    }

    @Test
    public void assertRoleName_Viewer() {
        assertThat(VIEWER).isEqualTo("ROLE_VIEWER");
    }

}
