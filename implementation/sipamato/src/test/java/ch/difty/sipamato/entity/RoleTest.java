package ch.difty.sipamato.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class RoleTest {

    private static final int ID = 1;
    private static final String NAME = "name";
    private static final String COMMENT = "comment";

    final Role role = new Role(ID, NAME);

    private final List<User> users = new ArrayList<>();

    @Mock
    private User userMock1, userMock2, userMock3;

    @Before
    public void setUp() {
        users.addAll(Arrays.asList(userMock1, userMock2));
    }

    @Test
    public void setAndGet() {
        Role r = new Role();
        r.setId(ID);
        r.setName(NAME);
        r.setComment(COMMENT);
        r.setUsers(users);

        assertThat(r.getId()).isEqualTo(1);
        assertThat(r.getName()).isEqualTo(NAME);
        assertThat(r.getComment()).isEqualTo(COMMENT);
        assertThat(r.getUsers()).containsExactly(userMock1, userMock2);
    }

    @Test
    public void displayValue_isEqualToName() {
        assertThat(role.getDisplayValue()).isEqualTo(NAME);
    }

    @Test
    public void reducedConstructor_leavesSomeFieldsDefault() {
        assertThat(role.getId()).isEqualTo(1);
        assertThat(role.getName()).isEqualTo(NAME);

        assertThat(role.getComment()).isNull();
        assertThat(role.getUsers()).isEmpty();
    }

    @Test
    public void settingUser_reSetsUsers() {
        final Role r = new Role(ID, NAME, COMMENT, users);
        assertThat(r.getUsers()).containsExactly(userMock1, userMock2);

        r.setUsers(Arrays.asList(userMock3, userMock1));

        assertThat(r.getUsers()).containsExactly(userMock3, userMock1);
    }

    @Test
    public void settingUser_withNullList_clearsList() {
        final Role r = new Role(ID, NAME, COMMENT, users);
        r.setUsers(null);
        assertThat(r.getUsers()).isEmpty();
    }

    @Test
    public void settingUser_withEmptyList_clearsList() {
        final Role r = new Role(ID, NAME, COMMENT, users);
        r.setUsers(new ArrayList<>());
        assertThat(r.getUsers()).isEmpty();
    }

    @Test
    public void addingUser_addsIt() {
        role.addUser(userMock1);
        assertThat(role.getUsers()).containsExactly(userMock1);
    }

}
