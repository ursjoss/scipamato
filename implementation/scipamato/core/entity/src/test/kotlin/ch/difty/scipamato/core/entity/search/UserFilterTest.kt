package ch.difty.scipamato.core.entity.search

import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class UserFilterTest {

    @Test
    fun test() {
        val f = UserFilter()
        f.nameMask = "name"
        f.emailMask = "email"
        f.enabled = true

        assertThat(f.nameMask).isEqualTo("name")
        assertThat(f.emailMask).isEqualTo("email")
        assertThat(f.enabled).isEqualTo(true)

        assertThat(f.toString()).isEqualTo("UserFilter(nameMask=name, emailMask=email, enabled=true)")
    }

    @Test
    fun equals() {
        EqualsVerifier
            .forClass(UserFilter::class.java)
            .withRedefinedSuperclass()
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify()
    }

    @Test
    fun assertEnumFields() {
        assertThat(UserFilter.UserFilterFields.values().map { it.fieldName })
            .containsExactly("nameMask", "emailMask", "enabled")
    }
}
