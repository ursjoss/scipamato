package ch.difty.scipamato.core.web.user

import ch.difty.scipamato.core.auth.Role
import ch.difty.scipamato.core.entity.User
import ch.difty.scipamato.core.persistence.OptimisticLockingException
import ch.difty.scipamato.core.web.CorePageParameters
import ch.difty.scipamato.core.web.common.BasePageTest
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapMultiSelect
import io.mockk.Matcher
import io.mockk.MockKMatcherScope
import io.mockk.clearMocks
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.CheckBox
import org.apache.wicket.markup.html.form.EmailTextField
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.FormComponent
import org.apache.wicket.markup.html.form.PasswordTextField
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.util.Optional

@Suppress("PrivatePropertyName", "SpellCheckingInspection", "SameParameterValue")
internal class UserEditPageTest : BasePageTest<UserEditPage>() {

    private val user = User(
        1, "user", "first", "last", "foo@bar.baz", PW1__HASH, true,
        setOf(Role.ADMIN, Role.USER)
    )
    private val user_saved = User(
        1, "user", "first", "last", "foo@bar.baz", PW2__HASH, true,
        setOf(Role.ADMIN, Role.USER)
    )

    override fun setUpHook() {
        super.setUpHook()
        every { userServiceMock.findById(1) } returns Optional.of(user)
    }

    override val userName: String
        get() = "testadmin"

    @AfterEach
    fun tearDown() {
        confirmVerified(userServiceMock)
    }

    override fun makePage(): UserEditPage = newUserEditPageInMode(UserEditPage.Mode.MANAGE)

    private fun newUserEditPageInMode(mode: UserEditPage.Mode, userId: Int? = 1): UserEditPage {
        val pp = PageParameters()
        userId?.let { pp.add(CorePageParameters.USER_ID.getName(), it) }
        pp.add(CorePageParameters.MODE.getName(), mode)
        return UserEditPage(pp)
    }

    override val pageClass: Class<UserEditPage>
        get() = UserEditPage::class.java

    public override fun assertSpecificComponents() {
        // Validating form in Admin-Mode
        var b = "form"
        tester.assertComponent(b, Form::class.java)
        b += ":"
        assertVisibleTextFieldAndLabel(b + "userName", "user", "User Name", true)
        assertVisibleTextFieldAndLabel(b + "firstName", "first", "First Name", true)
        assertVisibleTextFieldAndLabel(b + "lastName", "last", "Last Name", true)
        assertVisibleEmailFieldAndLabel(b + "email", "foo@bar.baz", "Email", true)
        assertMultiselect(b + "roles", true)
        assertInvisible(b + "rolesString")
        assertVisibleCheckBoxAndLabel(b + "enabled", true, "Enabled", true)
        assertInvisible(b + "currentPassword")
        assertVisiblePasswordFieldAndLabel(b + "password", null, "New Password", true)
        assertVisiblePasswordFieldAndLabel(b + "password2", null, "Confirm Password", true)
        tester.assertComponent(b + "submit", BootstrapButton::class.java)
        tester.assertComponent(b + "delete", BootstrapButton::class.java)
        verify { userServiceMock.findById(1) }
    }

    private fun assertMultiselect(bb: String, visible: Boolean) {
        if (visible) {
            tester.assertComponent(bb, BootstrapMultiSelect::class.java)
            tester.assertEnabled(bb)
            tester.assertLabel(bb + "Label", "Roles")
            tester.assertVisible(bb + "Label")
            tester.assertVisible(bb)
        } else {
            tester.assertInvisible(bb + "Label")
            tester.assertInvisible(bb)
        }
    }

    @Test
    fun assertUserEditPage_inCreateMode() {
        tester.startPage(newUserEditPageInMode(UserEditPage.Mode.CREATE))
        tester.assertRenderedPage(pageClass)
        var b = "form"
        tester.assertComponent(b, Form::class.java)
        b += ":"
        assertVisibleTextFieldAndLabel(b + "userName", null, "User Name", true)
        assertVisibleTextFieldAndLabel(b + "firstName", null, "First Name", true)
        assertVisibleTextFieldAndLabel(b + "lastName", null, "Last Name", true)
        assertVisibleEmailFieldAndLabel(b + "email", null, "Email", true)
        assertMultiselect(b + "roles", true)
        assertInvisible(b + "rolesString")
        assertVisibleCheckBoxAndLabel(b + "enabled", false, "Enabled", true)
        assertInvisible(b + "currentPassword")
        assertVisiblePasswordFieldAndLabel(b + "password", null, "New Password", true)
        assertVisiblePasswordFieldAndLabel(b + "password2", null, "Confirm Password", true)
        tester.assertComponent(b + "submit", BootstrapButton::class.java)
        verify(exactly = 0) { userServiceMock.findById(1) }
        tester.assertNoErrorMessage()
        tester.assertNoInfoMessage()
    }

    @Test
    fun assertUserEditPage_inPasswordChangeMode() {
        tester.startPage(newUserEditPageInMode(UserEditPage.Mode.CHANGE_PASSWORD))
        tester.assertRenderedPage(pageClass)
        var b = "form"
        tester.assertComponent(b, Form::class.java)
        b += ":"
        assertVisibleTextFieldAndLabel(b + "userName", "user", "User Name", false)
        assertVisibleTextFieldAndLabel(b + "firstName", "first", "First Name", false)
        assertVisibleTextFieldAndLabel(b + "lastName", "last", "Last Name", false)
        assertVisibleEmailFieldAndLabel(b + "email", "foo@bar.baz", "Email", false)
        assertMultiselect(b + "roles", false)
        assertVisibleLabelAndLabel(b + "rolesString", "ADMIN, USER", "Roles", true)
        assertVisibleCheckBoxAndLabel(b + "enabled", true, "Enabled", false)
        assertVisiblePasswordFieldAndLabel(b + "currentPassword", null, "Current Password", true)
        assertVisiblePasswordFieldAndLabel(b + "password", null, "New Password", true)
        assertVisiblePasswordFieldAndLabel(b + "password2", null, "Confirm Password", true)
        tester.assertComponent(b + "submit", BootstrapButton::class.java)
        verify { userServiceMock.findById(1) }
        tester.assertNoErrorMessage()
        tester.assertNoInfoMessage()
    }

    @Test
    fun assertUserEditPage_inEditMode() {
        tester.startPage(newUserEditPageInMode(UserEditPage.Mode.EDIT))
        tester.assertRenderedPage(pageClass)
        var b = "form"
        tester.assertComponent(b, Form::class.java)
        b += ":"
        assertVisibleTextFieldAndLabel(b + "userName", "user", "User Name", false)
        assertVisibleTextFieldAndLabel(b + "firstName", "first", "First Name", true)
        assertVisibleTextFieldAndLabel(b + "lastName", "last", "Last Name", true)
        assertVisibleEmailFieldAndLabel(b + "email", "foo@bar.baz", "Email", true)
        assertMultiselect(b + "roles", false)
        assertVisibleLabelAndLabel(b + "rolesString", "ADMIN, USER", "Roles", true)
        assertVisibleCheckBoxAndLabel(b + "enabled", true, "Enabled", false)
        assertInvisible(b + "currentPassword")
        assertInvisible(b + "password")
        assertInvisible(b + "password2")
        tester.assertComponent(b + "submit", BootstrapButton::class.java)
        verify { userServiceMock.findById(1) }
        tester.assertNoErrorMessage()
        tester.assertNoInfoMessage()
    }

    private fun assertVisibleTextFieldAndLabel(bb: String, modelValue: String?, labelText: String, enabled: Boolean) {
        assertVisibleFieldAndLabel(bb, modelValue, labelText, enabled, TextField::class.java)
    }

    private fun assertVisibleFieldAndLabel(
        bb: String,
        modelValue: Any?,
        labelText: String,
        enabled: Boolean,
        clazz: Class<out FormComponent<*>?>
    ) {
        tester.assertLabel(bb + "Label", labelText)
        tester.assertComponent(bb, clazz)
        tester.assertModelValue(bb, modelValue)
        if (enabled) tester.assertEnabled(bb) else tester.assertDisabled(bb)
    }

    private fun assertVisibleEmailFieldAndLabel(bb: String, modelValue: String?, labelText: String, enabled: Boolean) {
        assertVisibleFieldAndLabel(bb, modelValue, labelText, enabled, EmailTextField::class.java)
    }

    private fun assertVisibleCheckBoxAndLabel(bb: String, modelValue: Boolean, labelText: String, enabled: Boolean) {
        assertVisibleFieldAndLabel(bb, modelValue, labelText, enabled, CheckBox::class.java)
    }

    private fun assertVisiblePasswordFieldAndLabel(bb: String, modelValue: String?, labelText: String, enabled: Boolean) {
        assertVisibleFieldAndLabel(bb, modelValue, labelText, enabled, PasswordTextField::class.java)
    }

    private fun assertVisibleLabelAndLabel(bb: String, modelValue: Any, labelText: String, enabled: Boolean) {
        tester.assertLabel(bb + "Label", labelText)
        tester.assertComponent(bb, Label::class.java)
        tester.assertModelValue(bb, modelValue)
        if (enabled) tester.assertEnabled(bb) else tester.assertDisabled(bb)
    }

    private fun assertInvisible(bb: String) {
        tester.assertInvisible(bb)
        tester.assertInvisible(bb + "Label")
    }

    private inline fun <reified T : User> MockKMatcherScope.matchUser(pw: String?): T = match(UserMatcher(pw))

    private data class UserMatcher(val pw: String?) : Matcher<User> {
        override fun match(arg: User?): Boolean = if (pw == null) arg?.password == null else pw == arg?.password
    }

    @Test
    fun submitting_inEditMode_delegatesUserSaveWithoutPasswordToService() {
        every { userServiceMock.saveOrUpdate(matchUser(null)) } returns user_saved

        tester.startPage(newUserEditPageInMode(UserEditPage.Mode.EDIT))
        tester.assertRenderedPage(pageClass)

        val formTester = tester.newFormTester("form")
        formTester.submit("submit")
        tester.assertInfoMessages("Successfully saved User [id 1]: user).")
        tester.assertNoErrorMessage()

        verify { userServiceMock.saveOrUpdate(matchUser(null)) }
        verify { userServiceMock.findById(1) }
    }

    @Test
    fun submitting_inPWChangeMode_withCurrentPasswordCorrectAndTwoMatchingPasswords_delegatesToService() {
        every { userServiceMock.saveOrUpdate(matchUser(PASSWORD2)) } returns user_saved

        tester.startPage(newUserEditPageInMode(UserEditPage.Mode.CHANGE_PASSWORD))
        tester.assertRenderedPage(pageClass)

        val formTester = tester.newFormTester("form")
        formTester.setValue("currentPassword", PASSWORD1)
        formTester.setValue("password", PASSWORD2)
        formTester.setValue("password2", PASSWORD2)
        formTester.submit("submit")
        tester.assertInfoMessages("The password for user user was changed successfully.")
        tester.assertNoErrorMessage()

        verify { userServiceMock.saveOrUpdate(matchUser(PASSWORD2)) }
        verify { userServiceMock.findById(1) }
    }

    @Test
    fun submitting_inPWChangeMode_withCurrentPasswordWrong_fails() {
        tester.startPage(newUserEditPageInMode(UserEditPage.Mode.CHANGE_PASSWORD))
        tester.assertRenderedPage(pageClass)

        val formTester = tester.newFormTester("form")
        formTester.setValue("currentPassword", PASSWORD1 + "X")
        formTester.setValue("password", PASSWORD2)
        formTester.setValue("password2", PASSWORD2)
        formTester.submit("submit")
        tester.assertErrorMessages("The current password is not correct.")

        verify { userServiceMock.findById(1) }
    }

    @Test
    fun submitting_inPWChangeMode_withCurrentPasswordCorrectButNonMatchingNewPasswords_fails() {
        tester.startPage(newUserEditPageInMode(UserEditPage.Mode.CHANGE_PASSWORD))
        tester.assertRenderedPage(pageClass)

        val formTester = tester.newFormTester("form")
        formTester.setValue("currentPassword", PASSWORD1)
        formTester.setValue("password", PASSWORD2)
        formTester.setValue("password2", PASSWORD2 + "X")
        formTester.submit("submit")
        tester.assertErrorMessages("New Password and Confirm Password must be equal.")

        verify { userServiceMock.findById(1) }
    }

    @Test
    fun submitting_inPWChangeMode_withNoCurrentPassword_doesNotSubmitDueToMissingExistingPassword() {
        clearMocks(userServiceMock)
        val user = User(
            1, "user", "first", "last", "foo@bar.baz", null, true,
            setOf(Role.ADMIN, Role.USER)
        )
        every { userServiceMock.findById(1) } returns Optional.of(user)

        tester.startPage(newUserEditPageInMode(UserEditPage.Mode.CHANGE_PASSWORD))
        tester.assertRenderedPage(pageClass)

        val formTester = tester.newFormTester("form")
        formTester.setValue("password", PASSWORD2)
        formTester.setValue("password2", PASSWORD2)
        formTester.submit("submit")
        tester.assertErrorMessages("'Current Password' is required.")

        verify(exactly = 0) { userServiceMock.saveOrUpdate(any()) }
        verify { userServiceMock.findById(1) }
    }

    @Test
    fun submitting_inManageMode_withNoPasswordsSet_delegatesToService() {
        every { userServiceMock.saveOrUpdate(matchUser(null)) } returns user_saved

        tester.startPage(newUserEditPageInMode(UserEditPage.Mode.MANAGE))
        tester.assertRenderedPage(pageClass)

        val formTester = tester.newFormTester("form")
        formTester.submit("submit")
        tester.assertInfoMessages("Successfully saved User [id 1]: user).")
        tester.assertNoErrorMessage()

        verify { userServiceMock.saveOrUpdate(matchUser(null)) }
        verify { userServiceMock.findById(1) }
    }

    @Test
    fun submitting_inManageMode_withPasswordsSetWithMismatch_fails() {
        tester.startPage(newUserEditPageInMode(UserEditPage.Mode.MANAGE))
        tester.assertRenderedPage(pageClass)

        val formTester = tester.newFormTester("form")
        formTester.setValue("password", PASSWORD2)
        formTester.setValue("password2", PASSWORD2 + "X")
        formTester.submit("submit")
        tester.assertNoInfoMessage()
        tester.assertErrorMessages("New Password and Confirm Password must be equal.")

        verify { userServiceMock.findById(1) }
    }

    @Test
    fun submitting_inManageMode_withPasswordsSetIdentically_delegatesToService() {
        every { userServiceMock.saveOrUpdate(matchUser(PASSWORD2)) } returns user_saved

        tester.startPage(newUserEditPageInMode(UserEditPage.Mode.MANAGE))
        tester.assertRenderedPage(pageClass)

        val formTester = tester.newFormTester("form")
        formTester.setValue("password", PASSWORD2)
        formTester.setValue("password2", PASSWORD2)
        formTester.submit("submit")
        tester.assertInfoMessages("Successfully saved User [id 1]: user).")
        tester.assertNoErrorMessage()

        verify { userServiceMock.saveOrUpdate(matchUser(PASSWORD2)) }
        verify { userServiceMock.findById(1) }
    }

    @Test
    fun submitting_inCreateMode_delegatesCreateToService() {
        every { userServiceMock.saveOrUpdate(matchUser(PASSWORD2)) } returns user_saved

        tester.startPage(newUserEditPageInMode(UserEditPage.Mode.CREATE, null))
        tester.assertRenderedPage(pageClass)

        val formTester = tester.newFormTester("form")
        formTester.setValue("userName", user.userName)
        formTester.setValue("firstName", user.firstName)
        formTester.setValue("lastName", user.lastName)
        formTester.setValue("email", user.email)
        formTester.setValue("enabled", user.isEnabled)
        formTester.setValue("password", PASSWORD2)
        formTester.setValue("password2", PASSWORD2)
        formTester.submit("submit")
        tester.assertInfoMessages("Successfully saved User [id 1]: user).")
        tester.assertNoErrorMessage()

        verify { userServiceMock.saveOrUpdate(matchUser(PASSWORD2)) }
        verify(exactly = 0) { userServiceMock.findById(1) }
    }

    @Test
    fun submitting_withCreateServiceReturnNull_issuesErrorMessage() {
        every { userServiceMock.saveOrUpdate(matchUser(PASSWORD2)) } returns null

        tester.startPage(newUserEditPageInMode(UserEditPage.Mode.MANAGE))

        tester.assertRenderedPage(pageClass)
        val formTester = tester.newFormTester("form")
        formTester.setValue("password", PASSWORD2)
        formTester.setValue("password2", PASSWORD2)
        formTester.submit("submit")
        tester.assertNoInfoMessage()
        tester.assertErrorMessages("An unexpected error occurred when trying to save User [id 1]: ")

        verify { userServiceMock.saveOrUpdate(matchUser(null)) }
        verify { userServiceMock.findById(1) }
    }

    @Test
    fun submitting_withCreateServiceThrowingOptimisticLockingException() {
        every { userServiceMock.saveOrUpdate(matchUser(PASSWORD2)) } throws
            OptimisticLockingException("tblName", OptimisticLockingException.Type.UPDATE)

        tester.startPage(newUserEditPageInMode(UserEditPage.Mode.MANAGE))
        tester.assertRenderedPage(pageClass)
        val formTester = tester.newFormTester("form")
        formTester.setValue("password", PASSWORD2)
        formTester.setValue("password2", PASSWORD2)
        formTester.submit("submit")
        tester.assertNoInfoMessage()
        tester.assertErrorMessages(
            "The tblName with id 1 has been modified concurrently by another user. " +
                "Please reload it and apply your changes once more."
        )

        verify { userServiceMock.saveOrUpdate(matchUser(null)) }
        verify { userServiceMock.findById(1) }
    }

    @Test
    fun submitting_withCreateServiceThrowingOtherException() {
        every { userServiceMock.saveOrUpdate(matchUser(PASSWORD2)) } throws
            RuntimeException("otherExceptionMsg")

        tester.startPage(newUserEditPageInMode(UserEditPage.Mode.MANAGE))

        tester.assertRenderedPage(pageClass)
        val formTester = tester.newFormTester("form")
        formTester.setValue("password", PASSWORD2)
        formTester.setValue("password2", PASSWORD2)
        formTester.submit("submit")
        tester.assertNoInfoMessage()
        tester.assertErrorMessages("An unexpected error occurred when trying to save User [id 1]: otherExceptionMsg")

        verify { userServiceMock.saveOrUpdate(matchUser(null)) }
        verify { userServiceMock.findById(1) }
    }

    @Test
    fun deleting_delegatesToService() {
        every { userServiceMock.remove(any()) } returns Unit
        every { userServiceMock.countByFilter(any()) } returns 0

        tester.startPage(newUserEditPageInMode(UserEditPage.Mode.MANAGE))
        tester.assertRenderedPage(pageClass)

        val formTester = tester.newFormTester("form")
        formTester.submit("delete")
        tester.assertRenderedPage(UserListPage::class.java)
        tester.assertNoErrorMessage()

        verify { userServiceMock.findById(1) }
        verify { userServiceMock.remove(user) }
        // by UserListPage
        verify { userServiceMock.countByFilter(any()) }
    }

    @Test
    fun deleting_withExceptionWhileRemoving_delegatesToService() {
        every { userServiceMock.remove(user) } throws RuntimeException("foo")

        tester.startPage(newUserEditPageInMode(UserEditPage.Mode.MANAGE))
        tester.assertRenderedPage(pageClass)

        val formTester = tester.newFormTester("form")
        formTester.submit("delete")
        tester.assertRenderedPage(UserEditPage::class.java)
        tester.assertErrorMessages("There was an unexpected issue while deleting user user: foo")

        verify { userServiceMock.findById(1) }
        verify { userServiceMock.remove(user) }
    }

    @Test
    fun instantiateUserEditPage_withInvalidMode_throws() {
        val pp = PageParameters()
        pp.add(CorePageParameters.MODE.getName(), "foo")
        invoking { UserEditPage(pp) } shouldThrow IllegalArgumentException::class withMessage
            "No enum constant ch.difty.scipamato.core.web.user.UserEditPage.Mode.foo"
    }

    companion object {
        private const val PASSWORD1 = "pw"
        private const val PW1__HASH = "$2a$04$8r4NZRvT24ggS1TfOqov3eEb0bUN6xwx6zdUFz3XANEQl60M.EFDi"
        private const val PASSWORD2 = "pw2"
        private const val PW2__HASH = "$2a$04\$w6dFZqhgYL8tm/P2iNCPMOftTdwlU6aBxNZDaXHpfpn5HdBc7V3Bq"
    }
}
