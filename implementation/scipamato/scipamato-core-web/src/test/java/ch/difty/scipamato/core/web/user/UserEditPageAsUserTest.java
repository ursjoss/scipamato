package ch.difty.scipamato.core.web.user;

import org.junit.Test;

public class UserEditPageAsUserTest extends UserEditPageAsViewerTest {

    @Override
    protected String getUserName() {
        return "testuser";
    }

    @Test
    @Override
    public void userCannotAccessUserEditPageInManagedMode() {
        assertCannotAccessUserEditPageInMode(UserEditPage.Mode.MANAGE);
    }

    @Test
    @Override
    public void userCannotAccessUserEditPageInCreateMode() {
        assertCannotAccessUserEditPageInMode(UserEditPage.Mode.CREATE);
    }

    @Test
    public void userCanAccessUserEditPageInEditMode() {
        getTester().startPage(newUserEditPageInMode(UserEditPage.Mode.EDIT));
        getTester().assertRenderedPage(UserEditPage.class);
    }

    @Test
    public void userCanAccessUserEditPageInChangePasswordMode() {
        getTester().startPage(newUserEditPageInMode(UserEditPage.Mode.CHANGE_PASSWORD));
        getTester().assertRenderedPage(UserEditPage.class);
    }

    @Test
    @Override
    public void userCannotAccessUserEditPageInEditMode() {
        // no-op
    }

    @Test
    @Override
    public void userCannotAccessUserEditPageInChangePasswordMode() {
        // no-op
    }

}
