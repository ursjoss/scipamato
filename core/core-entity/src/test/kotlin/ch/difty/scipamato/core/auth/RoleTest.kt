package ch.difty.scipamato.core.auth

import ch.difty.scipamato.core.auth.Role.ADMIN
import ch.difty.scipamato.core.auth.Role.USER
import ch.difty.scipamato.core.auth.Role.VIEWER
import ch.difty.scipamato.core.auth.Role.of
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.junit.jupiter.api.Test

internal class RoleTest {

    @Test
    fun hasAllValues() {
        Role.entries shouldContainAll listOf(ADMIN, USER, VIEWER)
    }

    @Test
    fun assertIds() {
        Role.entries.map { it.id } shouldContainAll listOf(1, 2, 3)
    }

    @Test
    fun assertKeys() {
        Role.entries.map { it.key } shouldContainAll listOf(Roles.ADMIN, Roles.USER, Roles.VIEWER)
    }

    @Test
    fun assertDescriptions() {
        Role.entries.map { it.description } shouldContainSame
            listOf("System Administration", "Main SciPaMaTo Users", "Read-only Viewer")
    }

    @Test
    fun assertToString() {
        ADMIN.toString() shouldBeEqualTo "ROLE_ADMIN"
        USER.toString() shouldBeEqualTo "ROLE_USER"
        VIEWER.toString() shouldBeEqualTo "ROLE_VIEWER"
    }

    @Test
    fun of_withExistingId() {
        of(1) shouldBeEqualTo ADMIN
    }

    @Test
    fun of_withNotExistingId_throws() {
        invoking { of(0) } shouldThrow IllegalArgumentException::class withMessage "No matching type for id 0"
    }

    @Test
    fun of_withNullId_returnsNull() {
        of(null).shouldBeNull()
    }
}
