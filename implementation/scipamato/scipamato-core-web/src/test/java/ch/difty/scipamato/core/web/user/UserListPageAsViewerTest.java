package ch.difty.scipamato.core.web.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.fail;

import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.junit.Test;

import ch.difty.scipamato.core.web.WicketTest;

public class UserListPageAsViewerTest extends WicketTest {

    @Override
    protected String getUserName() {
        return "testviewer";
    }

    @Test
    public void viewerCannotAccessUserListPage() {
        try {
            getTester().startPage(new UserListPage(null));
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(UnauthorizedInstantiationException.class)
                .hasMessage("Not authorized to instantiate class ch.difty.scipamato.core.web.user.UserListPage");
        }
    }

}
