package ch.difty.scipamato.core.entity.newsletter

import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainSame
import org.junit.jupiter.api.Test

internal class NewsletterTopicFilterTest {

    private val f = NewsletterTopicFilter()

    @Test
    fun getAndSet() {
        f.titleMask = "titleMask"
        f.titleMask shouldBeEqualTo "titleMask"
        f.toString() shouldBeEqualTo "NewsletterTopicFilter(titleMask=titleMask)"
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
        NewsletterTopicFilter.NewsletterTopicFilterFields.values().map { it.fieldName } shouldContainSame
            listOf("titleMask")
    }
}
