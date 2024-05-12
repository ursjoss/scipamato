package ch.difty.scipamato.core.entity.newsletter

import nl.jqno.equalsverifier.EqualsVerifier
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
        EqualsVerifier.simple()
            .forClass(NewsletterTopicFilter::class.java)
            .withRedefinedSuperclass()
            .verify()
    }

    @Test
    fun assertEnumFields() {
        NewsletterTopicFilter.NewsletterTopicFilterFields.entries.map { it.fieldName } shouldContainSame
            listOf("titleMask")
    }
}
