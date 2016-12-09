package ch.difty.sipamato.persistance.jooq.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ch.difty.sipamato.entity.User;

@RunWith(MockitoJUnitRunner.class)
public class JooqUserServiceTest {

    private final JooqUserService service = new JooqUserService();

    @Mock
    private UserRepository repoMock;
    @Mock
    private UserFilter filterMock;
    @Mock
    private Pageable pageableMock;
    @Mock
    private Page<User> userPageMock;
    @Mock
    private User userMock;

    private final List<User> users = new ArrayList<>();

    @Before
    public void setUp() {
        service.setRepository(repoMock);

        users.add(userMock);
        users.add(userMock);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(repoMock, filterMock, pageableMock, userPageMock, userMock);
    }

    @Test
    public void findingById_withFoundEntity_returnsOptionalOfIt() {
        Integer id = 7;
        when(repoMock.findById(id)).thenReturn(userMock);

        Optional<User> optUser = service.findById(id);
        assertThat(optUser.isPresent()).isTrue();
        assertThat(optUser.get()).isEqualTo(userMock);

        verify(repoMock).findById(id);
    }

    @Test
    public void findingById_withNotFoundEntity_returnsOptionalEmpty() {
        Integer id = 7;
        when(repoMock.findById(id)).thenReturn(null);

        assertThat(service.findById(id).isPresent()).isFalse();

        verify(repoMock).findById(id);
    }

    @Test
    public void findingByFilter_delegatesToRepo() {
        when(repoMock.findByFilter(filterMock, pageableMock)).thenReturn(userPageMock);
        when(userPageMock.getContent()).thenReturn(users);

        assertThat(service.findByFilter(filterMock, pageableMock)).isEqualTo(users);

        verify(repoMock).findByFilter(filterMock, pageableMock);
        verify(userPageMock).getContent();
    }

    @Test
    public void countingByFilter_delegatesToRepo() {
        when(repoMock.countByFilter(filterMock)).thenReturn(3);
        assertThat(service.countByFilter(filterMock)).isEqualTo(3);
        verify(repoMock).countByFilter(filterMock);
    }

    @Test
    public void savingOrUpdating_withUserWithNullId_hasRepoAddTheUser() {
        when(userMock.getId()).thenReturn(null);
        when(repoMock.add(userMock)).thenReturn(userMock);
        assertThat(service.saveOrUpdate(userMock)).isEqualTo(userMock);
        verify(repoMock).add(userMock);
        verify(userMock).getId();
    }

    @Test
    public void savingOrUpdating_withUserWithNonNullId_hasRepoUpdateTheUser() {
        when(userMock.getId()).thenReturn(17);
        when(repoMock.update(userMock)).thenReturn(userMock);
        assertThat(service.saveOrUpdate(userMock)).isEqualTo(userMock);
        verify(repoMock).update(userMock);
        verify(userMock).getId();
    }

}
