package ch.difty.scipamato.core.web.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.fail;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.entity.User;
import ch.difty.scipamato.core.web.CorePageParameters;
import ch.difty.scipamato.core.web.WicketTest;

@SuppressWarnings("SameParameterValue")
class UserEditPageAsViewerTest extends WicketTest {

    @Override
    protected void setUpHook() {
        when(userServiceMock.findById(2)).thenReturn(Optional.of(new User()));
    }

    @Override
    protected String getUserName() {
        return "testviewer";
    }

    @Test
    void userCannotAccessUserEditPageInManagedMode() {
        assertCannotAccessUserEditPageInMode(UserEditPage.Mode.MANAGE);
    }

    @Test
    void userCannotAccessUserEditPageInCreateMode() {
        assertCannotAccessUserEditPageInMode(UserEditPage.Mode.CREATE);
    }

    @Test
    void userCannotAccessUserEditPageInEditMode() {
        assertCannotAccessUserEditPageInMode(UserEditPage.Mode.EDIT);
    }

    @Test
    void userCannotAccessUserEditPageInChangePasswordMode() {
        assertCannotAccessUserEditPageInMode(UserEditPage.Mode.CHANGE_PASSWORD);
    }

    void assertCannotAccessUserEditPageInMode(UserEditPage.Mode mode) {
        try {
            getTester().startPage(newUserEditPageInMode(mode));
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(UnauthorizedInstantiationException.class)
                .hasMessage("Not authorized to instantiate class ch.difty.scipamato.core.web.user.UserEditPage");
        }
    }

    UserEditPage newUserEditPageInMode(final UserEditPage.Mode mode) {
        return newUserEditPageInMode(mode, 1);
    }

    private UserEditPage newUserEditPageInMode(final UserEditPage.Mode mode, final Integer userId) {
        final PageParameters pp = new PageParameters();
        if (userId != null)
            pp.add(CorePageParameters.USER_ID.getName(), userId);
        pp.add(CorePageParameters.MODE.getName(), mode);
        return new UserEditPage(pp);
    }
}
