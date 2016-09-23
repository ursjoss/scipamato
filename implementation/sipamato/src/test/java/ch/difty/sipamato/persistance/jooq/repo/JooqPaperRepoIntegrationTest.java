package ch.difty.sipamato.persistance.jooq.repo;

import static ch.difty.sipamato.db.h2.Tables.PAPER;
import static org.fest.assertions.api.Assertions.assertThat;

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
import ch.difty.sipamato.entity.Paper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SipamatoApplication.class)
@ActiveProfiles({ "DB_JOOQ" })
public class JooqPaperRepoIntegrationTest {

    private static final int RECORD_COUNT_PREPOPULATED = 1;
    private static final Long MAX_ID_PREPOPULATED = 1l;

    @Autowired
    private DSLContext dsl;

    @Autowired
    private JooqPaperRepo repo;

    @After
    public void teardown() {
        // Delete all books that were created in any test
        dsl.delete(PAPER).where(PAPER.ID.gt(MAX_ID_PREPOPULATED)).execute();
    }

    @Test
    public void findingAll() {
        List<Paper> papers = repo.findAll();
        assertThat(papers).hasSize(RECORD_COUNT_PREPOPULATED);
        assertThat(papers.get(0).getId()).isEqualTo(MAX_ID_PREPOPULATED.intValue());
    }

    @Test
    public void findingById_withExistingId_returnsEntity() {
        Paper paper = repo.findById(1);
        assertThat(paper.getId()).isEqualTo(MAX_ID_PREPOPULATED.intValue());
    }

    @Test
    public void findingById_withNonExistingId_returnsNull() {
        assertThat(repo.findById(-1)).isNull();
    }

    @Test
    public void addingRecord_savesRecordAndRefreshesId() {
        Paper p = makeMinimalPaper();
        assertThat(p.getId()).isNull();

        Paper saved = repo.add(p);
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull().isGreaterThan(MAX_ID_PREPOPULATED.intValue());
        assertThat(saved.getAuthors()).isEqualTo("a");
    }

    private Paper makeMinimalPaper() {
        Paper p = new Paper();
        p.setAuthors("a");
        p.setFirstAuthor("b");
        p.setFirstAuthorOverridden(true);
        p.setTitle("t");
        p.setLocation("l");
        p.setGoals("g");
        return p;
    }

    @Test
    public void updatingRecord() {
        Paper paper = repo.add(makeMinimalPaper());
        assertThat(paper).isNotNull();
        assertThat(paper.getId()).isNotNull().isGreaterThan(MAX_ID_PREPOPULATED.intValue());
        final int id = paper.getId();
        assertThat(paper.getAuthors()).isEqualTo("a");

        paper.setAuthors("b");
        repo.update(paper);
        assertThat(paper.getId()).isEqualTo(id);

        Paper newCopy = repo.findById(id);
        assertThat(newCopy).isNotEqualTo(paper);
        assertThat(newCopy.getId()).isEqualTo(id);
        assertThat(newCopy.getAuthors()).isEqualTo("b");
    }

    @Test
    public void deletingRecord() {
        Paper paper = repo.add(makeMinimalPaper());
        assertThat(paper).isNotNull();
        assertThat(paper.getId()).isNotNull().isGreaterThan(MAX_ID_PREPOPULATED.intValue());
        final int id = paper.getId();
        assertThat(paper.getAuthors()).isEqualTo("a");

        Paper deleted = repo.delete(id);
        assertThat(deleted.getId()).isEqualTo(id);

        assertThat(repo.findById(id)).isNull();
    }

}
