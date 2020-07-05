package ch.difty.scipamato.core.persistence

import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.core.entity.User
import io.mockk.every
import io.mockk.mockk
import org.amshove.kluent.shouldBeEqualTo
import org.jooq.DSLContext
import org.junit.jupiter.api.Test
import org.springframework.security.core.Authentication

internal class AbstractRepoTest {

    private lateinit var repo: AbstractRepo

    private val dslContext = mockk<DSLContext>()
    private val dateTimeService = mockk<DateTimeService>()
    private val authentication = mockk<Authentication>()
    private val userMock = mockk<User>()

    @Test
    fun gettingActiveUser_withAuthenticationPresent_returnsPrincipalAsUser() {
        repo = object : AbstractRepo(dslContext, dateTimeService) {
            override fun getAuthentication(): Authentication? = this@AbstractRepoTest.authentication
        }
        every { authentication.principal } returns userMock
        repo.activeUser shouldBeEqualTo userMock
    }

    @Test
    fun gettingActiveUser_withNoAuthenticationPresent_returnsDummyUser() {
        repo = object : AbstractRepo(dslContext, dateTimeService) {
            override fun getAuthentication(): Authentication? = null
        }
        repo.activeUser shouldBeEqualTo User.NO_USER
    }
}
