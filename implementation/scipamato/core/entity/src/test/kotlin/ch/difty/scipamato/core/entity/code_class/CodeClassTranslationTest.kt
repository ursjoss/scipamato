package ch.difty.scipamato.core.entity.code_class

import org.assertj.core.api.Assertions.assertThat

import org.junit.jupiter.api.Test

internal class CodeClassTranslationTest {

    @Test
    fun comment() {
        val cct = CodeClassTranslation(1, "de", "code1", "description", 1)
        assertThat(cct.description).isEqualTo("description")
    }

    @Test
    fun displayValue() {
        val cct = CodeClassTranslation(1, "de", "code1", "description", 1)
        assertThat(cct.displayValue).isEqualTo("de: code1")
    }

    @Test
    fun field() {
        assertThat(CodeClassTranslation.CodeClassTranslationFields.DESCRIPTION.fieldName).isEqualTo("description")
    }
}