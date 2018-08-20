package ch.difty.scipamato.core.entity.newsletter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.scipamato.common.TestUtils;

public class NewsletterTopicTranslationTest {

    @Test
    public void degenerateConstruction() {
        TestUtils.assertDegenerateSupplierParameter(() -> new NewsletterTopicTranslation(1, null, "title", 1),
            "langCode");
    }

    @Test
    public void displayValue() {
        NewsletterTopicTranslation ntt = new NewsletterTopicTranslation(1, "de", "topic1", 1);
        assertThat(ntt.getDisplayValue()).isEqualTo("de: topic1");
    }
}