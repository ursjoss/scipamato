package ch.difty.scipamato.core.web.security;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ch.difty.scipamato.core.entity.User;
import ch.difty.scipamato.core.persistence.UserService;

@RunWith(MockitoJUnitRunner.class)
public class ScipamatoUserDetailServiceTest {

    private ScipamatoUserDetailService service;

    @Mock
    private UserService userServiceMock;
    private User        user = new User(10, "un", "fn", "ln", "em", "pw");

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
        assertDegenerateSupplierParameter(() -> service.loadUserByUsername(null), "username");
    }

    @Test
    public void loadUserByUsername_withUserNotFound_throws() {
        String username = "foo";
        when(userServiceMock.findByUserName(username)).thenReturn(Optional.empty());
        try {
            service.loadUserByUsername(username);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("No user found with name " + username);
        }
        verify(userServiceMock).findByUserName(username);
    }

    @Test
    public void loadUserByUsername_withUserFound() {
        String username = "bar";
        when(userServiceMock.findByUserName(username)).thenReturn(Optional.of(user));

        UserDetails usd = service.loadUserByUsername(username);

        assertThat(usd).isNotNull()
            .isInstanceOf(ScipamatoUserDetails.class);
        assertThat(usd.getUsername()).isEqualTo("un");
        assertThat(usd.getPassword()).isEqualTo("pw");

        verify(userServiceMock).findByUserName(username);
    }
}
