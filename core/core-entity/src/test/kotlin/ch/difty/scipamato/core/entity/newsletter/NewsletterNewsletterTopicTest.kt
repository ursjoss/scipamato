package ch.difty.scipamato.core.entity.newsletter

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

internal class NewsletterNewsletterTopicTest {

    private val newsletterId = 1
    private val newsletterTopicId = 2
    private val sort = 3
    private val title = "title"

    private val nnt = NewsletterNewsletterTopic(newsletterId, newsletterTopicId, sort, title)

    @Test
    fun getters() {
        nnt.newsletterId shouldBeEqualTo newsletterId
        nnt.newsletterTopicId shouldBeEqualTo newsletterTopicId
        nnt.sort shouldBeEqualTo sort
        nnt.title shouldBeEqualTo "title"
        nnt.displayValue shouldBeEqualTo nnt.title
    }

    @Test
    fun validatingToString() {
        nnt.toString() shouldBeEqualTo
            "NewsletterNewsletterTopic(newsletterId=1, newsletterTopicId=2, sort=3, title=title)"
    }
}
