package ch.difty.scipamato.core.entity.newsletter

import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBeEqualTo
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

    @Test
    fun equals_withItself_isEqual() {
        val ntt1 = NewsletterNewsletterTopic(1, 2, 1, "foo")
        (ntt1 == ntt1) shouldBe true
    }

    @Test
    fun equals_withNull_isDifferent() {
        val ntt1 = NewsletterNewsletterTopic(1, 2, 1, "foo")
        @Suppress("ReplaceCallWithBinaryOperator")
        ntt1.equals(null) shouldBe false
    }

    @Test
    fun equals_withSameIds_butDifferentSort_isEqual() {
        val ntt1 = NewsletterNewsletterTopic(1, 2, 1, "foo")
        val ntt2 = ntt1.apply { sort = 2 }
        (ntt1 == ntt2) shouldBe true
    }

    @Test
    fun equals_withSameIds_butDifferentTitle_isEqual() {
        val ntt1 = NewsletterNewsletterTopic(1, 2, 1, "foo")
        val ntt2 = ntt1.apply { title = "bar" }
        (ntt1 == ntt2) shouldBe true
    }

    @Test
    fun hashCode_withItself_isEqual() {
        val ntt1 = NewsletterNewsletterTopic(1, 2, 1, "foo")
        ntt1.hashCode() shouldBeEqualTo ntt1.hashCode()
    }

    @Test
    fun hashCode_withSameIds_butDifferentSort_isEqual() {
        val ntt1 = NewsletterNewsletterTopic(1, 2, 1, "foo")
        val ntt2 = NewsletterNewsletterTopic(1, 2, 2, "foo")
        ntt1.hashCode() shouldBeEqualTo ntt2.hashCode()
    }

    @Test
    fun hashCode_withSameIds_butDifferentTitle_isEqual() {
        val ntt1 = NewsletterNewsletterTopic(1, 2, 1, "foo")
        val ntt2 = NewsletterNewsletterTopic(1, 2, 1, "bar")
        ntt1.hashCode() shouldBeEqualTo ntt2.hashCode()
    }

    @Test
    fun equals_withDifferentNewsletterId_isNotEqual() {
        val ntt1 = NewsletterNewsletterTopic(1, 2, 1, "foo")
        val ntt2 = NewsletterNewsletterTopic(3, 2, 1, "foo")
        (ntt1 == ntt2) shouldBe false
    }

    @Test
    fun equals_withDifferentNewsletterTopicId_isNotEqual() {
        val ntt1 = NewsletterNewsletterTopic(1, 2, 1, "foo")
        val ntt2 = NewsletterNewsletterTopic(1, 3, 1, "foo")
        (ntt1 == ntt2) shouldBe false
    }

    @Test
    fun hashCode_withDifferentNewsletterId_differs() {
        val ntt1 = NewsletterNewsletterTopic(1, 2, 1, "foo")
        val ntt2 = NewsletterNewsletterTopic(3, 2, 1, "foo")
        ntt1.hashCode() shouldNotBeEqualTo ntt2.hashCode()
    }

    @Test
    fun hashCode_withDifferentNewsletterTopicId_differs() {
        val ntt1 = NewsletterNewsletterTopic(1, 2, 1, "foo")
        val ntt2 = NewsletterNewsletterTopic(1, 3, 1, "foo")
        ntt1.hashCode() shouldNotBeEqualTo ntt2.hashCode()
    }
}
