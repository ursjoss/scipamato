package ch.difty.scipamato.core.entity.keyword;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class KeywordTranslationTest {

    @Test
    void displayValue() {
        KeywordTranslation ntt = new KeywordTranslation(1, "de", "topic1", 1);
        assertThat(ntt.getDisplayValue()).isEqualTo("de: topic1");
    }
}