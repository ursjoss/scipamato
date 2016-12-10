package ch.difty.sipamato.web.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.extractProperty;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import ch.difty.sipamato.entity.Role;
import ch.difty.sipamato.entity.User;

public class SipamatoUserDetailsTest {

    private User user;

    private final List<Role> roles = Arrays.asList(new Role(1, "R1"), new Role(2, "R2"));

    @Test
    public void test() {
        user = new User(1, "un", "fn", "ln", "em", "pw", true, roles);
        SipamatoUserDetails sud = new SipamatoUserDetails(user);

        assertThat(sud.getId()).isEqualTo(1);
        assertThat(sud.getUserName()).isEqualTo("un");
        assertThat(sud.getFirstName()).isEqualTo("fn");
        assertThat(sud.getLastName()).isEqualTo("ln");
        assertThat(sud.getEmail()).isEqualTo("em");
        assertThat(sud.getPassword()).isEqualTo("pw");
        assertThat(sud.isEnabled()).isEqualTo(true);
        assertThat(extractProperty("name").from(sud.getRoles())).containsExactly("R1", "R2");

        assertThat(extractProperty("authority").from(sud.getAuthorities())).containsExactly("ROLE_R1", "ROLE_R2");
        assertThat(sud.getUsername()).isEqualTo("un");
    }

}
