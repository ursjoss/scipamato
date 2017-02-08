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
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.filter.SearchCondition;
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
        PaperSlim paper = repo.findById((long) MAX_ID_PREPOPULATED);
        assertThat(paper.getId()).isEqualTo(MAX_ID_PREPOPULATED);
    }

    @Test
    public void findingById_withNonExistingId_returnsNull() {
        assertThat(repo.findById(-1l)).isNull();
    }

    @Test
    public void bug21_queryingAllFieldsShouldNotThrowAnException() {
        final SearchOrder so = new SearchOrder();
        final SearchCondition sc = new SearchCondition();
        so.add(sc);

        final String x = "";
        sc.setId("1");
        sc.setDoi(x);
        sc.setPmId(x);
        sc.setAuthors(x);
        sc.setFirstAuthor(x);
        sc.setFirstAuthorOverridden(true);
        sc.setTitle(x);
        sc.setLocation(x);
        sc.setPublicationYear("1984");

        sc.setGoals(x);
        sc.setPopulation(x);
        sc.setMethods(x);

        sc.setPopulationPlace(x);
        sc.setPopulationParticipants(x);
        sc.setPopulationDuration(x);
        sc.setExposurePollutant(x);
        sc.setExposureAssessment(x);
        sc.setMethodStudyDesign(x);
        sc.setMethodOutcome(x);
        sc.setMethodStatistics(x);
        sc.setMethodConfounders(x);

        sc.setResult(x);
        sc.setComment(x);
        sc.setIntern(x);

        sc.setResultExposureRange(x);
        sc.setResultEffectEstimate(x);

        sc.setOriginalAbstract(x);

        assertThat(repo.findBySearchOrder(so)).isEmpty();
    }

}
