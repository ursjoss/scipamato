package ch.difty.scipamato.core.entity.newsletter

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

internal class NewsletterTopicTranslationTest {

    @Test
    fun titleIsAliasForName() {
        val ntt = NewsletterTopicTranslation(1, "de", "topic1", 1)
        ntt.title shouldBeEqualTo ntt.name
        ntt.title = "foo"
        ntt.name shouldBeEqualTo "foo"
    }

    @Test
    fun displayValue() {
        val ntt = NewsletterTopicTranslation(1, "de", "topic1", 1)
        ntt.displayValue shouldBeEqualTo "de: topic1"
    }
}
