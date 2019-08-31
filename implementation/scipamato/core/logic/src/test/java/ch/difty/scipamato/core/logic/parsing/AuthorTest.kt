package ch.difty.scipamato.core.logic.parsing

import nl.jqno.equalsverifier.EqualsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

private const val AUTHOR_STRING = "authorString"
private const val LAST_NAME = "lastName"
private const val FIRST_NAME = "firstName"

internal class AuthorTest {

    @Test
    fun canGetValues() {
        val a = Author(AUTHOR_STRING, LAST_NAME, FIRST_NAME)
        assertThat(a.authorString).isEqualTo(AUTHOR_STRING)
        assertThat(a.lastName).isEqualTo(LAST_NAME)
        assertThat(a.firstName).isEqualTo(FIRST_NAME)
    }

    @Test
    fun equals() {
        EqualsVerifier.forClass(Author::class.java).verify()
    }

}
