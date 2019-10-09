package ch.difty.scipamato.core.entity.newsletter

import org.assertj.core.api.Assertions.assertThat

import org.junit.jupiter.api.Test

internal class NewsletterTopicTranslationTest {

    @Test
    fun titleIsAiasForName() {
        val ntt = NewsletterTopicTranslation(1, "de", "topic1", 1)
        assertThat(ntt.title).isEqualTo(ntt.name)
        ntt.title = "foo"
        assertThat(ntt.name).isEqualTo("foo")
    }

    @Test
    fun displayValue() {
        val ntt = NewsletterTopicTranslation(1, "de", "topic1", 1)
        assertThat(ntt.displayValue).isEqualTo("de: topic1")
    }
}
