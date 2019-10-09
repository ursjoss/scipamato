package ch.difty.scipamato.core.persistence

import ch.difty.scipamato.common.entity.newsletter.PublicationStatus
import ch.difty.scipamato.common.persistence.paging.PaginationRequest
import ch.difty.scipamato.common.persistence.paging.Sort
import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.entity.newsletter.Newsletter
import ch.difty.scipamato.core.entity.newsletter.NewsletterFilter
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic
import ch.difty.scipamato.core.persistence.newsletter.JooqNewsletterRepo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.LocalDate

@JooqTest
@Testcontainers
@Suppress("TooManyFunctions", "FunctionName", "SpellCheckingInspection", "MagicNumber", "SameParameterValue")
internal open class JooqNewsletterRepoIntegrationTest {

    @Autowired
    private lateinit var repo: JooqNewsletterRepo

    @Test
    fun findingAll() {
        assertThat(repo.findAll()).hasSize(2)
    }

    @Test
    fun findingById_withNonExistingId_returnsNull() {
        assertThat(repo.findById(-1) == null).isTrue()
    }

    @Test
    fun findById_withExistingId_returnsRecord() {
        val nl = repo.findById(1)
        assertThat(nl.id).isEqualTo(1)
        assertThat(nl.issue).isEqualTo("1802")
        assertThat(nl.issueDate).isEqualTo(LocalDate.parse("2018-02-01"))
        assertThat(nl.publicationStatus).isEqualTo(PublicationStatus.PUBLISHED)
        assertThat(nl.papers.map { it.firstAuthor }).containsOnly(
            "Turner", "Lanzinger", "Lanzinger", "Eeftens", "Kubesch"
        )
        assertThat(nl.topics.map { it.title }).containsOnly(
            "Ultrafeine Partikel", "Sterblichkeit", "Gesundheitsfolgenabschätzung"
        )
    }

    @Test
    fun addingRecord_savesRecordAndRefreshesId() {
        val nl = makeMinimalNewsletter()
        assertThat(nl.id == null).isTrue()

        val saved = repo.add(nl)
        assertThat(saved.id).isGreaterThan(0)
        assertThat(saved.issue).isEqualTo("test-issue")
    }

    private fun makeMinimalNewsletter(): Newsletter {
        return Newsletter().apply {
            issue = "test-issue"
            issueDate = LocalDate.now()
            publicationStatus = PublicationStatus.CANCELLED
        }
    }

    @Test
    fun updatingRecord() {
        val nl = repo.add(makeMinimalNewsletter())
        assertThat(nl.id).isGreaterThan(0)
        val id = nl.id
        assertThat(nl.issue).isEqualTo("test-issue")

        nl.issue = "test-issue-modified"
        repo.update(nl)
        assertThat(nl.id).isEqualTo(id)

        val newCopy = repo.findById(id)
        assertThat(newCopy).isNotEqualTo(nl)
        assertThat(newCopy.id).isEqualTo(id)
        assertThat(newCopy.issue).isEqualTo("test-issue-modified")
    }

    @Test
    fun deletingRecord() {
        val nl = repo.add(makeMinimalNewsletter())
        assertThat(nl.id).isGreaterThan(0)
        val id = nl.id
        assertThat(nl.issue).isEqualTo("test-issue")

        val deleted = repo.delete(id, nl.version)
        assertThat(deleted.id).isEqualTo(id)

        assertThat(repo.findById(id) == null).isTrue()
    }

    @Test
    fun findingByFilter_withIssueFilter() {
        val nf = NewsletterFilter()
        nf.issueMask = "1802"
        val results = repo.findPageByFilter(nf, PaginationRequest(Sort.Direction.ASC, "issueDate"))
        assertThat(results).hasSize(1)
        assertThat(results.first().issue).isEqualTo("1802")
    }

    @Test
    fun findingByFilter_withTopicFilter() {
        val nf = NewsletterFilter()
        nf.newsletterTopic = NewsletterTopic(54, "foo")
        val results = repo.findPageByFilter(nf, PaginationRequest(Sort.Direction.ASC, "issueDate"))
        assertThat(results).isEmpty()
    }

    @Test
    fun mergingPaperIntoNewsletter_withNewAssociation() {
        val newsletterId = 2
        val paperId = 30L
        val langCode = "en"

        var nl = repo.findById(newsletterId)
        assertThat(nl.papers.map { it.id }).doesNotContain(paperId)

        var nlo: java.util.Optional<Paper.NewsletterLink> =
            repo.mergePaperIntoNewsletter(newsletterId, paperId, 1, langCode)
        assertThat(nlo).isPresent

        nlo = repo.mergePaperIntoNewsletter(newsletterId, paperId, 1, langCode)
        assertThat(nlo).isPresent

        nl = repo.findById(newsletterId)
        assertThat(nl.papers.map { it.id }).contains(paperId)
    }

    @Test
    fun mergingPaperIntoNewsletter_withExistingAssociationToUpdate() {
        val newsletterId = 2
        val paperId = 39L
        val languageCode = "en"

        var nl = repo.findById(newsletterId)
        assertThat(nl.papers.map { it.id }).contains(paperId)
        assertPaperIsAssignedToNewsletterWithTopic(null, paperId, nl)

        val newTopicId = 1
        val newTopic = NewsletterTopic(newTopicId, "Ultrafeine Partikel")

        repo.mergePaperIntoNewsletter(newsletterId, paperId, newTopicId, languageCode)

        nl = repo.findById(newsletterId)

        assertThat(nl.papers.map { it.id }).contains(paperId)
        assertPaperIsAssignedToNewsletterWithTopic(newTopic, paperId, nl)
    }

    private fun assertPaperIsAssignedToNewsletterWithTopic(nt: NewsletterTopic?, paperId: Long, nl: Newsletter) {
        val topicLessPapers = nl.papersByTopic[nt]
        assertThat(topicLessPapers?.filter { it.id == paperId }).isNotEmpty
    }

    @Test
    fun deletingPaperFromNewsletter_withExistingAssociation_managesToDeleteIt() {
        val newsletterId = 2
        val paperId = 39L
        var nl = repo.findById(newsletterId)
        assertThat(nl.papers.map { it.id }).contains(paperId)

        val count = repo.removePaperFromNewsletter(newsletterId, paperId)
        assertThat(count).isGreaterThan(0)

        nl = repo.findById(newsletterId)
        assertThat(nl.papers.map { it.id }).doesNotContain(paperId)
    }

    @Test
    fun deletingPaperFromNewsletter_withNonExistingRelation() {
        val newsletterId = 2
        val paperId = -1L
        var nl = repo.findById(newsletterId)
        assertThat(nl.papers.map { it.id }).doesNotContain(paperId)

        val count = repo.removePaperFromNewsletter(newsletterId, paperId)
        assertThat(count).isEqualTo(0)

        nl = repo.findById(newsletterId)
        assertThat(nl.papers.map { it.id }).doesNotContain(paperId)
    }

    @Test
    fun gettingNewsletterInStatusWorkInProgress() {
        val wipNl = repo.newsletterInStatusWorkInProgress
        assertThat(wipNl).isPresent
        assertThat(wipNl.get().issue).isEqualTo("1804")
    }
}
