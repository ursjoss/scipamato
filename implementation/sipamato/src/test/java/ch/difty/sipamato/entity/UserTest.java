package ch.difty.sipamato.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class UserTest {

    private static final int ID = 1;
    private static final String USER_NAME = "username";
    private static final String FIRST_NAME = "firstname";
    private static final String LAST_NAME = "lastname";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final boolean ENABLED = true;

    final User user = new User(ID, USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD);

    private final List<Role> roles = new ArrayList<>();

    @Mock
    private Role roleMock1, roleMock2, roleMock3;

    @Before
    public void setUp() {
        roles.addAll(Arrays.asList(roleMock1, roleMock2));
    }

    @Test
    public void setAndGet() {
        User u = new User();
        u.setId(ID);
        u.setUserName(USER_NAME);
        u.setFirstName(FIRST_NAME);
        u.setLastName(LAST_NAME);
        u.setEmail(EMAIL);
        u.setPassword(PASSWORD);
        u.setEnabled(ENABLED);
        u.setRoles(roles);

        assertUser(u);
    }

    private void assertUser(User u) {
        assertThat(u.getId()).isEqualTo(1);
        assertThat(u.getUserName()).isEqualTo(USER_NAME);
        assertThat(u.getFirstName()).isEqualTo(FIRST_NAME);
        assertThat(u.getLastName()).isEqualTo(LAST_NAME);
        assertThat(u.getEmail()).isEqualTo(EMAIL);
        assertThat(u.getPassword()).isEqualTo(PASSWORD);
        assertThat(u.isEnabled()).isEqualTo(ENABLED);
        assertThat(u.getRoles()).containsExactly(roleMock1, roleMock2);
    }

    @Test
    public void constructingByUser() {
        final User u = new User(ID, USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ENABLED, roles);
        final User u1 = new User(u);
        assertUser(u1);
    }

    @Test
    public void displayValue_isEqualToName() {
        final User u = new User(ID, USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ENABLED, roles);
        assertThat(u.getDisplayValue()).isEqualTo(USER_NAME);
    }

    @Test
    public void reducedConstructor_leavesSomeFieldsDefault() {
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getUserName()).isEqualTo(USER_NAME);
        assertThat(user.getFirstName()).isEqualTo(FIRST_NAME);
        assertThat(user.getLastName()).isEqualTo(LAST_NAME);
        assertThat(user.getEmail()).isEqualTo(EMAIL);
        assertThat(user.getPassword()).isEqualTo(PASSWORD);

        assertThat(user.isEnabled()).isFalse();
        assertThat(user.getRoles()).isEmpty();
    }

    @Test
    public void settingRoles_reSetsRoles() {
        final User u = new User(ID, USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ENABLED, roles);
        assertThat(u.getRoles()).containsExactly(roleMock1, roleMock2);

        u.setRoles(Arrays.asList(roleMock3, roleMock1));

        assertThat(u.getRoles()).containsExactly(roleMock3, roleMock1);
    }

    @Test
    public void settingRole_withNullList_clearsList() {
        final User u = new User(ID, USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ENABLED, roles);
        u.setRoles(null);
        assertThat(u.getRoles()).isEmpty();
    }

    @Test
    public void settingRole_withBlankList_clearsList() {
        final User u = new User(ID, USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ENABLED, roles);
        u.setRoles(new ArrayList<>());
        assertThat(u.getRoles()).isEmpty();
    }

    @Test
    public void addingNullRole_addsNothing() {
        user.addRole(roleMock1);
        assertThat(user.getRoles()).containsExactly(roleMock1);
    }

}
