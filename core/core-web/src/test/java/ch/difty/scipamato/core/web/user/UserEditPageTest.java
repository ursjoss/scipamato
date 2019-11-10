package ch.difty.scipamato.core.web.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.Set;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapMultiSelect;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;

import ch.difty.scipamato.core.auth.Role;
import ch.difty.scipamato.core.entity.User;
import ch.difty.scipamato.core.entity.search.UserFilter;
import ch.difty.scipamato.core.persistence.OptimisticLockingException;
import ch.difty.scipamato.core.web.CorePageParameters;
import ch.difty.scipamato.core.web.common.BasePageTest;

@SuppressWarnings({ "SpellCheckingInspection", "SameParameterValue", "ResultOfMethodCallIgnored",
    "CatchMayIgnoreException" })
class UserEditPageTest extends BasePageTest<UserEditPage> {

    private static final String PASSWORD1 = "pw";
    private static final String PW1__HASH = "$2a$04$8r4NZRvT24ggS1TfOqov3eEb0bUN6xwx6zdUFz3XANEQl60M.EFDi";

    private static final String PASSWORD2 = "pw2";
    private static final String PW2__HASH = "$2a$04$w6dFZqhgYL8tm/P2iNCPMOftTdwlU6aBxNZDaXHpfpn5HdBc7V3Bq";

    private final User user = new User(1, "user", "first", "last", "foo@bar.baz", PW1__HASH, true,
        Set.of(Role.ADMIN, Role.USER));

    private final User user_saved = new User(1, "user", "first", "last", "foo@bar.baz", PW2__HASH, true,
        Set.of(Role.ADMIN, Role.USER));

    @Override
    protected void setUpHook() {
        super.setUpHook();
        when(userServiceMock.findById(1)).thenReturn(Optional.of(user));
    }

    @Override
    protected String getUserName() {
        return "testadmin";
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(userServiceMock);
    }

    @Override
    protected UserEditPage makePage() {
        return newUserEditPageInMode(UserEditPage.Mode.MANAGE);
    }

    private UserEditPage newUserEditPageInMode(final UserEditPage.Mode mode) {
        return newUserEditPageInMode(mode, 1);
    }

    private UserEditPage newUserEditPageInMode(final UserEditPage.Mode mode, final Integer userId) {
        final PageParameters pp = new PageParameters();
        if (userId != null)
            pp.add(CorePageParameters.USER_ID.getName(), userId);
        pp.add(CorePageParameters.MODE.getName(), mode);
        return new UserEditPage(pp);
    }

    @Override
    protected Class<UserEditPage> getPageClass() {
        return UserEditPage.class;
    }

    @Override
    public void assertSpecificComponents() {
        // Validating form in Admin-Mode
        String b = "form";
        getTester().assertComponent(b, Form.class);

        b += ":";
        assertVisibleTextFieldAndLabel(b + "userName", "user", "User Name", true);
        assertVisibleTextFieldAndLabel(b + "firstName", "first", "First Name", true);
        assertVisibleTextFieldAndLabel(b + "lastName", "last", "Last Name", true);
        assertVisibleEmailFieldAndLabel(b + "email", "foo@bar.baz", "Email", true);
        assertMultiselect(b + "roles", true);
        assertInvisible(b + "rolesString");
        assertVisibleCheckBoxAndLabel(b + "enabled", true, "Enabled", true);
        assertInvisible(b + "currentPassword");
        assertVisiblePasswordFieldAndLabel(b + "password", null, "New Password", true);
        assertVisiblePasswordFieldAndLabel(b + "password2", null, "Confirm Password", true);

        getTester().assertComponent(b + "submit", BootstrapButton.class);
        getTester().assertComponent(b + "delete", BootstrapButton.class);

        verify(userServiceMock).findById(1);
    }

    private void assertMultiselect(final String bb, boolean visible) {
        if (visible) {
            getTester().assertComponent(bb, BootstrapMultiSelect.class);
            getTester().assertEnabled(bb);
            getTester().assertLabel(bb + "Label", "Roles");
            getTester().assertVisible(bb + "Label");
            getTester().assertVisible(bb);
        } else {
            getTester().assertInvisible(bb + "Label");
            getTester().assertInvisible(bb);
        }
    }

    @Test
    void assertUserEditPage_inCreateMode() {
        getTester().startPage(newUserEditPageInMode(UserEditPage.Mode.CREATE));
        getTester().assertRenderedPage(getPageClass());

        String b = "form";
        getTester().assertComponent(b, Form.class);

        b += ":";
        assertVisibleTextFieldAndLabel(b + "userName", null, "User Name", true);
        assertVisibleTextFieldAndLabel(b + "firstName", null, "First Name", true);
        assertVisibleTextFieldAndLabel(b + "lastName", null, "Last Name", true);
        assertVisibleEmailFieldAndLabel(b + "email", null, "Email", true);
        assertMultiselect(b + "roles", true);
        assertInvisible(b + "rolesString");
        assertVisibleCheckBoxAndLabel(b + "enabled", false, "Enabled", true);
        assertInvisible(b + "currentPassword");
        assertVisiblePasswordFieldAndLabel(b + "password", null, "New Password", true);
        assertVisiblePasswordFieldAndLabel(b + "password2", null, "Confirm Password", true);

        getTester().assertComponent(b + "submit", BootstrapButton.class);

        verify(userServiceMock, never()).findById(1);

        getTester().assertNoErrorMessage();
        getTester().assertNoInfoMessage();
    }

    @Test
    void assertUserEditPage_inPasswordChangeMode() {
        getTester().startPage(newUserEditPageInMode(UserEditPage.Mode.CHANGE_PASSWORD));
        getTester().assertRenderedPage(getPageClass());

        String b = "form";
        getTester().assertComponent(b, Form.class);

        b += ":";
        assertVisibleTextFieldAndLabel(b + "userName", "user", "User Name", false);
        assertVisibleTextFieldAndLabel(b + "firstName", "first", "First Name", false);
        assertVisibleTextFieldAndLabel(b + "lastName", "last", "Last Name", false);
        assertVisibleEmailFieldAndLabel(b + "email", "foo@bar.baz", "Email", false);
        assertMultiselect(b + "roles", false);
        assertVisibleLabelAndLabel(b + "rolesString", "ADMIN, USER", "Roles", true);
        assertVisibleCheckBoxAndLabel(b + "enabled", true, "Enabled", false);
        assertVisiblePasswordFieldAndLabel(b + "currentPassword", null, "Current Password", true);
        assertVisiblePasswordFieldAndLabel(b + "password", null, "New Password", true);
        assertVisiblePasswordFieldAndLabel(b + "password2", null, "Confirm Password", true);

        getTester().assertComponent(b + "submit", BootstrapButton.class);

        verify(userServiceMock).findById(1);

        getTester().assertNoErrorMessage();
        getTester().assertNoInfoMessage();
    }

    @Test
    void assertUserEditPage_inEditMode() {
        getTester().startPage(newUserEditPageInMode(UserEditPage.Mode.EDIT));
        getTester().assertRenderedPage(getPageClass());

        String b = "form";
        getTester().assertComponent(b, Form.class);

        b += ":";
        assertVisibleTextFieldAndLabel(b + "userName", "user", "User Name", false);
        assertVisibleTextFieldAndLabel(b + "firstName", "first", "First Name", true);
        assertVisibleTextFieldAndLabel(b + "lastName", "last", "Last Name", true);
        assertVisibleEmailFieldAndLabel(b + "email", "foo@bar.baz", "Email", true);
        assertMultiselect(b + "roles", false);
        assertVisibleLabelAndLabel(b + "rolesString", "ADMIN, USER", "Roles", true);
        assertVisibleCheckBoxAndLabel(b + "enabled", true, "Enabled", false);
        assertInvisible(b + "currentPassword");
        assertInvisible(b + "password");
        assertInvisible(b + "password2");

        getTester().assertComponent(b + "submit", BootstrapButton.class);

        verify(userServiceMock).findById(1);

        getTester().assertNoErrorMessage();
        getTester().assertNoInfoMessage();
    }

    private void assertVisibleTextFieldAndLabel(final String bb, final String modelValue, final String labelText,
        final boolean enabled) {
        assertVisibleFieldAndLabel(bb, modelValue, labelText, enabled, TextField.class);
    }

    private void assertVisibleFieldAndLabel(final String bb, final Object modelValue, final String labelText,
        final boolean enabled, final Class<? extends FormComponent> clazz) {
        getTester().assertLabel(bb + "Label", labelText);
        getTester().assertComponent(bb, clazz);
        getTester().assertModelValue(bb, modelValue);
        if (enabled)
            getTester().assertEnabled(bb);
        else
            getTester().assertDisabled(bb);
    }

    private void assertVisibleEmailFieldAndLabel(final String bb, final String modelValue, final String labelText,
        final boolean enabled) {
        assertVisibleFieldAndLabel(bb, modelValue, labelText, enabled, EmailTextField.class);
    }

    private void assertVisibleCheckBoxAndLabel(final String bb, final boolean modelValue, final String labelText,
        final boolean enabled) {
        assertVisibleFieldAndLabel(bb, modelValue, labelText, enabled, CheckBox.class);
    }

    private void assertVisiblePasswordFieldAndLabel(final String bb, final String modelValue, final String labelText,
        final boolean enabled) {
        assertVisibleFieldAndLabel(bb, modelValue, labelText, enabled, PasswordTextField.class);
    }

    private void assertVisibleLabelAndLabel(final String bb, final Object modelValue, final String labelText,
        final boolean enabled) {
        getTester().assertLabel(bb + "Label", labelText);
        getTester().assertComponent(bb, Label.class);
        getTester().assertModelValue(bb, modelValue);
        if (enabled)
            getTester().assertEnabled(bb);
        else
            getTester().assertDisabled(bb);
    }

    private void assertInvisible(final String bb) {
        getTester().assertInvisible(bb);
        getTester().assertInvisible(bb + "Label");
    }

    private static class UserMatcher implements ArgumentMatcher<User> {

        private final String pw;

        UserMatcher(final String password) {
            this.pw = password;
        }

        @Override
        public boolean matches(final User u) {
            return pw == null ? (u.getPassword() == null) : pw.equals(u.getPassword());
        }
    }

    @Test
    void submitting_inEditMode_delegatesUserSaveWithoutPasswordToService() {
        when(userServiceMock.saveOrUpdate(argThat(new UserMatcher(null)))).thenReturn(user_saved);

        getTester().startPage(newUserEditPageInMode(UserEditPage.Mode.EDIT));
        getTester().assertRenderedPage(getPageClass());

        FormTester formTester = getTester().newFormTester("form");
        formTester.submit("submit");

        getTester().assertInfoMessages("Successfully saved User [id 1]: user).");
        getTester().assertNoErrorMessage();

        verify(userServiceMock).saveOrUpdate(argThat(new UserMatcher(null)));
        verify(userServiceMock).findById(1);
    }

    @Test
    void submitting_inPWChangeMode_withCurrentPasswordCorrectAndTwoMatchingPasswords_delegatesToService() {
        when(userServiceMock.saveOrUpdate(argThat(new UserMatcher(PASSWORD2)))).thenReturn(user_saved);

        getTester().startPage(newUserEditPageInMode(UserEditPage.Mode.CHANGE_PASSWORD));
        getTester().assertRenderedPage(getPageClass());

        FormTester formTester = getTester().newFormTester("form");
        formTester.setValue("currentPassword", PASSWORD1);
        formTester.setValue("password", PASSWORD2);
        formTester.setValue("password2", PASSWORD2);
        formTester.submit("submit");

        getTester().assertInfoMessages("The password for user user was changed successfully.");
        getTester().assertNoErrorMessage();

        verify(userServiceMock).saveOrUpdate(argThat(new UserMatcher(PASSWORD2)));
        verify(userServiceMock).findById(1);
    }

    @Test
    void submitting_inPWChangeMode_withCurrentPasswordWrong_fails() {
        getTester().startPage(newUserEditPageInMode(UserEditPage.Mode.CHANGE_PASSWORD));
        getTester().assertRenderedPage(getPageClass());

        FormTester formTester = getTester().newFormTester("form");
        formTester.setValue("currentPassword", PASSWORD1 + "X");
        formTester.setValue("password", PASSWORD2);
        formTester.setValue("password2", PASSWORD2);
        formTester.submit("submit");

        getTester().assertErrorMessages("The current password is not correct.");

        verify(userServiceMock).findById(1);
    }

    @Test
    void submitting_inPWChangeMode_withCurrentPasswordCorrectButNonMatchingNewPasswords_fails() {
        getTester().startPage(newUserEditPageInMode(UserEditPage.Mode.CHANGE_PASSWORD));
        getTester().assertRenderedPage(getPageClass());

        FormTester formTester = getTester().newFormTester("form");
        formTester.setValue("currentPassword", PASSWORD1);
        formTester.setValue("password", PASSWORD2);
        formTester.setValue("password2", PASSWORD2 + "X");
        formTester.submit("submit");

        getTester().assertErrorMessages("New Password and Confirm Password must be equal.");

        verify(userServiceMock).findById(1);
    }

    @Test
    void submitting_inPWChangeMode_withNoCurrentPassword_doesNotSubmitDueToMissingExistingPassword() {
        reset(userServiceMock);
        final User user = new User(1, "user", "first", "last", "foo@bar.baz", null, true,
            Set.of(Role.ADMIN, Role.USER));
        when(userServiceMock.findById(1)).thenReturn(Optional.of(user));

        getTester().startPage(newUserEditPageInMode(UserEditPage.Mode.CHANGE_PASSWORD));
        getTester().assertRenderedPage(getPageClass());

        FormTester formTester = getTester().newFormTester("form");
        formTester.setValue("password", PASSWORD2);
        formTester.setValue("password2", PASSWORD2);
        formTester.submit("submit");

        getTester().assertErrorMessages("'Current Password' is required.");

        verify(userServiceMock, never()).saveOrUpdate(any());
        verify(userServiceMock).findById(1);
    }

    @Test
    void submitting_inManageMode_withNoPasswordsSet_delegatesToService() {
        when(userServiceMock.saveOrUpdate(argThat(new UserMatcher(null)))).thenReturn(user_saved);

        getTester().startPage(newUserEditPageInMode(UserEditPage.Mode.MANAGE));
        getTester().assertRenderedPage(getPageClass());

        FormTester formTester = getTester().newFormTester("form");
        formTester.submit("submit");

        getTester().assertInfoMessages("Successfully saved User [id 1]: user).");
        getTester().assertNoErrorMessage();

        verify(userServiceMock).saveOrUpdate(argThat(new UserMatcher(null)));
        verify(userServiceMock).findById(1);
    }

    @Test
    void submitting_inManageMode_withPasswordsSetWithMismatch_fails() {
        getTester().startPage(newUserEditPageInMode(UserEditPage.Mode.MANAGE));
        getTester().assertRenderedPage(getPageClass());

        FormTester formTester = getTester().newFormTester("form");
        formTester.setValue("password", PASSWORD2);
        formTester.setValue("password2", PASSWORD2 + "X");
        formTester.submit("submit");

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages("New Password and Confirm Password must be equal.");

        verify(userServiceMock).findById(1);
    }

    @Test
    void submitting_inManageMode_withPasswordsSetIdentically_delegatesToService() {
        when(userServiceMock.saveOrUpdate(argThat(new UserMatcher(PASSWORD2)))).thenReturn(user_saved);

        getTester().startPage(newUserEditPageInMode(UserEditPage.Mode.MANAGE));
        getTester().assertRenderedPage(getPageClass());

        FormTester formTester = getTester().newFormTester("form");
        formTester.setValue("password", PASSWORD2);
        formTester.setValue("password2", PASSWORD2);
        formTester.submit("submit");

        getTester().assertInfoMessages("Successfully saved User [id 1]: user).");
        getTester().assertNoErrorMessage();

        verify(userServiceMock).saveOrUpdate(argThat(new UserMatcher(PASSWORD2)));
        verify(userServiceMock).findById(1);
    }

    @Test
    void submitting_inCreateMode_delegatesCreateToService() {
        when(userServiceMock.saveOrUpdate(argThat(new UserMatcher(PASSWORD2)))).thenReturn(user_saved);

        getTester().startPage(newUserEditPageInMode(UserEditPage.Mode.CREATE, null));
        getTester().assertRenderedPage(getPageClass());

        FormTester formTester = getTester().newFormTester("form");
        formTester.setValue("userName", user.getUserName());
        formTester.setValue("firstName", user.getFirstName());
        formTester.setValue("lastName", user.getLastName());
        formTester.setValue("email", user.getEmail());
        formTester.setValue("enabled", user.isEnabled());
        formTester.setValue("password", PASSWORD2);
        formTester.setValue("password2", PASSWORD2);
        formTester.submit("submit");

        getTester().assertInfoMessages("Successfully saved User [id 1]: user).");
        getTester().assertNoErrorMessage();

        verify(userServiceMock).saveOrUpdate(argThat(new UserMatcher(PASSWORD2)));
        verify(userServiceMock, never()).findById(1);
    }

    @Test
    void submitting_withCreateServiceReturnNull_issuesErrorMessage() {
        when(userServiceMock.saveOrUpdate(argThat(new UserMatcher(PASSWORD2)))).thenReturn(null);

        getTester().startPage(newUserEditPageInMode(UserEditPage.Mode.MANAGE));
        getTester().assertRenderedPage(getPageClass());

        FormTester formTester = getTester().newFormTester("form");
        formTester.setValue("password", PASSWORD2);
        formTester.setValue("password2", PASSWORD2);
        formTester.submit("submit");

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages("An unexpected error occurred when trying to save User [id 1]: ");

        verify(userServiceMock).saveOrUpdate(argThat(new UserMatcher(null)));
        verify(userServiceMock).findById(1);
    }

    @Test
    void submitting_withCreateServiceThrowingOptimisticLockingException() {
        when(userServiceMock.saveOrUpdate(argThat(new UserMatcher(PASSWORD2)))).thenThrow(
            new OptimisticLockingException("tblName", OptimisticLockingException.Type.UPDATE));

        getTester().startPage(newUserEditPageInMode(UserEditPage.Mode.MANAGE));
        getTester().assertRenderedPage(getPageClass());

        FormTester formTester = getTester().newFormTester("form");
        formTester.setValue("password", PASSWORD2);
        formTester.setValue("password2", PASSWORD2);
        formTester.submit("submit");

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages(
            "The tblName with id 1 has been modified concurrently by another user. Please reload it and apply your changes once more.");

        verify(userServiceMock).saveOrUpdate(argThat(new UserMatcher(null)));
        verify(userServiceMock).findById(1);
    }

    @Test
    void submitting_withCreateServiceThrowingOtherException() {
        when(userServiceMock.saveOrUpdate(argThat(new UserMatcher(PASSWORD2)))).thenThrow(
            new RuntimeException("otherExceptionMsg"));

        getTester().startPage(newUserEditPageInMode(UserEditPage.Mode.MANAGE));
        getTester().assertRenderedPage(getPageClass());

        FormTester formTester = getTester().newFormTester("form");
        formTester.setValue("password", PASSWORD2);
        formTester.setValue("password2", PASSWORD2);
        formTester.submit("submit");

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages(
            "An unexpected error occurred when trying to save User [id 1]: otherExceptionMsg");

        verify(userServiceMock).saveOrUpdate(argThat(new UserMatcher(null)));
        verify(userServiceMock).findById(1);
    }

    @Test
    void deleting_delegatesToService() {
        getTester().startPage(newUserEditPageInMode(UserEditPage.Mode.MANAGE));
        getTester().assertRenderedPage(getPageClass());

        FormTester formTester = getTester().newFormTester("form");
        formTester.submit("delete");

        getTester().assertRenderedPage(UserListPage.class);

        getTester().assertNoErrorMessage();

        verify(userServiceMock).findById(1);
        verify(userServiceMock).remove(user);
        // by UserListPage
        verify(userServiceMock).countByFilter(isA(UserFilter.class));
    }

    @Test
    void deleting_withExceptionWhileremoving_delegatesToService() {
        doThrow(new RuntimeException("foo"))
            .when(userServiceMock)
            .remove(user);

        getTester().startPage(newUserEditPageInMode(UserEditPage.Mode.MANAGE));
        getTester().assertRenderedPage(getPageClass());

        FormTester formTester = getTester().newFormTester("form");
        formTester.submit("delete");

        getTester().assertRenderedPage(UserEditPage.class);

        getTester().assertErrorMessages("There was an unexpected issue while deleting user user: foo");

        verify(userServiceMock).findById(1);
        verify(userServiceMock).remove(user);
    }

    @Test
    void instantiateUserEditPage_withInvalidMode_throws() {
        final PageParameters pp = new PageParameters();
        pp.add(CorePageParameters.MODE.getName(), "foo");
        try {
            new UserEditPage(pp);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No enum constant ch.difty.scipamato.core.web.user.UserEditPage.Mode.foo");
        }
    }
}
