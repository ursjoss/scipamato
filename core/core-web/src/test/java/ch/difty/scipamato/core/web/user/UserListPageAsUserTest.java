package ch.difty.scipamato.core.web.user;

class UserListPageAsUserTest extends UserListPageAsViewerTest {

    @Override
    protected String getUserName() {
        return "testuser";
    }
}
