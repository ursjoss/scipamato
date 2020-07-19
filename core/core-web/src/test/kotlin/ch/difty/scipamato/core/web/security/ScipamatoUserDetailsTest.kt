package ch.difty.scipamato.core.web.security

import ch.difty.scipamato.core.auth.Role
import ch.difty.scipamato.core.entity.User
import ch.difty.scipamato.core.web.authentication.ScipamatoUserDetails
import nl.jqno.equalsverifier.EqualsVerifier
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldEndWith
import org.amshove.kluent.shouldStartWith
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class ScipamatoUserDetailsTest {

    @Test
    fun test() {
        val user = User(
            1, "un", "fn", "ln", "em", "pw", true,
            setOf(Role.ADMIN, Role.USER)
        )
        val sud = ScipamatoUserDetails(user)
        sud.id shouldBeEqualTo 1
        sud.userName shouldBeEqualTo "un"
        sud.firstName shouldBeEqualTo "fn"
        sud.lastName shouldBeEqualTo "ln"
        sud.email shouldBeEqualTo "em"
        sud.password shouldBeEqualTo "pw"
        sud.isEnabled shouldBeEqualTo true
        sud.roles.map { it.key } shouldContainSame listOf("ROLE_ADMIN", "ROLE_USER")
        sud.authorities.map { it.authority } shouldContainSame listOf("ROLE_ADMIN", "ROLE_USER")
        sud.username shouldBeEqualTo "un"

        // statically set
        sud.isAccountNonExpired.shouldBeTrue()
        sud.isAccountNonLocked.shouldBeTrue()
        sud.isCredentialsNonExpired.shouldBeTrue()
    }

    @Test
    @Disabled("TODO")
    fun testToString() {
        val user = User(
            1, "un", "fn", "ln", "em", "pw", true,
            setOf(Role.ADMIN, Role.USER)
        )
        val sud = ScipamatoUserDetails(user)
        val ts = sud.toString()
        ts shouldContainAll listOf("ROLE_ADMIN", "ROLE_USER")
        ts shouldStartWith "ScipamatoUserDetails[roles=[ROLE_"
        ts shouldContain "],userName=un,firstName=fn,lastName=ln,email=em,password=pw,enabled=true,roles=[ROLE_"
        ts shouldEndWith "],id=1,createdBy=<null>,lastModifiedBy=<null>,created=<null>,lastModified=<null>,version=0]"
    }

    @Test
    fun equals() {
        EqualsVerifier.simple()
            .forClass(User::class.java)
            .withRedefinedSuperclass()
            .usingGetClass()
            .withIgnoredFields("created", "createdBy", "lastModified", "lastModifiedBy")
            .verify()
    }
}
