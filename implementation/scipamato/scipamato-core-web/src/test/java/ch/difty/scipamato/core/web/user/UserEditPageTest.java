package ch.difty.scipamato.core.web.user;

import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.junit.After;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.core.auth.Role;
import ch.difty.scipamato.core.entity.User;
import ch.difty.scipamato.core.persistence.UserService;
import ch.difty.scipamato.core.web.CorePageParameters;
import ch.difty.scipamato.core.web.common.BasePageTest;

@SuppressWarnings("ALL")
public class UserEditPageTest extends BasePageTest<UserEditPage> {

    private User user = new User(1, "user", "first", "last", "foo@bar.baz", "pw", true, List.of(Role.ADMIN, Role.USER));

    @MockBean
    private UserService userServiceMock;

    @Override
    protected void setUpHook() {
        super.setUpHook();
        when(userServiceMock.findById(1)).thenReturn(Optional.of(user));
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(userServiceMock);
    }

    @Override
    protected UserEditPage makePage() {
        return newUserEditPageForUserWithId(UserEditPage.Mode.MANAGE);
    }

    private UserEditPage newUserEditPageForUserWithId(final UserEditPage.Mode mode) {
        final PageParameters pp = new PageParameters();
        pp.add(CorePageParameters.USER_ID.getName(), 1);
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
        assertInvisible(b + "rolesString");
        assertVisibleCheckBoxAndLabel(b + "enabled", true, "Enabled", true);
        assertInvisible(b + "currentPassword");
        assertVisiblePasswordFieldAndLabel(b + "password", null, "New Password", true);
        assertVisiblePasswordFieldAndLabel(b + "password2", null, "Confirm Password", true);

        getTester().assertComponent(b + "submit", BootstrapButton.class);

        verify(userServiceMock).findById(1);
    }

    @Test
    public void testUserEditPage_inPasswordChangeMode() {
        getTester().startPage(newUserEditPageForUserWithId(UserEditPage.Mode.CHANGE_PASSWORD));
        getTester().assertRenderedPage(getPageClass());

        String b = "form";
        getTester().assertComponent(b, Form.class);

        b += ":";
        assertVisibleTextFieldAndLabel(b + "userName", "user", "User Name", false);
        assertVisibleTextFieldAndLabel(b + "firstName", "first", "First Name", false);
        assertVisibleTextFieldAndLabel(b + "lastName", "last", "Last Name", false);
        assertVisibleEmailFieldAndLabel(b + "email", "foo@bar.baz", "Email", false);
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
    public void testUserEditPage_inEditMode() {
        getTester().startPage(newUserEditPageForUserWithId(UserEditPage.Mode.EDIT));
        getTester().assertRenderedPage(getPageClass());

        String b = "form";
        getTester().assertComponent(b, Form.class);

        b += ":";
        assertVisibleTextFieldAndLabel(b + "userName", "user", "User Name", false);
        assertVisibleTextFieldAndLabel(b + "firstName", "first", "First Name", true);
        assertVisibleTextFieldAndLabel(b + "lastName", "last", "Last Name", true);
        assertVisibleEmailFieldAndLabel(b + "email", "foo@bar.baz", "Email", true);
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

}