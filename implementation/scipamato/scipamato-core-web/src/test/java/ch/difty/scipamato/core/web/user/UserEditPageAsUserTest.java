package ch.difty.scipamato.core.web.user;

import org.junit.jupiter.api.Test;

class UserEditPageAsUserTest extends UserEditPageAsViewerTest {

    @Override
    protected String getUserName() {
        return "testuser";
    }

    @Test
    @Override
    void userCannotAccessUserEditPageInManagedMode() {
        assertCannotAccessUserEditPageInMode(UserEditPage.Mode.MANAGE);
    }

    @Test
    @Override
    void userCannotAccessUserEditPageInCreateMode() {
        assertCannotAccessUserEditPageInMode(UserEditPage.Mode.CREATE);
    }

    @Test
    void userCanAccessUserEditPageInEditMode() {
        getTester().startPage(newUserEditPageInMode(UserEditPage.Mode.EDIT));
        getTester().assertRenderedPage(UserEditPage.class);
    }

    @Test
    void userCanAccessUserEditPageInChangePasswordMode() {
        getTester().startPage(newUserEditPageInMode(UserEditPage.Mode.CHANGE_PASSWORD));
        getTester().assertRenderedPage(UserEditPage.class);
    }

    @Test
    @Override
    void userCannotAccessUserEditPageInEditMode() {
        // no-op
    }

    @Test
    @Override
    void userCannotAccessUserEditPageInChangePasswordMode() {
        // no-op
    }

}
