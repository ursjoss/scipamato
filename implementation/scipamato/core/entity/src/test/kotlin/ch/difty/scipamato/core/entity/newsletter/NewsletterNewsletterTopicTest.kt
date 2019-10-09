package ch.difty.scipamato.core.entity.newsletter

import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat

import org.junit.jupiter.api.Test

internal class NewsletterNewsletterTopicTest {

    private val newsletterId = 1
    private val newsletterTopicId = 2
    private val sort = 3
    private val title = "title"

    private val nnt = NewsletterNewsletterTopic(newsletterId, newsletterTopicId, sort, title)

    @Test
    fun getters() {
        assertThat(nnt.newsletterId).isEqualTo(newsletterId)
        assertThat(nnt.newsletterTopicId).isEqualTo(newsletterTopicId)
        assertThat(nnt.sort).isEqualTo(sort)
        assertThat(nnt.title).isEqualTo("title")
        assertThat(nnt.displayValue).isEqualTo(nnt.title)
    }

    @Test
    fun validatingToString() {
        assertThat(nnt.toString()).isEqualTo(
            "NewsletterNewsletterTopic(newsletterId=1, newsletterTopicId=2, sort=3, title=title)"
        )
    }
}