package ch.difty.scipamato.core.entity.code_class;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CodeClassTranslationTest {

    @Test
    void comment() {
        CodeClassTranslation cct = new CodeClassTranslation(1, "de", "code1", "description", 1);
        assertThat(cct.getDescription()).isEqualTo("description");
    }

    @Test
    void displayValue() {
        CodeClassTranslation cct = new CodeClassTranslation(1, "de", "code1", "description", 1);
        assertThat(cct.getDisplayValue()).isEqualTo("de: code1");
    }

    @Test
    void field() {
        assertThat(CodeClassTranslation.CodeClassTranslationFields.DESCRIPTION.getFieldName()).isEqualTo("description");
    }
}