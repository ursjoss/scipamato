package ch.difty.scipamato.core.persistence

import ch.difty.scipamato.common.entity.newsletter.PublicationStatus
import ch.difty.scipamato.common.persistence.paging.PaginationRequest
import ch.difty.scipamato.common.persistence.paging.Sort
import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.entity.newsletter.Newsletter
import ch.difty.scipamato.core.entity.newsletter.NewsletterFilter
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic
import ch.difty.scipamato.core.persistence.newsletter.JooqNewsletterRepo
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeGreaterThan
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldHaveSize
import org.amshove.kluent.shouldNotBeEmpty
import org.amshove.kluent.shouldNotBeEqualTo
import org.amshove.kluent.shouldNotContain
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
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
        repo.findAll() shouldHaveSize 2
    }

    @Test
    fun findingById_withNonExistingId_returnsNull() {
        repo.findById(-1).shouldBeNull()
    }

    @Test
    fun findById_withExistingId_returnsRecord() {
        val nl = repo.findById(1) ?: fail { "Unable to add newsletter" }
        nl.id shouldBeEqualTo 1
        nl.issue shouldBeEqualTo "1802"
        nl.issueDate shouldBeEqualTo LocalDate.parse("2018-02-01")
        nl.publicationStatus shouldBeEqualTo PublicationStatus.PUBLISHED
        nl.papers.map { it.firstAuthor } shouldContainSame
            listOf("Turner", "Lanzinger", "Lanzinger", "Eeftens", "Kubesch")
        nl.topics.map { it.title } shouldContainSame
            listOf("Ultrafeine Partikel", "Sterblichkeit", "Gesundheitsfolgenabschätzung")
    }

    @Test
    fun addingRecord_savesRecordAndRefreshesId() {
        val nl = makeMinimalNewsletter()
        nl.id.shouldBeNull()

        val saved = repo.add(nl) ?: fail { "Unable to add newsletter" }
        saved.id?.shouldBeGreaterThan(0)
        saved.issue shouldBeEqualTo "test-issue"
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
        val nl = repo.add(makeMinimalNewsletter()) ?: fail { "Unable to add newsletter" }
        nl.id?.shouldBeGreaterThan(0)
        val id: Int = nl.id ?: error("id must no be null now")
        nl.issue shouldBeEqualTo "test-issue"

        nl.issue = "test-issue-modified"
        repo.update(nl)
        nl.id as Int shouldBeEqualTo id

        val newCopy = repo.findById(id) ?: fail { "Unable to find newsletter" }
        newCopy shouldNotBeEqualTo nl
        newCopy.id shouldBeEqualTo id
        newCopy.issue shouldBeEqualTo "test-issue-modified"
    }

    @Test
    fun deletingRecord() {
        val nl = repo.add(makeMinimalNewsletter()) ?: fail { "Unable to add newsletter" }
        nl.id?.shouldBeGreaterThan(0)
        val id = nl.id ?: error("id must no be null now")
        nl.issue shouldBeEqualTo "test-issue"

        val deleted = repo.delete(id, nl.version)
        deleted.id shouldBeEqualTo id

        repo.findById(id).shouldBeNull()
    }

    @Test
    fun findingByFilter_withIssueFilter() {
        val nf = NewsletterFilter()
        nf.issueMask = "1802"
        val results = repo.findPageByFilter(nf, PaginationRequest(Sort.Direction.ASC, "issueDate"))
        results shouldHaveSize 1
        results.first().issue shouldBeEqualTo "1802"
    }

    @Test
    fun findingByFilter_withTopicFilter() {
        val nf = NewsletterFilter()
        nf.newsletterTopic = NewsletterTopic(54, "foo")
        val results = repo.findPageByFilter(nf, PaginationRequest(Sort.Direction.ASC, "issueDate"))
        results.shouldBeEmpty()
    }

    @Test
    fun mergingPaperIntoNewsletter_withNewAssociation() {
        val newsletterId = 2
        val paperId = 30L
        val langCode = "en"

        var nl = repo.findById(newsletterId) ?: fail { "Unable to find newsletter" }
        nl.papers.map { it.id } shouldNotContain paperId

        var nlo: java.util.Optional<Paper.NewsletterLink> =
            repo.mergePaperIntoNewsletter(newsletterId, paperId, 1, langCode)
        nlo.isPresent.shouldBeTrue()

        nlo = repo.mergePaperIntoNewsletter(newsletterId, paperId, 1, langCode)
        nlo.isPresent.shouldBeTrue()

        nl = repo.findById(newsletterId) ?: fail { "Unable to find newsletter" }
        nl.papers.map { it.id } shouldContain paperId
    }

    @Test
    fun mergingPaperIntoNewsletter_withExistingAssociationToUpdate() {
        val newsletterId = 2
        val paperId = 39L
        val languageCode = "en"

        var nl = repo.findById(newsletterId) ?: fail { "Unable to find newsletter" }
        nl.papers.map { it.id } shouldContain paperId
        assertPaperIsAssignedToNewsletterWithTopic(null, paperId, nl)

        val newTopicId = 1
        val newTopic = NewsletterTopic(newTopicId, "Ultrafeine Partikel")

        repo.mergePaperIntoNewsletter(newsletterId, paperId, newTopicId, languageCode)

        nl = repo.findById(newsletterId) ?: fail { "Unable to find newsletter" }

        nl.papers.map { it.id } shouldContain paperId
        assertPaperIsAssignedToNewsletterWithTopic(newTopic, paperId, nl)
    }

    private fun assertPaperIsAssignedToNewsletterWithTopic(nt: NewsletterTopic?, paperId: Long, nl: Newsletter) {
        val topicLessPapers = nl.papersByTopic[nt]
        topicLessPapers?.filter { it.id == paperId }?.shouldNotBeEmpty()
    }

    @Test
    fun deletingPaperFromNewsletter_withExistingAssociation_managesToDeleteIt() {
        val newsletterId = 2
        val paperId = 39L
        var nl = repo.findById(newsletterId) ?: fail { "Unable to find newsletter" }
        nl.papers.map { it.id } shouldContain paperId

        val count = repo.removePaperFromNewsletter(newsletterId, paperId)
        count shouldBeGreaterThan 0

        nl = repo.findById(newsletterId) ?: fail { "Unable to find newsletter" }
        nl.papers.map { it.id } shouldNotContain paperId
    }

    @Test
    fun deletingPaperFromNewsletter_withNonExistingRelation() {
        val newsletterId = 2
        val paperId = -1L
        var nl = repo.findById(newsletterId) ?: fail { "Unable to find newsletter" }
        nl.papers.map { it.id } shouldNotContain paperId

        val count = repo.removePaperFromNewsletter(newsletterId, paperId)
        count shouldBeEqualTo 0

        nl = repo.findById(newsletterId) ?: fail { "Unable to find newsletter" }
        nl.papers.map { it.id } shouldNotContain paperId
    }

    @Test
    fun gettingNewsletterInStatusWorkInProgress() {
        val wipNl = repo.newsletterInStatusWorkInProgress
        wipNl.isPresent.shouldBeTrue()
        wipNl.get().issue shouldBeEqualTo "1804"
    }
}
