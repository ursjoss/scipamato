package ch.difty.scipamato.core.entity.code

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

internal class CodeTranslationTest {

    @Test
    fun comment() {
        val ntt = CodeTranslation(1, "de", "code1", "comment", 1)
        ntt.comment shouldBeEqualTo "comment"
    }

    @Test
    fun displayValue() {
        val ntt = CodeTranslation(1, "de", "code1", "comment", 1)
        ntt.displayValue shouldBeEqualTo "de: code1"
    }

    @Test
    fun field() {
        CodeTranslation.CodeTranslationFields.COMMENT.fieldName shouldBeEqualTo "comment"
    }
}
