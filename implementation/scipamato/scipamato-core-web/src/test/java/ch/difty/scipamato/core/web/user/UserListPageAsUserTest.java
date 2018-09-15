package ch.difty.scipamato.core.web.user;

public class UserListPageAsUserTest extends UserListPageAsViewerTest {

    @Override
    protected String getUserName() {
        return "testuser";
    }

}
