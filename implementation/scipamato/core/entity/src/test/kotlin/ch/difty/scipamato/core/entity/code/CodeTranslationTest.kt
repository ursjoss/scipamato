package ch.difty.scipamato.core.entity.code

import org.assertj.core.api.Assertions.assertThat

import org.junit.jupiter.api.Test

internal class CodeTranslationTest {

    @Test
    fun comment() {
        val ntt = CodeTranslation(1, "de", "code1", "comment", 1)
        assertThat(ntt.comment).isEqualTo("comment")
    }

    @Test
    fun displayValue() {
        val ntt = CodeTranslation(1, "de", "code1", "comment", 1)
        assertThat(ntt.displayValue).isEqualTo("de: code1")
    }

    @Test
    fun field() {
        assertThat(CodeTranslation.CodeTranslationFields.COMMENT.fieldName).isEqualTo("comment")
    }
}