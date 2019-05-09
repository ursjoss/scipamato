package ch.difty.scipamato.core.entity.newsletter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class NewsletterTopicTranslationTest {

    @Test
    void titleIsAiasForName() {
        NewsletterTopicTranslation ntt = new NewsletterTopicTranslation(1, "de", "topic1", 1);
        assertThat(ntt.getTitle()).isEqualTo(ntt.getName());
        ntt.setTitle("foo");
        assertThat(ntt.getName()).isEqualTo("foo");
    }

    @Test
    void displayValue() {
        NewsletterTopicTranslation ntt = new NewsletterTopicTranslation(1, "de", "topic1", 1);
        assertThat(ntt.getDisplayValue()).isEqualTo("de: topic1");
    }
}