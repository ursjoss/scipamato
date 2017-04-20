package ch.difty.sipamato.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import ch.difty.sipamato.auth.Role;

/**
 * {@link User} entity. Holds a list of associated {@link Role}s
 *
 * @author u.joss
 */
public class User extends IdSipamatoEntity<Integer> {

    private static final long serialVersionUID = 1L;

    public static final User NO_USER = new User(-1, "noUser", "n.a", "n.a.", "n.a.", "n.a");

    public static final String USER_NAME = "userName";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String ENABLED = "enabled";
    public static final String ROLES = "roles";

    @NotNull
    @Size(max = 30)
    private String userName;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String email;

    @NotNull
    private String password;

    private boolean enabled;

    private final List<Role> roles = new ArrayList<>();

    public User() {
        // default constructor
    }

    public User(final int id, final String userName, final String firstName, final String lastName, final String email, final String password, final boolean enabled, final List<Role> roles) {
        setId(id);
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        setRoles(roles);
    }

    public User(final int id, final String userName, final String firstName, final String lastName, final String email, final String password) {
        this(id, userName, firstName, lastName, email, password, false, null);
    }

    public User(User user) {
        setId(user.getId());
        this.userName = user.getUserName();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.enabled = user.isEnabled();
        setRoles(user.getRoles());
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(final List<Role> roles) {
        this.roles.clear();
        if (roles != null)
            this.roles.addAll(roles);
    }

    public void addRole(final Role role) {
        this.roles.add(role);
    }

    public void removeRole(final Role role) {
        final Iterator<Role> it = this.roles.iterator();
        while (it.hasNext()) {
            final Role r = it.next();
            if (r.equals(role)) {
                it.remove();
                return;
            }
        }
    }

    @Override
    public String getDisplayValue() {
        return userName;
    }

    public String getFullName() {
        final StringBuilder sb = new StringBuilder();
        sb.append(firstName).append(" ").append(lastName);
        return sb.toString();
    }
}
