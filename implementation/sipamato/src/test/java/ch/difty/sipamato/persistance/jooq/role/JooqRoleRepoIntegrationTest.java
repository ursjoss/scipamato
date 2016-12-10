package ch.difty.sipamato.persistance.jooq.role;

import static ch.difty.sipamato.db.Tables.ROLE;
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
import ch.difty.sipamato.entity.Role;

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
public class JooqRoleRepoIntegrationTest {

    private static final Integer MAX_ID_PREPOPULATED = 2;
    private static final int RECORD_COUNT_PREPOPULATED = 2;

    @Autowired
    private DSLContext dsl;

    @Autowired
    private JooqRoleRepo repo;

    @After
    public void teardown() {
        // Delete all books that were created in any test
        dsl.delete(ROLE).where(ROLE.ID.gt(MAX_ID_PREPOPULATED)).execute();
    }

    @Test
    public void findingAll() {
        List<Role> roles = repo.findAll();
        assertThat(roles).hasSize(RECORD_COUNT_PREPOPULATED);
        assertThat(roles.get(0).getId()).isEqualTo(1);
        assertThat(roles.get(1).getId()).isEqualTo(2);
    }

    @Test
    public void findingById_withExistingId_returnsEntity() {
        Role role = repo.findById(2);
        role = repo.findById(RECORD_COUNT_PREPOPULATED);
        assertThat(role.getId()).isEqualTo(MAX_ID_PREPOPULATED);

        assertThat(role.getUsers()).hasSize(3);
        assertThat(extractProperty("id").from(role.getUsers())).containsExactly(1, 3, 4);
    }

    @Test
    public void findingById_withNonExistingId_returnsNull() {
        assertThat(repo.findById(-1)).isNull();
    }

    @Test
    public void addingRecord_savesRecordAndRefreshesId() {
        Role p = makeMinimalRole();
        assertThat(p.getId()).isNull();

        Role saved = repo.add(p);
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull().isGreaterThan(MAX_ID_PREPOPULATED);
        assertThat(saved.getName()).isEqualTo("a");
    }

    private Role makeMinimalRole() {
        Role p = new Role();
        p.setName("a");
        p.setComment("b");
        return p;
    }

    @Test
    public void updatingRecord() {
        Role role = repo.add(makeMinimalRole());
        assertThat(role).isNotNull();
        assertThat(role.getId()).isNotNull().isGreaterThan(MAX_ID_PREPOPULATED);
        final int id = role.getId();
        assertThat(role.getName()).isEqualTo("a");

        role.setName("b");
        repo.update(role);
        assertThat(role.getId()).isEqualTo(id);

        Role newCopy = repo.findById(id);
        assertThat(newCopy).isNotEqualTo(role);
        assertThat(newCopy.getId()).isEqualTo(id);
        assertThat(newCopy.getName()).isEqualTo("b");
    }

    @Test
    public void deletingRecord() {
        Role role = repo.add(makeMinimalRole());
        assertThat(role).isNotNull();
        assertThat(role.getId()).isNotNull().isGreaterThan(MAX_ID_PREPOPULATED);
        final int id = role.getId();
        assertThat(role.getName()).isEqualTo("a");

        Role deleted = repo.delete(id);
        assertThat(deleted.getId()).isEqualTo(id);

        assertThat(repo.findById(id)).isNull();
    }

}
