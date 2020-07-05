package ch.difty.scipamato.core.web.model

import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import org.amshove.kluent.shouldContainSame
import org.junit.jupiter.api.Test

internal class NewsletterTopicModelTest : ModelTest() {

    @Test
    fun loading_delegatesToNewsletterTopicService() {
        val languageCode = "de"
        val topics = listOf(NewsletterTopic(1, "t1"), NewsletterTopic(2, "t2"))
        every { newsletterTopicServiceMock.findAll(languageCode) } returns topics
        val model = NewsletterTopicModel("de")

        model.load().map { it.title } shouldContainSame listOf("t1", "t2")

        verify { newsletterTopicServiceMock.findAll(languageCode) }
        confirmVerified(newsletterTopicServiceMock)
    }
}
