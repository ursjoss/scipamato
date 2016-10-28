package ch.difty.sipamato.persistance.jooq.paper.slim;

import static ch.difty.sipamato.persistance.jooq.TestDbConstants.MAX_ID_PREPOPULATED;
import static ch.difty.sipamato.persistance.jooq.TestDbConstants.RECORD_COUNT_PREPOPULATED;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import ch.difty.sipamato.SipamatoApplication;
import ch.difty.sipamato.entity.projection.PaperSlim;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SipamatoApplication.class)
@ActiveProfiles({ "DB_JOOQ" })
public class JooqPaperSlimRepoIntegrationTest {

    @Autowired
    private JooqPaperSlimRepo repo;

    @Test
    public void findingAll() {
        List<PaperSlim> papers = repo.findAll();
        assertThat(papers).hasSize(RECORD_COUNT_PREPOPULATED);
        assertThat(papers.get(0).getId()).isEqualTo(1);
        assertThat(papers.get(1).getId()).isEqualTo(2);
        assertThat(papers.get(2).getId()).isEqualTo(3);
        assertThat(papers.get(3).getId()).isEqualTo(4);
    }

    @Test
    public void findingById_withExistingId_returnsEntity() {
        PaperSlim paper = repo.findById(1l);
        paper = repo.findById((long) RECORD_COUNT_PREPOPULATED);
        assertThat(paper.getId()).isEqualTo(MAX_ID_PREPOPULATED);
    }

    @Test
    public void findingById_withNonExistingId_returnsNull() {
        assertThat(repo.findById(-1l)).isNull();
    }

}
