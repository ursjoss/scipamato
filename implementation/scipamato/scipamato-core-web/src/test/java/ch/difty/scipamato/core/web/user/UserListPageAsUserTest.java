package ch.difty.scipamato.core.web.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.fail;

import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.junit.Test;

import ch.difty.scipamato.core.web.WicketTest;

public class UserListPageAsUserTest extends UserListPageAsViewerTest {

    @Override
    protected String getUserName() {
        return "testuser";
    }

}
