package ch.difty.scipamato.core.web.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ch.difty.scipamato.core.entity.User;
import ch.difty.scipamato.core.persistence.UserService;
import ch.difty.scipamato.core.web.authentication.ScipamatoUserDetails;
import ch.difty.scipamato.core.web.authentication.ScipamatoUserDetailsService;

@SuppressWarnings({ "CatchMayIgnoreException", "ResultOfMethodCallIgnored" })
@ExtendWith(MockitoExtension.class)
class ScipamatoUserDetailsServiceTest {

    private ScipamatoUserDetailsService service;

    @Mock
    private       UserService userServiceMock;
    private final User        user = new User(10, "un", "fn", "ln", "em", "pw");

    @BeforeEach
    void setUp() {
        service = new ScipamatoUserDetailsService(userServiceMock);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(userServiceMock);
    }

    @Test
    void loadUserByUsername_withUserNotFound_throws() {
        String username = "foo";
        when(userServiceMock.findByUserName(username)).thenReturn(Optional.empty());
        try {
            service.loadUserByUsername(username);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("No user found with name " + username);
        }
        verify(userServiceMock).findByUserName(username);
    }

    @Test
    void loadUserByUsername_withUserFound() {
        String username = "bar";
        when(userServiceMock.findByUserName(username)).thenReturn(Optional.of(user));

        UserDetails usd = service.loadUserByUsername(username);

        assertThat(usd)
            .isNotNull()
            .isInstanceOf(ScipamatoUserDetails.class);
        assertThat(usd.getUsername()).isEqualTo("un");
        assertThat(usd.getPassword()).isEqualTo("pw");

        verify(userServiceMock).findByUserName(username);
    }
}
