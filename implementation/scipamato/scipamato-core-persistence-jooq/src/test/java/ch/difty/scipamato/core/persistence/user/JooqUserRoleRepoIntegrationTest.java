package ch.difty.scipamato.core.persistence.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.difty.scipamato.core.auth.Role;
import ch.difty.scipamato.core.persistence.JooqBaseIntegrationTest;

public class JooqUserRoleRepoIntegrationTest extends JooqBaseIntegrationTest {

    @Autowired
    private JooqUserRoleRepo repo;

    @Test
    public void findingRolesForUser_withTwoRoles_returnsBoth() {
        final Set<Role> roles = repo.findRolesForUser(4);
        assertThat(roles).containsExactlyInAnyOrder(Role.ADMIN, Role.USER);
    }

    @Test
    public void findingRolesForUser_withNoRoles_returnsNone() {
        final Set<Role> roles = repo.findRolesForUser(7);
        assertThat(roles).isEmpty();
    }
}