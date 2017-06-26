package ch.difty.scipamato.web.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.extractProperty;

import java.util.Arrays;

import org.junit.Test;

import ch.difty.scipamato.auth.Role;
import ch.difty.scipamato.entity.User;

public class ScipamatoUserDetailsTest {

    @Test
    public void test() {
        User user = new User(1, "un", "fn", "ln", "em", "pw", true, Arrays.asList(Role.ADMIN, Role.USER));
        ScipamatoUserDetails sud = new ScipamatoUserDetails(user);

        assertThat(sud.getId()).isEqualTo(1);
        assertThat(sud.getUserName()).isEqualTo("un");
        assertThat(sud.getFirstName()).isEqualTo("fn");
        assertThat(sud.getLastName()).isEqualTo("ln");
        assertThat(sud.getEmail()).isEqualTo("em");
        assertThat(sud.getPassword()).isEqualTo("pw");
        assertThat(sud.isEnabled()).isEqualTo(true);
        assertThat(extractProperty("key").from(sud.getRoles())).containsExactly("ROLE_ADMIN", "ROLE_USER");

        assertThat(extractProperty("authority").from(sud.getAuthorities())).containsExactly("ROLE_ADMIN", "ROLE_USER");
        assertThat(sud.getUsername()).isEqualTo("un");
    }

}
