package ch.difty.scipamato.web.security;

import static ch.difty.scipamato.entity.ScipamatoEntity.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.Test;

import ch.difty.scipamato.auth.Role;
import ch.difty.scipamato.entity.User;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

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

        // statically set
        assertThat(sud.isAccountNonExpired()).isTrue();
        assertThat(sud.isAccountNonLocked()).isTrue();
        assertThat(sud.isCredentialsNonExpired()).isTrue();

        assertThat(sud.toString()).isEqualTo(
                "ScipamatoUserDetails[roles=[ROLE_ADMIN, ROLE_USER],userName=un,firstName=fn,lastName=ln,email=em,password=pw,enabled=true,roles=[ROLE_ADMIN, ROLE_USER],id=1,created=<null>,createdBy=<null>,lastModified=<null>,lastModifiedBy=<null>,version=0]");
    }

    @Test
    public void equals() {
        EqualsVerifier
            .forClass(User.class)
            .withRedefinedSuperclass()
            .withIgnoredFields(CREATED, CREATOR_ID, MODIFIED, MODIFIER_ID)
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }

}
