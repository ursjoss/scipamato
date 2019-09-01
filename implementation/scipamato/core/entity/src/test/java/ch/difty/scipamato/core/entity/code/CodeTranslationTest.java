package ch.difty.scipamato.core.entity.code;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CodeTranslationTest {

    @Test
    void comment() {
        CodeTranslation ntt = new CodeTranslation(1, "de", "code1", "comment", 1);
        assertThat(ntt.getComment()).isEqualTo("comment");
    }

    @Test
    void displayValue() {
        CodeTranslation ntt = new CodeTranslation(1, "de", "code1", "comment", 1);
        assertThat(ntt.getDisplayValue()).isEqualTo("de: code1");
    }

    @Test
    void field() {
        assertThat(CodeTranslation.CodeTranslationFields.COMMENT.getFieldName()).isEqualTo("comment");
    }
}