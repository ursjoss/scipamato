package ch.difty.scipamato.core.persistence;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.jooq.DSLContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;

import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.core.entity.User;

@RunWith(MockitoJUnitRunner.class)
public class AbstractRepoTest {

    private AbstractRepo repo;

    @Mock
    private DSLContext dslContext;

    @Mock
    private DateTimeService dateTimeService;

    @Mock
    private Authentication authentication;

    @Mock
    private User userMock;

    @Before
    public void setUp() {
    }

    @Test
    public void gettingActiveUser_withAuthenticationPresent_returnsPrincipalAsUser() {
        repo = new AbstractRepo(dslContext, dateTimeService) {
            @Override
            Authentication getAuthentication() {
                return authentication;
            }
        };
        when(authentication.getPrincipal()).thenReturn(userMock);
        assertThat(repo.getActiveUser()).isEqualTo(userMock);
    }

    @Test
    public void gettingActiveUser_withNoAuthenticationPresent_returnsDummyUser() {
        repo = new AbstractRepo(dslContext, dateTimeService) {
            @Override
            Authentication getAuthentication() {
                return null;
            }
        };
        assertThat(repo.getActiveUser()).isEqualTo(User.NO_USER);
    }
}