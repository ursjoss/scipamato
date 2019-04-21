package ch.difty.scipamato.core.entity.code_class;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class CodeClassTranslationTest {

    @Test
    public void comment() {
        CodeClassTranslation cct = new CodeClassTranslation(1, "de", "code1", "description", 1);
        assertThat(cct.getDescription()).isEqualTo("description");
    }

    @Test
    public void displayValue() {
        CodeClassTranslation cct = new CodeClassTranslation(1, "de", "code1", "description", 1);
        assertThat(cct.getDisplayValue()).isEqualTo("de: code1");
    }

    @Test
    public void field() {
        assertThat(CodeClassTranslation.CodeClassTranslationFields.DESCRIPTION.getName()).isEqualTo("description");
    }
}