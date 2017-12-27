package ch.difty.scipamato.core.persistence.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.extractProperty;
import static org.assertj.core.api.Assertions.fail;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.difty.scipamato.core.auth.Role;
import ch.difty.scipamato.core.entity.User;
import ch.difty.scipamato.core.persistence.JooqTransactionalIntegrationTest;
import ch.difty.scipamato.core.persistence.OptimisticLockingException;

public class JooqUserRepoIntegrationTest extends JooqTransactionalIntegrationTest {

    private static final Integer MAX_ID_PREPOPULATED       = 8;
    private static final int     RECORD_COUNT_PREPOPULATED = 8;

    @Autowired
    private JooqUserRepo repo;

    @Test
    public void findingAll() {
        List<User> users = repo.findAll();
        assertThat(users).hasSize(RECORD_COUNT_PREPOPULATED);
        assertThat(users.get(0)
            .getId()).isEqualTo(1);
        assertThat(users.get(1)
            .getId()).isEqualTo(2);
        assertThat(users.get(2)
            .getId()).isEqualTo(3);
        assertThat(users.get(3)
            .getId()).isEqualTo(4);
    }

    @Test
    public void findingById_withExistingId_returnsEntity() {
        User user = repo.findById(4);
        assertThat(user.getId()).isEqualTo(4);

        assertThat(user.getRoles()).hasSize(2);
        assertThat(extractProperty("id").from(user.getRoles())).containsExactly(1, 2);
    }

    @Test
    public void findingById_withNonExistingId_returnsNull() {
        assertThat(repo.findById(-1)).isNull();
    }

    @Test
    public void addingRecord_savesRecordAndRefreshesId() {
        User p = makeMinimalUser();
        assertThat(p.getId()).isNull();

        User saved = repo.add(p);
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull()
            .isGreaterThan(MAX_ID_PREPOPULATED);
        assertThat(saved.getUserName()).isEqualTo("a");
    }

    private User makeMinimalUser() {
        User u = new User();
        u.setUserName("a");
        u.setFirstName("b");
        u.setLastName("c");
        u.setEmail("d");
        u.setPassword("e");
        return u;
    }

    @Test
    public void updatingRecord() {
        User user = repo.add(makeMinimalUser());
        assertThat(user).isNotNull();
        assertThat(user.getId()).isNotNull()
            .isGreaterThan(MAX_ID_PREPOPULATED);
        final int id = user.getId();
        assertThat(user.getUserName()).isEqualTo("a");

        user.setUserName("b");
        repo.update(user);
        assertThat(user.getId()).isEqualTo(id);

        User newCopy = repo.findById(id);
        assertThat(newCopy).isNotEqualTo(user);
        assertThat(newCopy.getId()).isEqualTo(id);
        assertThat(newCopy.getUserName()).isEqualTo("b");
    }

    @Test
    public void deletingRecord() {
        User user = repo.add(makeMinimalUser());
        assertThat(user).isNotNull();
        assertThat(user.getId()).isNotNull()
            .isGreaterThan(MAX_ID_PREPOPULATED);
        final int id = user.getId();
        assertThat(user.getUserName()).isEqualTo("a");

        User deleted = repo.delete(id, user.getVersion());
        assertThat(deleted.getId()).isEqualTo(id);

        assertThat(repo.findById(id)).isNull();
    }

    @Test
    public void findingUserByName_withNonExistingUserName_returnsNull() {
        assertThat(repo.findByUserName("lkajdsklj")).isNull();
    }

    @Test
    public void findingUserByName_withExistingUserName_returnsUserIncludingRoles() {
        String name = "admin";
        final User admin = repo.findByUserName(name);
        assertThat(admin.getUserName()).isEqualTo(name);
        assertThat(admin.getRoles()).isNotEmpty();
    }

    @Test
    public void updatingAssociatedEntities_addsAndRemovesRoles() {
        Integer id = newUserAndSave();

        addRoleViewerAndUserToUserWith(id);
        addRoleAdminAndRemoveRoleViewerFrom(id);
        removeRoleAdminFrom(id);
    }

    private Integer newUserAndSave() {
        User u = new User();
        u.setUserName("test");
        u.setFirstName("fn");
        u.setLastName("ln");
        u.setEnabled(false);
        u.setEmail("u@foo.bar");
        u.setPassword("xyz");

        assertThat(u.getId()).isNull();

        User savedUser = repo.add(u);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUserName()).isEqualTo("test");
        assertThat(savedUser.getRoles()).isEmpty();
        assertThat(savedUser.getVersion()).isEqualTo(1);

        return savedUser.getId();
    }

    private void addRoleViewerAndUserToUserWith(Integer id) {
        User user = repo.findById(id);
        user.addRole(Role.VIEWER);
        user.addRole(Role.USER);
        User viewer = repo.update(user);
        assertThat(viewer.getRoles()).containsOnly(Role.VIEWER, Role.USER);
    }

    private void addRoleAdminAndRemoveRoleViewerFrom(Integer id) {
        User user = repo.findById(id);
        user.addRole(Role.ADMIN);
        user.removeRole(Role.VIEWER);
        User u = repo.update(user);
        assertThat(u.getRoles()).containsOnly(Role.USER, Role.ADMIN);
    }

    private void removeRoleAdminFrom(Integer id) {
        User user = repo.findById(id);
        user.removeRole(Role.ADMIN);
        User u = repo.update(user);
        assertThat(u.getRoles()).containsOnly(Role.USER);
    }

    @Test
    public void canUpdateUser_thatHasBeenModifiedElswhereInTheMeanTime() {
        User user = makeAndValidateNewUser();
        User secondReloaded = loadSameUserIndependentlyAndModifyAndUpdate(user);

        user.setLastName("yetanother");

        try {
            repo.update(user);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(OptimisticLockingException.class);
        }

        User reloaded = repo.findById(user.getId());
        assertThat(reloaded.getVersion()).isEqualTo(2);
        assertThat(reloaded.getFirstName()).isEqualTo(secondReloaded.getFirstName());
        assertThat(reloaded.getLastName()).isEqualTo(secondReloaded.getLastName());
        assertThat(reloaded.getVersion()).isEqualTo(secondReloaded.getVersion());
    }

    private User makeAndValidateNewUser() {
        User user = repo.add(makeMinimalUser());
        assertThat(user).isNotNull();
        assertThat(user.getVersion()).isEqualTo(1);
        assertThat(user.getId()).isNotNull()
            .isGreaterThan(MAX_ID_PREPOPULATED);
        return user;
    }

    private User loadSameUserIndependentlyAndModifyAndUpdate(User user) {
        User secondInstance = repo.findById(user.getId());
        assertThat(user.getVersion()).isEqualTo(secondInstance.getVersion());
        secondInstance.setFirstName("changed");
        User secondReloaded = repo.update(secondInstance);
        assertThat(secondReloaded.getVersion()).isEqualTo(2);
        return secondReloaded;
    }

    @Test
    public void cannotDeleteUser_thatHasbeenModifiedElsewhereInTheMeanTime() {
        User user = makeAndValidateNewUser();
        User secondReloaded = loadSameUserIndependentlyAndModifyAndUpdate(user);
        try {
            repo.delete(user.getId(), user.getVersion());
            fail("Should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(OptimisticLockingException.class);
        }
        User deletedEntity = repo.delete(user.getId(), secondReloaded.getVersion());
        assertThat(deletedEntity.getVersion()).isEqualTo(secondReloaded.getVersion());
    }
}
