package ch.difty.scipamato.core.entity.newsletter

import ch.difty.scipamato.common.entity.newsletter.PublicationStatus
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class NewsletterFilterTest {

    private val f = NewsletterFilter()

    @Test
    fun getAndSet() {
        f.issueMask = "issueMask"
        f.publicationStatus = PublicationStatus.CANCELLED
        f.newsletterTopic = NewsletterTopic(1, "foo")

        assertThat(f.issueMask).isEqualTo("issueMask")
        assertThat(f.publicationStatus).isEqualTo(PublicationStatus.CANCELLED)
        assertThat(f.newsletterTopic.id).isEqualTo(1)

        assertThat(f.toString()).isEqualTo(
            "NewsletterFilter(issueMask=issueMask, publicationStatus=CANCELLED," +
                " newsletterTopic=NewsletterTopic(title=foo))"
        )
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
        assertThat(NewsletterFilter.NewsletterFilterFields.values().map { it.fieldName })
            .containsExactly("issueMask", "publicationStatus", "newsletterTopic")
    }
}
