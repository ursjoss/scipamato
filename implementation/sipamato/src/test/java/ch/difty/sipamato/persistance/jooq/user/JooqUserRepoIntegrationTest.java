package ch.difty.sipamato.persistance.jooq.user;

import static ch.difty.sipamato.db.Tables.SIPAMATO_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.extractProperty;

import java.util.List;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import ch.difty.sipamato.SipamatoApplication;
import ch.difty.sipamato.entity.User;

/**
 * Note: The test will insert some records into the DB. It will try to wipe those records after the test suite terminates.
 *
 * If however, the number of records in the db does not match with the defined constants a few lines further down, the 
 * additional records in the db would be wiped out by the tearDown method. So please make sure the number of records (plus
 * the highest id) match the declarations further down.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SipamatoApplication.class)
@ActiveProfiles({ "DB_JOOQ" })
public class JooqUserRepoIntegrationTest {

    private static final Integer MAX_ID_PREPOPULATED = 4;
    private static final int RECORD_COUNT_PREPOPULATED = 4;

    @Autowired
    private DSLContext dsl;

    @Autowired
    private JooqUserRepo repo;

    @After
    public void teardown() {
        // Delete all users that were created in any test
        dsl.delete(SIPAMATO_USER).where(SIPAMATO_USER.ID.gt(MAX_ID_PREPOPULATED)).execute();
    }

    @Test
    public void findingAll() {
        List<User> users = repo.findAll();
        assertThat(users).hasSize(RECORD_COUNT_PREPOPULATED);
        assertThat(users.get(0).getId()).isEqualTo(1);
        assertThat(users.get(1).getId()).isEqualTo(2);
        assertThat(users.get(2).getId()).isEqualTo(3);
        assertThat(users.get(3).getId()).isEqualTo(4);
    }

    @Test
    public void findingById_withExistingId_returnsEntity() {
        User user = repo.findById(RECORD_COUNT_PREPOPULATED);
        assertThat(user.getId()).isEqualTo(MAX_ID_PREPOPULATED);

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
        assertThat(saved.getId()).isNotNull().isGreaterThan(MAX_ID_PREPOPULATED);
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
        assertThat(user.getId()).isNotNull().isGreaterThan(MAX_ID_PREPOPULATED);
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
        assertThat(user.getId()).isNotNull().isGreaterThan(MAX_ID_PREPOPULATED);
        final int id = user.getId();
        assertThat(user.getUserName()).isEqualTo("a");

        User deleted = repo.delete(id);
        assertThat(deleted.getId()).isEqualTo(id);

        assertThat(repo.findById(id)).isNull();
    }

}
