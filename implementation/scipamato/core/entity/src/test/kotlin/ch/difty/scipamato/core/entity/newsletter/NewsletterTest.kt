package ch.difty.scipamato.core.entity.newsletter

import ch.difty.scipamato.common.NullArgumentException
import ch.difty.scipamato.common.entity.newsletter.PublicationStatus
import ch.difty.scipamato.core.entity.Jsr303ValidatedEntityTest
import ch.difty.scipamato.core.entity.newsletter.Newsletter.NewsletterFields.ISSUE
import ch.difty.scipamato.core.entity.newsletter.Newsletter.NewsletterFields.PUBLICATION_STATUS
import ch.difty.scipamato.core.entity.projection.PaperSlim
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

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

    override val toString = "Newsletter(issue=2018-03, issueDate=2018-03-26, publicationStatus=WIP, " +
            "papersByTopic={null=[PaperSlim(number=null, firstAuthor=null, publicationYear=null, title=otherpaper)], " +
            "NewsletterTopic(title=sometopic)=[PaperSlim(number=null, firstAuthor=null, publicationYear=null, title=somepaper)]})"

    override val displayValue = newValidEntity().issue

    @Test
    fun get() {
        val nl = newValidEntity()
        assertThat(nl.id).isEqualTo(1)
        assertThat(nl.issue).isEqualTo("2018-03")
        assertThat(nl.issueDate).isEqualTo(LocalDate.parse("2018-03-26"))
        assertThat(nl.publicationStatus).isEqualTo(PublicationStatus.WIP)
        assertThat(nl.papers).hasSize(2).containsOnly(paper1, paper2)
        assertThat(nl.topics).hasSize(2).containsOnly(topic1, null)
    }

    @Test
    fun validatingNewsletter_withIssueAndPublicationStatus_succeeds() {
        verifySuccessfulValidation(newValidEntity())
    }

    @Test
    fun validatingNewsletter_withNullIssue_fails() {
        val nl = newValidEntity()
        nl.issue = null
        validateAndAssertFailure(nl, ISSUE, null, "{javax.validation.constraints.NotNull.message}")
    }

    @Test
    fun validatingNewsletter_withNullPublicationStatus_fails() {
        val nl = newValidEntity()
        nl.publicationStatus = null
        validateAndAssertFailure(nl, PUBLICATION_STATUS, null, "{javax.validation.constraints.NotNull.message}")
    }

    @Test
    fun isDeletable_delegatesToPublicationStatus() {
        val nl = newValidEntity()

        nl.publicationStatus = PublicationStatus.WIP
        assertThat(nl.isDeletable).isTrue()
        nl.publicationStatus = PublicationStatus.CANCELLED
        assertThat(nl.isDeletable).isFalse()
    }

    @Test
    fun cannotAddNullPaper() {
        assertDegenerateParameter(newValidEntity(), null, "paper")
    }

    @Test
    fun cannotAddPaperWithNullId() {
        assertDegenerateParameter(newValidEntity(), PaperSlim(), "paper.id")
    }

    private fun assertDegenerateParameter(nl: Newsletter, p: PaperSlim?, prmName: String) {
        try {
            nl.addPaper(p, NewsletterTopic(1, "t1"))
            Assertions.fail<Any>("should have thrown exception")
        } catch (ex: Exception) {
            assertThat(ex)
                    .isInstanceOf(NullArgumentException::class.java)
                    .hasMessage("$prmName must not be null.")
        }

    }

    @Test
    fun canAddAdditionalPaperToExistingTopic() {
        val nl = newValidEntity()
        assertThat(nl.topics).containsOnly(null, topic1)
        assertThat(nl.papers).containsOnly(paper1, paper2)

        val oneMore = PaperSlim()
        oneMore.id = 10L
        oneMore.title = "foo"
        nl.addPaper(oneMore, topic1)

        assertThat(nl.topics).containsOnly(null, topic1)
        assertThat(nl.papers).containsOnly(paper1, paper2, oneMore)
    }

    @Test
    fun canAddAdditionalPaperToNewTopic() {
        val nl = newValidEntity()
        assertThat(nl.topics).containsOnly(null, topic1)
        assertThat(nl.papers).containsOnly(paper1, paper2)

        val oneMoreTopic = NewsletterTopic(1, "t1")
        oneMoreTopic.title = "anotherTopic"

        val oneMore = PaperSlim()
        oneMore.id = 10L
        oneMore.title = "foo"
        nl.addPaper(oneMore, oneMoreTopic)

        assertThat(nl.topics).containsOnly(null, topic1, oneMoreTopic)
        assertThat(nl.papers).containsOnly(paper1, paper2, oneMore)
    }

    @Test
    fun canAddPaperWithNullTopic_evenIfItWasAddedWithTopicBefore() {
        val nl = newValidEntity()
        assertThat(nl.topics).containsOnly(null, topic1)
        assertThat(nl.papers).containsOnly(paper1, paper2)

        nl.addPaper(paper1, null)

        assertThat(nl.topics).hasSize(1)
        assertThat(nl.topics).containsNull()
        assertThat(nl.papers).containsOnly(paper1, paper2)
    }

    @Test
    fun canAddPaperWithTopic_evenIfItWasAddedWithNullTopicBefore() {
        val nl = newValidEntity()
        assertThat(nl.topics).containsOnly(null, topic1)
        assertThat(nl.papers).containsOnly(paper1, paper2)

        nl.addPaper(paper2, topic1)

        assertThat(nl.topics).containsOnly(topic1)
        assertThat(nl.papers).containsOnly(paper1, paper2)
    }

    @Test
    fun canAddPaperWithTopic_evenIfItWasAddedWithOtherTopicBefore() {
        val nl = newValidEntity()
        assertThat(nl.topics).containsOnly(null, topic1)
        assertThat(nl.papers).containsOnly(paper1, paper2)

        val oneMoreTopic = NewsletterTopic(1, "t1")
        oneMoreTopic.title = "anotherTopic"

        nl.addPaper(paper2, oneMoreTopic)

        assertThat(nl.topics).containsOnly(topic1, oneMoreTopic)
        assertThat(nl.papers).containsOnly(paper1, paper2)
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

        assertThat(nl.topics).containsOnly(topic1)
        assertThat(nl.papers).containsOnly(paper1)

        val oneMore = PaperSlim()
        oneMore.id = 10L
        oneMore.title = "foo"

        nl.addPaper(oneMore, null)

        assertThat(nl.topics).containsOnly(null, topic1)
        assertThat(nl.papers).containsOnly(paper1, oneMore)
    }

    @Test
    fun canReassignAssociatedPaperFromOneTopicToAnother() {
        val nl = newValidEntity()
        assertThat(nl.topics).containsOnly(null, topic1)
        assertThat(nl.papers).containsOnly(paper1, paper2)
        assertThat(nl.papersByTopic[null]).contains(paper2)

        nl.addPaper(paper2, topic1)

        assertThat(nl.topics).containsOnly(topic1)
        assertThat(nl.papers).containsOnly(paper1, paper2)
        assertThat(nl.papersByTopic[topic1]).contains(paper2)
    }
}
