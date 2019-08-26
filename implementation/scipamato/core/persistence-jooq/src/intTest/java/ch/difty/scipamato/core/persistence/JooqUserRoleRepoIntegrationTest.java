package ch.difty.scipamato.core.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import ch.difty.scipamato.core.auth.Role;
import ch.difty.scipamato.core.persistence.user.JooqUserRoleRepo;

@JooqTest
@Testcontainers
class JooqUserRoleRepoIntegrationTest {

    @Autowired
    private JooqUserRoleRepo repo;

    @Test
    void findingRolesForUser_withTwoRoles_returnsBoth() {
        final Set<Role> roles = repo.findRolesForUser(4);
        assertThat(roles).containsExactlyInAnyOrder(Role.ADMIN, Role.USER);
    }

    @Test
    void findingRolesForUser_withNoRoles_returnsNone() {
        final Set<Role> roles = repo.findRolesForUser(7);
        assertThat(roles).isEmpty();
    }
}