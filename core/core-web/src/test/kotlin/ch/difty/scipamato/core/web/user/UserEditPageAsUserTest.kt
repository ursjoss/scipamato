package ch.difty.scipamato.core.web.user

import org.junit.jupiter.api.Test

internal class UserEditPageAsUserTest : UserEditPageAsViewerTest() {

    override val userName: String
        get() = "testuser"

    @Test
    override fun userCannotAccessUserEditPageInManagedMode() {
        assertCannotAccessUserEditPageInMode(UserEditPage.Mode.MANAGE)
    }

    @Test
    override fun userCannotAccessUserEditPageInCreateMode() {
        assertCannotAccessUserEditPageInMode(UserEditPage.Mode.CREATE)
    }

    @Test
    fun userCanAccessUserEditPageInEditMode() {
        tester.startPage(newUserEditPageInMode(UserEditPage.Mode.EDIT))
        tester.assertRenderedPage(UserEditPage::class.java)
    }

    @Test
    fun userCanAccessUserEditPageInChangePasswordMode() {
        tester.startPage(newUserEditPageInMode(UserEditPage.Mode.CHANGE_PASSWORD))
        tester.assertRenderedPage(UserEditPage::class.java)
    }

    @Test
    override fun userCannotAccessUserEditPageInEditMode() {
        // no-op
    }

    @Test
    override fun userCannotAccessUserEditPageInChangePasswordMode() {
        // no-op
    }
}
