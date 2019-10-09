package ch.difty.scipamato.core.auth

import ch.difty.scipamato.core.auth.Role.ADMIN
import ch.difty.scipamato.core.auth.Role.USER
import ch.difty.scipamato.core.auth.Role.VIEWER
import ch.difty.scipamato.core.auth.Role.of
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.extractProperty
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.Test

internal class RoleTest {

    @Test
    fun hasAllValues() {
        assertThat(Role.values()).containsExactly(ADMIN, USER, VIEWER)
    }

    @Test
    fun assertIds() {
        assertThat(extractProperty("id").from(Role.values())).containsExactly(1, 2, 3)
    }

    @Test
    fun assertKeys() {
        assertThat(extractProperty("key").from(Role.values())).containsExactly(Roles.ADMIN, Roles.USER, Roles.VIEWER)
    }

    @Test
    fun assertDescriptions() {
        assertThat(Role.values().map { it.description })
            .containsExactly("System Administration", "Main SciPaMaTo Users", "Read-only Viewer")
    }

    @Test
    fun assertToString() {
        assertThat(ADMIN.toString()).isEqualTo("ROLE_ADMIN")
        assertThat(USER.toString()).isEqualTo("ROLE_USER")
        assertThat(VIEWER.toString()).isEqualTo("ROLE_VIEWER")
    }

    @Test
    fun of_withExistingId() {
        assertThat(of(1)).isEqualTo(ADMIN)
    }

    @Test
    fun of_withNotExistingId_throws() {
        try {
            of(0)
            fail<Any>("Should have thrown exception")
        } catch (ex: Exception) {
            assertThat(ex).isInstanceOf(IllegalArgumentException::class.java).hasMessage("No matching type for id 0")
        }
    }

    @Test
    fun of_withNullId_returnsNull() {
        assertThat(of(null)).isNull()
    }
}
