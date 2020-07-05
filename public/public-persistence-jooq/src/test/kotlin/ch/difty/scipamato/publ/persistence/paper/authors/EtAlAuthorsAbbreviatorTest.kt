package ch.difty.scipamato.publ.persistence.paper.authors

import ch.difty.scipamato.publ.config.ApplicationPublicProperties
import io.mockk.every
import io.mockk.mockk
import org.amshove.kluent.shouldBeBlank
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeGreaterThan
import org.amshove.kluent.shouldBeLessOrEqualTo
import org.amshove.kluent.shouldBeLessThan
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class EtAlAuthorsAbbreviatorTest {

    private val maxLength = 16

    private val propertiesMock = mockk<ApplicationPublicProperties>()
    private lateinit var abbr16: EtAlAuthorsAbbreviator

    @BeforeEach
    fun setUp() {
        every { propertiesMock.authorsAbbreviatedMaxLength } returns maxLength
        abbr16 = EtAlAuthorsAbbreviator(propertiesMock)
    }

    private fun assertAbbreviation(abbr: String, expected: String) {
        abbr.length shouldBeLessOrEqualTo maxLength
        abbr shouldBeEqualTo expected
    }

    @Test
    fun abbreviating_withNullAuthors_returnsBlank() {
        abbr16.abbreviate(null).shouldBeBlank()
    }

    @Test
    fun abbreviating_withAuthorsLengthBelowThreshold_returnsFullAuthorsString() {
        val authors = "12345678901."
        authors.length shouldBeLessThan maxLength

        assertAbbreviation(abbr16.abbreviate(authors), authors)
    }

    @Test
    fun abbreviating_withAuthorsLengthAtThreshold_returnsFullAuthorsString() {
        val authors = "123456789012345."
        authors.length shouldBeEqualTo maxLength

        assertAbbreviation(abbr16.abbreviate(authors), authors)
    }

    @Test
    fun abbreviating_withSingleAuthorLongerThanThreshold_returnsAbbreviatedAuthorWithEllipsis() {
        val authors = "12345678901234567890."
        authors.length shouldBeGreaterThan maxLength

        assertAbbreviation(abbr16.abbreviate(authors), "1234567890123...")
    }

    @Test
    fun abbreviating_withMultipleAuthors_returnsFirstAuthorEtAl() {
        val authors = "FooBar F, BarBaz B."
        authors.length shouldBeGreaterThan maxLength

        assertAbbreviation(abbr16.abbreviate(authors), "FooBar F et al.")
    }

    @Test
    fun abbreviating_withMultipleAuthors_withCommaAtCutoff_returnsTwoAuthorsEtAl() {
        val authors = "F F, A A, Bar B, Baz B."
        val cutoffIndex = maxLength - ET_AL.length - 1
        authors[cutoffIndex] shouldBeEqualTo ','

        assertAbbreviation(abbr16.abbreviate(authors), "F F, A A et al.")
    }

    @Test
    fun abbreviating_withMultipleAuthors_withCommaJustBeyondCutoff_returnsTwoAuthorsEtAl() {
        val authors = "F F, AB A, Bar B, Baz B."
        val justAfterCutoffIndex = maxLength - ET_AL.length
        authors[justAfterCutoffIndex] shouldBeEqualTo ','

        assertAbbreviation(abbr16.abbreviate(authors), "F F, AB A et al.")
    }

    @Test
    fun abbreviating_withMultipleAuthors_withPeriodJustBeyondCutoff_returnsTwoAuthorsEtAl() {
        val authors = "F F, AB A."
        val justAfterCutoffIndex = maxLength - ET_AL.length
        authors[justAfterCutoffIndex] shouldBeEqualTo '.'

        assertAbbreviation(abbr16.abbreviate(authors), "F F, AB A.")
    }

    @Test
    fun abbreviating_withMaxLengthZero_doesNotAbbreviate() {
        abbr16 = EtAlAuthorsAbbreviator(propertiesMock)
        every { propertiesMock.authorsAbbreviatedMaxLength } returns 0
        val abbr0 = EtAlAuthorsAbbreviator(propertiesMock)
        val authors = "Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, " +
            "Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, " +
            "Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, ."

        abbr0.abbreviate(authors) shouldBeEqualTo authors
    }

    companion object {
        private const val ET_AL = " et al."
    }
}
