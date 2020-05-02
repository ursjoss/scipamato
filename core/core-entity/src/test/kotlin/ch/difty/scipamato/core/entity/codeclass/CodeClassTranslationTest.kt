package ch.difty.scipamato.core.entity.codeclass

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

internal class CodeClassTranslationTest {

    @Test
    fun comment() {
        val cct = CodeClassTranslation(1, "de", "code1", "description", 1)
        cct.description shouldBeEqualTo "description"
    }

    @Test
    fun displayValue() {
        val cct = CodeClassTranslation(1, "de", "code1", "description", 1)
        cct.displayValue shouldBeEqualTo "de: code1"
    }

    @Test
    fun field() {
        CodeClassTranslation.CodeClassTranslationFields.DESCRIPTION.fieldName shouldBeEqualTo "description"
    }
}
