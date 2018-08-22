package ch.difty.scipamato.core.persistence.newsletter;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.common.persistence.paging.Sort;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.newsletter.Newsletter;
import ch.difty.scipamato.core.entity.newsletter.NewsletterFilter;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;
import ch.difty.scipamato.core.entity.newsletter.PublicationStatus;
import ch.difty.scipamato.core.entity.projection.PaperSlim;
import ch.difty.scipamato.core.persistence.JooqTransactionalIntegrationTest;

public class JooqNewsletterRepoIntegrationTest extends JooqTransactionalIntegrationTest {

    @Autowired
    private JooqNewsletterRepo repo;

    @Test
    public void findingAll() {
        assertThat(repo.findAll()).hasSize(2);
    }

    @Test
    public void findingById_withNonExistingId_returnsNull() {
        assertThat(repo.findById(-1)).isNull();
    }

    @Test
    public void findById_withExistingId_returnsRecord() {
        final Newsletter nl = repo.findById(1);
        assertThat(nl).isNotNull();
        assertThat(nl.getId()).isEqualTo(1);
        assertThat(nl.getIssue()).isEqualTo("1802");
        assertThat(nl.getIssueDate()).isEqualTo(LocalDate.parse("2018-02-01"));
        assertThat(nl.getPublicationStatus()).isEqualTo(PublicationStatus.PUBLISHED);
        assertThat(nl.getPapers()).hasSize(5);
        assertThat(nl.getPapers())
            .extracting(PaperSlim.PaperSlimFields.FIRST_AUTHOR.getName())
            .containsOnly("Turner", "Lanzinger", "Lanzinger", "Eeftens", "Kubesch");
        assertThat(nl.getTopics()).hasSize(3);
        assertThat(nl.getTopics())
            .extracting(NewsletterTopic.NewsletterTopicFields.TITLE.getName())
            .containsOnly("Ultrafeine Partikel", "Sterblichkeit", "Gesundheitsfolgenabschätzung");
    }

    @Test
    public void addingRecord_savesRecordAndRefreshesId() {
        Newsletter nl = makeMinimalNewsletter();
        assertThat(nl.getId()).isNull();

        Newsletter saved = repo.add(nl);
        assertThat(saved).isNotNull();
        assertThat(saved.getId())
            .isNotNull()
            .isGreaterThan(0);
        assertThat(saved.getIssue()).isEqualTo("test-issue");
    }

    private Newsletter makeMinimalNewsletter() {
        final Newsletter nl = new Newsletter();
        nl.setIssue("test-issue");
        nl.setIssueDate(LocalDate.now());
        nl.setPublicationStatus(PublicationStatus.CANCELLED);
        return nl;
    }

    @Test
    public void updatingRecord() {
        Newsletter nl = repo.add(makeMinimalNewsletter());
        assertThat(nl).isNotNull();
        assertThat(nl.getId())
            .isNotNull()
            .isGreaterThan(0);
        final int id = nl.getId();
        assertThat(nl.getIssue()).isEqualTo("test-issue");

        nl.setIssue("test-issue-modified");
        repo.update(nl);
        assertThat(nl.getId()).isEqualTo(id);

        Newsletter newCopy = repo.findById(id);
        assertThat(newCopy).isNotEqualTo(nl);
        assertThat(newCopy.getId()).isEqualTo(id);
        assertThat(newCopy.getIssue()).isEqualTo("test-issue-modified");
    }

    @Test
    public void deletingRecord() {
        Newsletter nl = repo.add(makeMinimalNewsletter());
        assertThat(nl).isNotNull();
        assertThat(nl.getId())
            .isNotNull()
            .isGreaterThan(0);
        final int id = nl.getId();
        assertThat(nl.getIssue()).isEqualTo("test-issue");

        Newsletter deleted = repo.delete(id, nl.getVersion());
        assertThat(deleted.getId()).isEqualTo(id);

        assertThat(repo.findById(id)).isNull();
    }

    @Test
    public void findingByFilter_withIssueFilter() {
        NewsletterFilter nf = new NewsletterFilter();
        nf.setIssueMask("1802");
        List<Newsletter> results = repo.findPageByFilter(nf, new PaginationRequest(Sort.Direction.ASC, "issueDate"));
        assertThat(results).hasSize(1);
        assertThat(results
            .get(0)
            .getIssue()).isEqualTo("1802");
    }

    @Test
    public void mergingPaperIntoNewsletter_withNewAssociation() {
        int newsletterId = 2;
        long paperId = 30l;
        String languCode = "en";

        Newsletter nl = repo.findById(newsletterId);
        assertThat(getIdsOfAssociatedPapers(nl)).doesNotContain(paperId);

        Optional<Paper.NewsletterLink> nlo = repo.mergePaperIntoNewsletter(newsletterId, paperId, 1, languCode);
        assertThat(nlo).isPresent();

        nlo = repo.mergePaperIntoNewsletter(newsletterId, paperId, 1, languCode);
        assertThat(nlo).isPresent();

        nl = repo.findById(newsletterId);
        assertThat(getIdsOfAssociatedPapers(nl)).contains(paperId);
    }

    private List<Long> getIdsOfAssociatedPapers(final Newsletter nl) {
        return nl
            .getPapers()
            .stream()
            .map(p -> p.getId())
            .collect(Collectors.toList());
    }

    @Test
    public void mergingPaperIntoNewsletter_withExistingAssociationToUpdate() {
        final int newsletterId = 2;
        final long paperId = 39l;
        final String languageCode = "en";

        Newsletter nl = repo.findById(newsletterId);
        assertThat(getIdsOfAssociatedPapers(nl)).contains(paperId);
        assertPaperIsAssignedToNewsletterWithTopic(null, paperId, nl);

        final int newTopicId = 1;
        final NewsletterTopic newTopic = new NewsletterTopic(newTopicId, "Ultrafeine Partikel");

        repo.mergePaperIntoNewsletter(newsletterId, paperId, newTopicId, languageCode);

        nl = repo.findById(newsletterId);

        assertThat(getIdsOfAssociatedPapers(nl)).contains(paperId);
        assertPaperIsAssignedToNewsletterWithTopic(newTopic, paperId, nl);
    }

    private void assertPaperIsAssignedToNewsletterWithTopic(final NewsletterTopic nt, final long paperId,
        final Newsletter nl) {
        List<PaperSlim> topiclessPapers = nl
            .getPapersByTopic()
            .get(nt);
        assertThat(topiclessPapers
            .stream()
            .filter(p -> p.getId() == paperId)
            .collect(Collectors.toList())).isNotEmpty();
    }

    @Test
    public void deletingPaperFromNewsletter_withExistingAssociation_managesToDeleteIt() {
        final int newsletterId = 2;
        final long paperId = 39l;
        Newsletter nl = repo.findById(newsletterId);
        assertThat(getIdsOfAssociatedPapers(nl)).contains(paperId);

        int count = repo.removePaperFromNewsletter(newsletterId, paperId);
        assertThat(count).isGreaterThan(0);

        nl = repo.findById(newsletterId);
        assertThat(getIdsOfAssociatedPapers(nl)).doesNotContain(paperId);
    }

    @Test
    public void deletingPaperFromNewsletter_withNonExistingRelation() {
        final int newsletterId = 2;
        final long paperId = -1l;
        Newsletter nl = repo.findById(newsletterId);
        assertThat(getIdsOfAssociatedPapers(nl)).doesNotContain(paperId);

        int count = repo.removePaperFromNewsletter(newsletterId, paperId);
        assertThat(count).isEqualTo(0);

        nl = repo.findById(newsletterId);
        assertThat(getIdsOfAssociatedPapers(nl)).doesNotContain(paperId);
    }

    @Test
    public void gettingNewsletterInStatusWorkInProgress() {
        final Optional<Newsletter> wipNl = repo.getNewsletterInStatusWorkInProgress();
        AssertionsForClassTypes
            .assertThat(wipNl)
            .isPresent();
        assertThat(wipNl
            .get()
            .getIssue()).isEqualTo("1804");
    }
}