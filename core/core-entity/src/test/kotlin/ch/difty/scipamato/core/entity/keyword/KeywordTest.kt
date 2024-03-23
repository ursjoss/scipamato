package ch.difty.scipamato.core.entity.keyword

import ch.difty.scipamato.core.entity.Jsr303ValidatedEntityTest
import ch.difty.scipamato.core.entity.keyword.Keyword.KeywordFields.NAME
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

internal class KeywordTest : Jsr303ValidatedEntityTest<Keyword>(Keyword::class.java) {

    override fun newValidEntity(): Keyword {
        return Keyword(10, "kw", "so")
    }

    override val toString: String = "Keyword(name=kw, searchOverride=so)"
    override val displayValue: String = newValidEntity().name

    @Test
    fun get() {
        val nt = newValidEntity()
        nt.id shouldBeEqualTo 10
        nt.name shouldBeEqualTo "kw"
        nt.searchOverride shouldBeEqualTo "so"
    }

    @Test
    fun validatingKeyword_withCompleteInformation_succeeds() {
        verifySuccessfulValidation(newValidEntity())
    }

    @Test
    fun validatingNewsletter_withNullIssue_fails() {
        val nt = newValidEntity()
        nt.name = null
        validateAndAssertFailure(nt, NAME, null, "{jakarta.validation.constraints.NotNull.message}")
    }
}
