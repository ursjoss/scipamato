package ch.difty.scipamato.core.web.security;

import static ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED;
import static ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED;
import static ch.difty.scipamato.core.entity.CoreEntity.CoreEntityFields.CREATOR_ID;
import static ch.difty.scipamato.core.entity.CoreEntity.CoreEntityFields.MODIFIER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.extractProperty;

import java.util.Set;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.auth.Role;
import ch.difty.scipamato.core.entity.User;
import ch.difty.scipamato.core.web.authentication.ScipamatoUserDetails;

class ScipamatoUserDetailsTest {

    @Test
    void test() {
        User user = new User(1, "un", "fn", "ln", "em", "pw", true, Set.of(Role.ADMIN, Role.USER));
        ScipamatoUserDetails sud = new ScipamatoUserDetails(user);

        assertThat(sud.getId()).isEqualTo(1);
        assertThat(sud.getUserName()).isEqualTo("un");
        assertThat(sud.getFirstName()).isEqualTo("fn");
        assertThat(sud.getLastName()).isEqualTo("ln");
        assertThat(sud.getEmail()).isEqualTo("em");
        assertThat(sud.getPassword()).isEqualTo("pw");
        assertThat(sud.isEnabled()).isEqualTo(true);
        assertThat(extractProperty("key").from(sud.getRoles())).containsExactlyInAnyOrder("ROLE_ADMIN", "ROLE_USER");

        assertThat(extractProperty("authority").from(sud.getAuthorities())).containsExactlyInAnyOrder("ROLE_ADMIN",
            "ROLE_USER");
        assertThat(sud.getUsername()).isEqualTo("un");

        // statically set
        assertThat(sud.isAccountNonExpired()).isTrue();
        assertThat(sud.isAccountNonLocked()).isTrue();
        assertThat(sud.isCredentialsNonExpired()).isTrue();
    }

    @Test
    void testToString() {
        User user = new User(1, "un", "fn", "ln", "em", "pw", true, Set.of(Role.ADMIN, Role.USER));
        ScipamatoUserDetails sud = new ScipamatoUserDetails(user);
        final String ts = sud.toString();
        assertThat(ts).contains("ROLE_ADMIN", "ROLE_USER");
        assertThat(ts).startsWith("ScipamatoUserDetails[roles=[ROLE_");
        assertThat(ts).contains(
            "],userName=un,firstName=fn,lastName=ln,email=em,password=pw,enabled=true,roles=[ROLE_");
        assertThat(ts).endsWith(
            "],id=1,createdBy=<null>,lastModifiedBy=<null>,created=<null>,lastModified=<null>,version=0]");
    }

    @Test
    void equals() {
        EqualsVerifier
            .forClass(User.class)
            .withRedefinedSuperclass()
            .withIgnoredFields(CREATED.getName(), CREATOR_ID.getName(), MODIFIED.getName(), MODIFIER_ID.getName())
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }

}
