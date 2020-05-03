package ch.difty.scipamato.core.web.security

import ch.difty.scipamato.core.entity.User
import ch.difty.scipamato.core.persistence.UserService
import ch.difty.scipamato.core.web.authentication.ScipamatoUserDetails
import ch.difty.scipamato.core.web.authentication.ScipamatoUserDetailsService
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.util.Optional

@ExtendWith(MockKExtension::class)
internal class ScipamatoUserDetailsServiceTest {

    private lateinit var service: ScipamatoUserDetailsService

    @MockK
    private lateinit var userServiceMock: UserService

    private val user = User(10, "un", "fn", "ln", "em", "pw")

    @BeforeEach
    fun setUp() {
        service = ScipamatoUserDetailsService(userServiceMock)
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(userServiceMock)
    }

    @Test
    fun loadUserByUsername_withUserNotFound_throws() {
        val username = "foo"
        every { userServiceMock.findByUserName(username) } returns Optional.empty()
        invoking { service.loadUserByUsername(username) } shouldThrow UsernameNotFoundException::class withMessage "No user found with name $username"
        verify { userServiceMock.findByUserName(username) }
    }

    @Test
    fun loadUserByUsername_withUserFound() {
        val username = "bar"
        every { userServiceMock.findByUserName(username) } returns Optional.of(user)
        val usd = service.loadUserByUsername(username)
        usd shouldBeInstanceOf ScipamatoUserDetails::class
        usd.username shouldBeEqualTo "un"
        usd.password shouldBeEqualTo "pw"
        verify { userServiceMock.findByUserName(username) }
    }
}
