package ch.difty.scipamato.core.web.user;

import static org.mockito.Mockito.*;

import java.util.Optional;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.junit.After;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.core.entity.User;
import ch.difty.scipamato.core.persistence.UserService;
import ch.difty.scipamato.core.web.CorePageParameters;
import ch.difty.scipamato.core.web.common.BasePageTest;

public class UserEditPageTest extends BasePageTest<UserEditPage> {

    private User user = new User(1, "user", "first", "last", "foo@bar.baz", "pw");

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
        final PageParameters pp = new PageParameters();
        pp.add(CorePageParameters.USER_ID.getName(), Integer.valueOf(1));
        return new UserEditPage(pp);
    }

    @Override
    protected Class<UserEditPage> getPageClass() {
        return UserEditPage.class;
    }

    @Override
    public void assertSpecificComponents() {
        String b = "form";
        getTester().assertComponent(b, Form.class);

        b += ":";
        String bb = b + "userName";
        getTester().assertLabel(bb + "Label", "User Name");
        getTester().assertComponent(bb, TextField.class);
        getTester().assertModelValue(bb, "user");

        bb = b + "firstName";
        getTester().assertLabel(bb + "Label", "First Name");
        getTester().assertComponent(bb, TextField.class);
        getTester().assertModelValue(bb, "first");

        bb = b + "lastName";
        getTester().assertLabel(bb + "Label", "Last Name");
        getTester().assertComponent(bb, TextField.class);
        getTester().assertModelValue(bb, "last");

        bb = b + "email";
        getTester().assertLabel(bb + "Label", "Email");
        getTester().assertComponent(bb, EmailTextField.class);
        getTester().assertModelValue(bb, "foo@bar.baz");

        bb = b + "password";
        getTester().assertLabel(bb + "Label", "Password");
        getTester().assertComponent(bb, PasswordTextField.class);
        getTester().assertModelValue(bb, null);

        bb = b + "password2";
        getTester().assertLabel(bb + "Label", "Repeat Password");
        getTester().assertComponent(bb, PasswordTextField.class);
        getTester().assertModelValue(bb, null);

        getTester().assertComponent(b + "submit", BootstrapButton.class);

        verify(userServiceMock).findById(1);
    }
}