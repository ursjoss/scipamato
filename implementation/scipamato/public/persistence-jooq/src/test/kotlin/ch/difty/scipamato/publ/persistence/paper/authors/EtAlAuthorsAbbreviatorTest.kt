package ch.difty.scipamato.publ.persistence.paper.authors

import ch.difty.scipamato.publ.config.ApplicationPublicProperties
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class EtAlAuthorsAbbreviatorTest {

    private val maxLength = 16

    private val propertiesMock = mock<ApplicationPublicProperties>()
    private lateinit var abbr16: EtAlAuthorsAbbreviator

    @BeforeEach
    fun setUp() {
        whenever(propertiesMock.authorsAbbreviatedMaxLength).thenReturn(maxLength)
        abbr16 = EtAlAuthorsAbbreviator(propertiesMock)
    }

    private fun assertAbbreviation(abbr: String, expected: String) {
        assertThat(abbr.length).isLessThanOrEqualTo(maxLength)
        assertThat(abbr).isEqualTo(expected)
    }

    @Test
    fun abbreviating_withNullAuthors_returnsBlank() {
        assertThat(abbr16.abbreviate(null)).isBlank()
    }

    @Test
    fun abbreviating_withAuthorsLengthBelowThreshold_returnsFullAuthorsString() {
        val authors = "12345678901."
        assertThat(authors.length).isLessThan(maxLength)

        assertAbbreviation(abbr16.abbreviate(authors), authors)
    }

    @Test
    fun abbreviating_withAuthorsLengthAtThreshold_returnsFullAuthorsString() {
        val authors = "123456789012345."
        assertThat(authors.length).isEqualTo(maxLength)

        assertAbbreviation(abbr16.abbreviate(authors), authors)
    }

    @Test
    fun abbreviating_withSingleAuthorLongerThanThreshold_returnsAbbreviatedAuthorWithEllipsis() {
        val authors = "12345678901234567890."
        assertThat(authors.length).isGreaterThan(maxLength)

        assertAbbreviation(abbr16.abbreviate(authors), "1234567890123...")
    }

    @Test
    fun abbreviating_withMultipleAuthors_returnsFirstAuthorEtAl() {
        val authors = "FooBar F, BarBaz B."
        assertThat(authors.length).isGreaterThan(maxLength)

        assertAbbreviation(abbr16.abbreviate(authors), "FooBar F et al.")
    }

    @Test
    fun abbreviating_withMultipleAuthors_withCommaAtCutoff_returnsTwoAuthorsEtAl() {
        val authors = "F F, A A, Bar B, Baz B."
        val cutoffIndex = maxLength - ET_AL.length - 1
        assertThat(authors[cutoffIndex]).isEqualTo(',')

        assertAbbreviation(abbr16.abbreviate(authors), "F F, A A et al.")
    }

    @Test
    fun abbreviating_withMultipleAuthors_withCommaJustBeyondCutoff_returnsTwoAuthorsEtAl() {
        val authors = "F F, AB A, Bar B, Baz B."
        val justAfterCutoffIndex = maxLength - ET_AL.length
        assertThat(authors[justAfterCutoffIndex]).isEqualTo(',')

        assertAbbreviation(abbr16.abbreviate(authors), "F F, AB A et al.")
    }

    @Test
    fun abbreviating_withMultipleAuthors_withPeriodJustBeyondCutoff_returnsTwoAuthorsEtAl() {
        val authors = "F F, AB A."
        val justAfterCutoffIndex = maxLength - ET_AL.length
        assertThat(authors[justAfterCutoffIndex]).isEqualTo('.')

        assertAbbreviation(abbr16.abbreviate(authors), "F F, AB A.")
    }

    @Test
    fun abbreviating_withMaxLengthZero_doesNotAbbreviate() {
        abbr16 = EtAlAuthorsAbbreviator(propertiesMock)
        whenever(propertiesMock.authorsAbbreviatedMaxLength).thenReturn(0)
        val abbr0 = EtAlAuthorsAbbreviator(propertiesMock)
        val authors = "Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, Foo B, ."

        assertThat(abbr0.abbreviate(authors)).isEqualTo(authors)
    }

    companion object {
        private const val ET_AL = " et al."
    }
}
