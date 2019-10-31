package ch.difty.scipamato.core.entity.newsletter

import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class NewsletterTopicFilterTest {

    private val f = NewsletterTopicFilter()

    @Test
    fun getAndSet() {
        f.titleMask = "titleMask"
        assertThat(f.titleMask).isEqualTo("titleMask")
        assertThat(f.toString()).isEqualTo("NewsletterTopicFilter(titleMask=titleMask)")
    }

    @Test
    fun equals() {
        EqualsVerifier
            .forClass(NewsletterTopicFilter::class.java)
            .withRedefinedSuperclass()
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify()
    }

    @Test
    fun assertEnumFields() {
        assertThat(NewsletterTopicFilter.NewsletterTopicFilterFields.values().map { it.fieldName })
            .containsExactly("titleMask")
    }
}
