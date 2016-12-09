package ch.difty.sipamato.persistance.jooq.role;

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

import ch.difty.sipamato.entity.Role;

@RunWith(MockitoJUnitRunner.class)
public class JooqRoleServiceTest {

    private final JooqRoleService service = new JooqRoleService();

    @Mock
    private RoleRepository repoMock;
    @Mock
    private RoleFilter filterMock;
    @Mock
    private Pageable pageableMock;
    @Mock
    private Page<Role> rolePageMock;
    @Mock
    private Role roleMock;

    private final List<Role> roles = new ArrayList<>();

    @Before
    public void setUp() {
        service.setRepository(repoMock);

        roles.add(roleMock);
        roles.add(roleMock);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(repoMock, filterMock, pageableMock, rolePageMock, roleMock);
    }

    @Test
    public void findingById_withFoundEntity_returnsOptionalOfIt() {
        Integer id = 7;
        when(repoMock.findById(id)).thenReturn(roleMock);

        Optional<Role> optRole = service.findById(id);
        assertThat(optRole.isPresent()).isTrue();
        assertThat(optRole.get()).isEqualTo(roleMock);

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
        when(repoMock.findByFilter(filterMock, pageableMock)).thenReturn(rolePageMock);
        when(rolePageMock.getContent()).thenReturn(roles);

        assertThat(service.findByFilter(filterMock, pageableMock)).isEqualTo(roles);

        verify(repoMock).findByFilter(filterMock, pageableMock);
        verify(rolePageMock).getContent();
    }

    @Test
    public void countingByFilter_delegatesToRepo() {
        when(repoMock.countByFilter(filterMock)).thenReturn(3);
        assertThat(service.countByFilter(filterMock)).isEqualTo(3);
        verify(repoMock).countByFilter(filterMock);
    }

    @Test
    public void savingOrUpdating_withRoleWithNullId_hasRepoAddTheRole() {
        when(roleMock.getId()).thenReturn(null);
        when(repoMock.add(roleMock)).thenReturn(roleMock);
        assertThat(service.saveOrUpdate(roleMock)).isEqualTo(roleMock);
        verify(repoMock).add(roleMock);
        verify(roleMock).getId();
    }

    @Test
    public void savingOrUpdating_withRoleWithNonNullId_hasRepoUpdateTheRole() {
        when(roleMock.getId()).thenReturn(17);
        when(repoMock.update(roleMock)).thenReturn(roleMock);
        assertThat(service.saveOrUpdate(roleMock)).isEqualTo(roleMock);
        verify(repoMock).update(roleMock);
        verify(roleMock).getId();
    }

}
