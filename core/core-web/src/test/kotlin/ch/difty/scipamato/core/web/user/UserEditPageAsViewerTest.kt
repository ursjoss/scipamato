package ch.difty.scipamato.core.web.user

import ch.difty.scipamato.core.entity.User
import ch.difty.scipamato.core.web.CorePageParameters
import ch.difty.scipamato.core.web.WicketTest
import io.mockk.every
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.apache.wicket.authorization.UnauthorizedInstantiationException
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.junit.jupiter.api.Test
import java.util.Optional

@Suppress("SpellCheckingInspection")
internal open class UserEditPageAsViewerTest : WicketTest() {

    override fun setUpHook() {
        every { userServiceMock.findById(2) } returns Optional.of(User())
    }

    override val userName: String
        get() = "testviewer"

    @Test
    open fun userCannotAccessUserEditPageInManagedMode() {
        assertCannotAccessUserEditPageInMode(UserEditPage.Mode.MANAGE)
    }

    @Test
    open fun userCannotAccessUserEditPageInCreateMode() {
        assertCannotAccessUserEditPageInMode(UserEditPage.Mode.CREATE)
    }

    @Test
    open fun userCannotAccessUserEditPageInEditMode() {
        assertCannotAccessUserEditPageInMode(UserEditPage.Mode.EDIT)
    }

    @Test
    open fun userCannotAccessUserEditPageInChangePasswordMode() {
        assertCannotAccessUserEditPageInMode(UserEditPage.Mode.CHANGE_PASSWORD)
    }

    fun assertCannotAccessUserEditPageInMode(mode: UserEditPage.Mode?) {
        invoking { tester.startPage(newUserEditPageInMode(mode)) } shouldThrow
            UnauthorizedInstantiationException::class withMessage
            "Not authorized to instantiate class ch.difty.scipamato.core.web.user.UserEditPage"
    }

    fun newUserEditPageInMode(mode: UserEditPage.Mode?): UserEditPage = newUserEditPageInMode(mode, 1)

    @Suppress("SameParameterValue")
    private fun newUserEditPageInMode(mode: UserEditPage.Mode?, userId: Int?): UserEditPage {
        val pp = PageParameters()
        userId?.let { pp.add(CorePageParameters.USER_ID.getName(), it) }
        pp.add(CorePageParameters.MODE.getName(), mode)
        return UserEditPage(pp)
    }
}
