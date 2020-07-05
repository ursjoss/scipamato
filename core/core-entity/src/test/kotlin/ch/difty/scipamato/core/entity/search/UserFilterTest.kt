package ch.difty.scipamato.core.entity.search

import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainSame
import org.junit.jupiter.api.Test

internal class UserFilterTest {

    @Test
    fun test() {
        val f = UserFilter()
        f.nameMask = "name"
        f.emailMask = "email"
        f.enabled = true

        f.nameMask shouldBeEqualTo "name"
        f.emailMask shouldBeEqualTo "email"
        f.enabled shouldBeEqualTo true

        f.toString() shouldBeEqualTo "UserFilter(nameMask=name, emailMask=email, enabled=true)"
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
        UserFilter.UserFilterFields.values().map { it.fieldName } shouldContainSame
            listOf("nameMask", "emailMask", "enabled")
    }
}
