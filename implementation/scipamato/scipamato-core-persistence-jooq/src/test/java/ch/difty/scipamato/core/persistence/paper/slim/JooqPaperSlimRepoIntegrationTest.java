package ch.difty.scipamato.core.persistence.paper.slim;

import static ch.difty.scipamato.core.persistence.TestDbConstants.MAX_ID_PREPOPULATED;
import static ch.difty.scipamato.core.persistence.TestDbConstants.RECORD_COUNT_PREPOPULATED;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.difty.scipamato.core.entity.projection.NewsletterAssociation;
import ch.difty.scipamato.core.entity.projection.PaperSlim;
import ch.difty.scipamato.core.entity.search.SearchCondition;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.persistence.JooqTransactionalIntegrationTest;

public class JooqPaperSlimRepoIntegrationTest extends JooqTransactionalIntegrationTest {

    @Autowired
    private JooqPaperSlimRepo repo;

    @Test
    public void findingAll() {
        List<PaperSlim> papers = repo.findAll();
        assertThat(papers).hasSize(RECORD_COUNT_PREPOPULATED);
    }

    @Test
    public void findingById_withExistingId_returnsEntity() {
        PaperSlim paper = repo.findById(MAX_ID_PREPOPULATED);
        if (MAX_ID_PREPOPULATED > 0)
            assertThat(paper.getId()).isEqualTo(MAX_ID_PREPOPULATED);
        else
            assertThat(paper).isNull();
    }

    @Test
    public void findingById_withNonExistingId_returnsNull() {
        assertThat(repo.findById(-1L)).isNull();
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

    @Test
    public void canQueryNewsletterFields() {
        PaperSlim paper = repo.findById(31l, "en");

        assertThat(paper).isNotNull();

        NewsletterAssociation na = paper.getNewsletterAssociation();
        assertThat(na).isNotNull();
        assertThat(na.getIssue()).isEqualTo("1802");
        assertThat(na.getPublicationStatusId()).isEqualTo(1);
        assertThat(na.getHeadline()).isEqualTo("some headline");
    }
}
