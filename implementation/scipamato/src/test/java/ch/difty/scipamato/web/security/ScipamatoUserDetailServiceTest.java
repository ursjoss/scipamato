package ch.difty.scipamato.web.security;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ch.difty.scipamato.entity.User;
import ch.difty.scipamato.lib.NullArgumentException;
import ch.difty.scipamato.service.UserService;

@RunWith(MockitoJUnitRunner.class)
public class ScipamatoUserDetailServiceTest {

    private ScipamatoUserDetailService service;

    @Mock
    private UserService userServiceMock;
    private User user = new User(10, "un", "fn", "ln", "em", "pw");

    @Before
    public void setUp() {
        service = new ScipamatoUserDetailService(userServiceMock);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(userServiceMock);
    }

    @Test
    public void degenerateCallWithNullUserName_throws() {
        try {
            service.loadUserByUsername(null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("username must not be null.");
        }
    }

    @Test
    public void loadUserByUsername_withUserNotFound_throws() {
        String username = "foo";
        when(userServiceMock.findByUserName(username)).thenReturn(Optional.empty());
        try {
            service.loadUserByUsername(username);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(UsernameNotFoundException.class).hasMessage("No user found with name " + username);
        }
        verify(userServiceMock).findByUserName(username);
    }

    @Test
    public void loadUserByUsername_withUserFound() {
        String username = "bar";
        when(userServiceMock.findByUserName(username)).thenReturn(Optional.of(user));

        UserDetails usd = service.loadUserByUsername(username);

        assertThat(usd).isNotNull().isInstanceOf(ScipamatoUserDetails.class);
        assertThat(usd.getUsername()).isEqualTo("un");
        assertThat(usd.getPassword()).isEqualTo("pw");

        verify(userServiceMock).findByUserName(username);
    }
}
