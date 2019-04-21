package ch.difty.scipamato.core.entity.keyword;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import ch.difty.scipamato.common.TestUtils;

public class KeywordTranslationTest {

    @Test
    public void displayValue() {
        KeywordTranslation ntt = new KeywordTranslation(1, "de", "topic1", 1);
        assertThat(ntt.getDisplayValue()).isEqualTo("de: topic1");
    }
}