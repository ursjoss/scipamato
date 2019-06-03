package ch.difty.scipamato.core.persistence;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.core.entity.User;

@ExtendWith(MockitoExtension.class)
class AbstractRepoTest {

    private AbstractRepo repo;

    @Mock
    private DSLContext dslContext;

    @Mock
    private DateTimeService dateTimeService;

    @Mock
    private Authentication authentication;

    @Mock
    private User userMock;

    @BeforeEach
    void setUp() {
    }

    @Test
    void gettingActiveUser_withAuthenticationPresent_returnsPrincipalAsUser() {
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
    void gettingActiveUser_withNoAuthenticationPresent_returnsDummyUser() {
        repo = new AbstractRepo(dslContext, dateTimeService) {
            @Override
            Authentication getAuthentication() {
                return null;
            }
        };
        assertThat(repo.getActiveUser()).isEqualTo(User.NO_USER);
    }
}