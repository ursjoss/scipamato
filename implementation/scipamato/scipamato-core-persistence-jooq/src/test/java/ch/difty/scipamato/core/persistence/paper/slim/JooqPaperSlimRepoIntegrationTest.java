package ch.difty.scipamato.core.persistence.paper.slim;

import static ch.difty.scipamato.core.persistence.TestDbConstants.MAX_ID_PREPOPULATED;
import static ch.difty.scipamato.core.persistence.TestDbConstants.RECORD_COUNT_PREPOPULATED;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.core.entity.projection.NewsletterAssociation;
import ch.difty.scipamato.core.entity.projection.PaperSlim;
import ch.difty.scipamato.core.entity.search.PaperFilter;
import ch.difty.scipamato.core.entity.search.SearchCondition;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.persistence.JooqBaseIntegrationTest;

class JooqPaperSlimRepoIntegrationTest extends JooqBaseIntegrationTest {

    @Autowired
    private JooqPaperSlimRepo repo;

    @Test
    void findingAll() {
        List<PaperSlim> papers = repo.findAll();
        assertThat(papers).hasSize(RECORD_COUNT_PREPOPULATED);
    }

    @Test
    void findingById_withExistingId_returnsEntity() {
        PaperSlim paper = repo.findById(MAX_ID_PREPOPULATED);
        if (MAX_ID_PREPOPULATED > 0)
            assertThat(paper.getId()).isEqualTo(MAX_ID_PREPOPULATED);
        else
            assertThat(paper).isNull();
    }

    @Test
    void findingById_withNonExistingId_returnsNull() {
        assertThat(repo.findById(-1L)).isNull();
    }

    @Test
    void findingById_withExistingIdAndVersion_returnsEntityWithCorrectIdButMissingVersion() {
        PaperSlim paper = repo.findById(31L, 11, "en");
        assertThat(paper.getId()).isEqualTo(31L);
        assertThat(paper.getNumber()).isEqualTo(31L);
        assertThat(paper.getPublicationYear()).isEqualTo(2016);
        //noinspection SpellCheckingInspection
        assertThat(paper.getFirstAuthor()).isEqualTo("Lanzinger");
        assertThat(paper.getVersion()).isEqualTo(0);
        assertThat(paper.getTitle()).startsWith("Ultrafine");
        final NewsletterAssociation newsletterAssociation = paper.getNewsletterAssociation();
        assertThat(newsletterAssociation).isNotNull();
        assertThat(newsletterAssociation.getId()).isEqualTo(1L);
        assertThat(newsletterAssociation.getPublicationStatusId()).isEqualTo(1L);
        assertThat(newsletterAssociation.getIssue()).isEqualTo("1802");
        assertThat(newsletterAssociation.getHeadline()).isEqualTo("some headline");
    }

    @Test
    void findingById_withExistingIdAndWrongVersion_returnsNull() {
        PaperSlim paper = repo.findById(31L, 1, "en");
        assertThat(paper).isNull();
    }

    @Test
    void findingPageByFilter() {
        final PaperFilter pf = new PaperFilter();
        //noinspection SpellCheckingInspection
        pf.setAuthorMask("lanz");
        PaginationContext pc = new PaginationRequest(0, 10);
        List<PaperSlim> papers = repo.findPageByFilter(pf, pc);
        assertThat(papers).hasSize(2);
        assertThat(papers)
            .extracting("number")
            .containsExactlyInAnyOrder(33L, 31L);
    }

    @Test
    void bug21_queryingAllFieldsShouldNotThrowAnException() {
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
    void canQueryNewsletterFields() {
        PaperSlim paper = repo.findById(31L, "en");

        assertThat(paper).isNotNull();

        NewsletterAssociation na = paper.getNewsletterAssociation();
        assertThat(na).isNotNull();
        assertThat(na.getIssue()).isEqualTo("1802");
        assertThat(na.getPublicationStatusId()).isEqualTo(1);
        assertThat(na.getHeadline()).isEqualTo("some headline");
    }
}
