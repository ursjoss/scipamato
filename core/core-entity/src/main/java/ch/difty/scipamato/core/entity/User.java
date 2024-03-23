package ch.difty.scipamato.core.entity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.entity.FieldEnumType;
import ch.difty.scipamato.core.auth.Role;

/**
 * {@link User} entity. Holds a list of associated {@link Role}s
 *
 * @author u.joss
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends IdScipamatoEntity<Integer> {

    private static final long serialVersionUID = 1L;

    public static final User NO_USER = new User(-1, "noUser", "n.a", "n.a.", "n.a.", "n.a");

    @jakarta.validation.constraints.NotNull
    @jakarta.validation.constraints.Size(max = 30)
    private String userName;

    @jakarta.validation.constraints.NotNull
    private String firstName;

    @jakarta.validation.constraints.NotNull
    private String lastName;

    @jakarta.validation.constraints.NotNull
    private String email;

    private String password;

    private boolean enabled;

    private final Set<Role> roles = new HashSet<>();

    public enum UserFields implements FieldEnumType {
        USER_NAME("userName"),
        FIRST_NAME("firstName"),
        LAST_NAME("lastName"),
        EMAIL("email"),
        PASSWORD("password"),
        ENABLED("enabled"),
        ROLES("roles");

        private final String name;

        UserFields(final String name) {
            this.name = name;
        }

        @NotNull
        @Override
        public String getFieldName() {
            return name;
        }
    }

    public User(final int id, @NotNull final String userName, @NotNull final String firstName,
        @NotNull final String lastName, @NotNull final String email, @Nullable final String password,
        final boolean enabled, @Nullable final Set<Role> roles) {
        setId(id);
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        setRoles(roles);
    }

    public User(final int id, @NotNull final String userName, @NotNull final String firstName,
        @NotNull final String lastName, @NotNull final String email, @Nullable final String password) {
        this(id, userName, firstName, lastName, email, password, false, null);
    }

    public User(@NotNull final User user) {
        setId(user.getId());
        this.userName = user.getUserName();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.enabled = user.isEnabled();
        setRoles(user.getRoles());
    }

    public void setRoles(@Nullable final Set<Role> roles) {
        this.roles.clear();
        if (roles != null)
            this.roles.addAll(roles);
    }

    public void addRole(@NotNull final Role role) {
        this.roles.add(role);
    }

    public void removeRole(@NotNull final Role role) {
        final Iterator<Role> it = this.roles.iterator();
        while (it.hasNext()) {
            final Role r = it.next();
            if (Objects.equals(r, role)) {
                it.remove();
                return;
            }
        }
    }

    @NotNull
    @Override
    public String getDisplayValue() {
        return userName;
    }

    @NotNull
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
