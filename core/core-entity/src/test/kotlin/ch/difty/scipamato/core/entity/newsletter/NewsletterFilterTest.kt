package ch.difty.scipamato.core.entity.newsletter

import ch.difty.scipamato.common.entity.newsletter.PublicationStatus
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainSame
import org.junit.jupiter.api.Test

internal class NewsletterFilterTest {

    private val f = NewsletterFilter()

    @Test
    fun getAndSet() {
        f.issueMask = "issueMask"
        f.publicationStatus = PublicationStatus.CANCELLED
        f.newsletterTopic = NewsletterTopic(1, "foo")

        f.issueMask shouldBeEqualTo "issueMask"
        f.publicationStatus shouldBeEqualTo PublicationStatus.CANCELLED
        f.newsletterTopic.id shouldBeEqualTo 1

        f.toString() shouldBeEqualTo
            "NewsletterFilter(issueMask=issueMask, publicationStatus=CANCELLED," +
            " newsletterTopic=NewsletterTopic(title=foo))"
    }

    @Test
    fun equals() {
        EqualsVerifier
            .forClass(NewsletterFilter::class.java)
            .withRedefinedSuperclass()
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify()
    }

    @Test
    fun assertEnumFields() {
        NewsletterFilter.NewsletterFilterFields.values().map { it.fieldName } shouldContainSame
            listOf("issueMask", "publicationStatus", "newsletterTopic")
    }
}
