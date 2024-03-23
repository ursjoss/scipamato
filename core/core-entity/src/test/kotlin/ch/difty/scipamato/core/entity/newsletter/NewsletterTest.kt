package ch.difty.scipamato.core.entity.newsletter

import ch.difty.scipamato.common.entity.newsletter.PublicationStatus
import ch.difty.scipamato.core.entity.Jsr303ValidatedEntityTest
import ch.difty.scipamato.core.entity.newsletter.Newsletter.NewsletterFields.ISSUE
import ch.difty.scipamato.core.entity.newsletter.Newsletter.NewsletterFields.PUBLICATION_STATUS
import ch.difty.scipamato.core.entity.projection.PaperSlim
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate

@Suppress("SpellCheckingInspection")
internal class NewsletterTest : Jsr303ValidatedEntityTest<Newsletter>(Newsletter::class.java) {

    private val topic1 = NewsletterTopic(1, "sometopic")
    private val paper1 = PaperSlim()
    private val paper2 = PaperSlim()

    override fun newValidEntity(): Newsletter {
        val nl = Newsletter()
        nl.id = 1
        nl.issue = "2018-03"
        nl.issueDate = LocalDate.parse("2018-03-26")
        nl.publicationStatus = PublicationStatus.WIP

        paper1.id = 1L
        paper1.title = "somepaper"
        nl.addPaper(paper1, topic1)

        paper2.id = 2L
        paper2.title = "otherpaper"
        nl.addPaper(paper2, null)
        return nl
    }

    override val toString =
        """Newsletter(issue=2018-03, issueDate=2018-03-26, publicationStatus=WIP,
        | papersByTopic={null=[PaperSlim(number=null, firstAuthor=null, publicationYear=null, title=otherpaper)],
        |  NewsletterTopic(title=sometopic)=[PaperSlim(number=null, firstAuthor=null, publicationYear=null,
        |   title=somepaper)]})""".trimMargin()
    override val displayValue: String = newValidEntity().issue

    @Test
    fun get() {
        val nl = newValidEntity()
        nl.id shouldBeEqualTo 1
        nl.issue shouldBeEqualTo "2018-03"
        nl.issueDate shouldBeEqualTo LocalDate.parse("2018-03-26")
        nl.publicationStatus shouldBeEqualTo PublicationStatus.WIP
        nl.papers shouldContainSame listOf(paper1, paper2)
        nl.topics shouldContainSame listOf(topic1, null)
    }

    @Test
    fun validatingNewsletter_withIssueAndPublicationStatus_succeeds() {
        verifySuccessfulValidation(newValidEntity())
    }

    @Test
    fun validatingNewsletter_withNullIssue_fails() {
        val nl = newValidEntity()
        nl.issue = null
        validateAndAssertFailure(nl, ISSUE, null, "{jakarta.validation.constraints.NotNull.message}")
    }

    @Test
    fun validatingNewsletter_withNullPublicationStatus_fails() {
        val nl = newValidEntity()
        nl.publicationStatus = null
        validateAndAssertFailure(nl, PUBLICATION_STATUS, null, "{jakarta.validation.constraints.NotNull.message}")
    }

    @Test
    fun isDeletable_delegatesToPublicationStatus() {
        val nl = newValidEntity()

        nl.publicationStatus = PublicationStatus.WIP
        nl.isDeletable.shouldBeTrue()
        nl.publicationStatus = PublicationStatus.CANCELLED
        nl.isDeletable.shouldBeFalse()
    }

    @Test
    fun cannotAddPaperWithNullId() {
        Assertions.assertThrows(NullPointerException::class.java) {
            newValidEntity().addPaper(PaperSlim(), NewsletterTopic(1, "t1"))
        }
    }

    @Test
    fun canAddAdditionalPaperToExistingTopic() {
        val nl = newValidEntity()
        nl.topics shouldContainSame listOf(null, topic1)
        nl.papers shouldContainSame listOf(paper1, paper2)

        val oneMore = PaperSlim()
        oneMore.id = 10L
        oneMore.title = "foo"
        nl.addPaper(oneMore, topic1)

        nl.topics shouldContainSame listOf(null, topic1)
        nl.papers shouldContainSame listOf(paper1, paper2, oneMore)
    }

    @Test
    fun canAddAdditionalPaperToNewTopic() {
        val nl = newValidEntity()
        nl.topics shouldContainSame listOf(null, topic1)
        nl.papers shouldContainSame listOf(paper1, paper2)

        val oneMoreTopic = NewsletterTopic(1, "t1")
        oneMoreTopic.title = "anotherTopic"

        val oneMore = PaperSlim()
        oneMore.id = 10L
        oneMore.title = "foo"
        nl.addPaper(oneMore, oneMoreTopic)

        nl.topics shouldContainSame listOf(null, topic1, oneMoreTopic)
        nl.papers shouldContainSame listOf(paper1, paper2, oneMore)
    }

    @Test
    fun canAddPaperWithNullTopic_evenIfItWasAddedWithTopicBefore() {
        val nl = newValidEntity()
        nl.topics shouldContainSame listOf(null, topic1)
        nl.papers shouldContainSame listOf(paper1, paper2)

        nl.addPaper(paper1, null)

        nl.topics shouldHaveSize 1
        nl.topics shouldContainSame listOf(null)
        nl.papers shouldContainSame listOf(paper1, paper2)
    }

    @Test
    fun canAddPaperWithTopic_evenIfItWasAddedWithNullTopicBefore() {
        val nl = newValidEntity()
        nl.topics shouldContainSame listOf(null, topic1)
        nl.papers shouldContainSame listOf(paper1, paper2)

        nl.addPaper(paper2, topic1)

        nl.topics shouldContainSame listOf(topic1)
        nl.papers shouldContainSame listOf(paper1, paper2)
    }

    @Test
    fun canAddPaperWithTopic_evenIfItWasAddedWithOtherTopicBefore() {
        val nl = newValidEntity()
        nl.topics shouldContainSame listOf(null, topic1)
        nl.papers shouldContainSame listOf(paper1, paper2)

        val oneMoreTopic = NewsletterTopic(1, "t1")
        oneMoreTopic.title = "anotherTopic"

        nl.addPaper(paper2, oneMoreTopic)

        nl.topics shouldContainSame listOf(topic1, oneMoreTopic)
        nl.papers shouldContainSame listOf(paper1, paper2)
    }

    @Test
    fun canAddNullTopic_hasNoEffectEffect() {
        val nl = Newsletter()
        nl.id = 1
        nl.issue = "2018-03"
        nl.issueDate = LocalDate.parse("2018-03-26")
        nl.publicationStatus = PublicationStatus.WIP

        paper1.id = 1L
        paper1.title = "somepaper"
        nl.addPaper(paper1, topic1)

        nl.topics shouldContainSame listOf(topic1)
        nl.papers shouldContainSame listOf(paper1)

        val oneMore = PaperSlim()
        oneMore.id = 10L
        oneMore.title = "foo"

        nl.addPaper(oneMore, null)

        nl.topics shouldContainSame listOf(null, topic1)
        nl.papers shouldContainSame listOf(paper1, oneMore)
    }

    @Test
    fun canReassignAssociatedPaperFromOneTopicToAnother() {
        val nl = newValidEntity()
        nl.topics shouldContainSame listOf(null, topic1)
        nl.papers shouldContainSame listOf(paper1, paper2)
        nl.papersByTopic[null]?.shouldContainAll(listOf(paper2))

        nl.addPaper(paper2, topic1)

        nl.topics shouldContainSame listOf(topic1)
        nl.papers shouldContainSame listOf(paper1, paper2)
        nl.papersByTopic[topic1]?.shouldContainAll(listOf(paper2))
    }
}
