package ch.difty.scipamato.core.persistence

import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.core.entity.User
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Java6Assertions.assertThat
import org.jooq.DSLContext
import org.junit.jupiter.api.Test
import org.springframework.security.core.Authentication

internal class AbstractRepoTest {

    private lateinit var repo: AbstractRepo

    private val dslContext = mock<DSLContext>()
    private val dateTimeService = mock<DateTimeService>()
    private val authentication = mock<Authentication>()
    private val userMock = mock<User>()

    @Test
    fun gettingActiveUser_withAuthenticationPresent_returnsPrincipalAsUser() {
        repo = object : AbstractRepo(dslContext, dateTimeService) {
            override fun getAuthentication(): Authentication? = this@AbstractRepoTest.authentication
        }
        whenever(authentication.principal).thenReturn(userMock)
        assertThat(repo.activeUser).isEqualTo(userMock)
    }

    @Test
    fun gettingActiveUser_withNoAuthenticationPresent_returnsDummyUser() {
        repo = object : AbstractRepo(dslContext, dateTimeService) {
            override fun getAuthentication(): Authentication? = null
        }
        assertThat(repo.activeUser).isEqualTo(User.NO_USER)
    }
}
