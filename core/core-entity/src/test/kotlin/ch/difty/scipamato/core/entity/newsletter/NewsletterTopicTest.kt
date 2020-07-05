package ch.difty.scipamato.core.entity.newsletter

import ch.difty.scipamato.core.entity.Jsr303ValidatedEntityTest
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic.NewsletterTopicFields.TITLE
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

@Suppress("SpellCheckingInspection")
internal class NewsletterTopicTest : Jsr303ValidatedEntityTest<NewsletterTopic>(NewsletterTopic::class.java) {

    override fun newValidEntity(): NewsletterTopic {
        return NewsletterTopic(10, "sometopic")
    }

    override val toString = "NewsletterTopic(title=sometopic)"

    override val displayValue: String = newValidEntity().title

    @Test
    fun get() {
        val nt = newValidEntity()
        nt.id shouldBeEqualTo 10
        nt.title shouldBeEqualTo "sometopic"
    }

    @Test
    fun validatingNewsletterTopic_withCompleteInformation_succeeds() {
        verifySuccessfulValidation(newValidEntity())
    }

    @Test
    fun validatingNewsletter_withNullIssue_fails() {
        val nt = newValidEntity()
        nt.title = null
        validateAndAssertFailure(nt, TITLE, null, "{javax.validation.constraints.NotNull.message}")
    }
}
