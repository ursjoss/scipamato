package ch.difty.sipamato.entity;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * {@link Role} entity. Holds a list of associated {@link User}s.
 *
 * @author u.joss
 */
public class Role extends IdSipamatoEntity<Integer> {

    private static final long serialVersionUID = 1L;

    public static final String NAME = "name";
    public static final String COMMENT = "comment";
    public static final String USERS = "users";

    @NotNull
    @Size(max = 45)
    private String name;

    private String comment;

    private final List<User> users = new ArrayList<>();

    public Role() {
    }

    public Role(final Integer id, final String name, final String comment, final List<User> users) {
        setId(id);
        this.name = name;
        this.comment = comment;
        setUsers(users);
    }

    public Role(final Integer id, final String name) {
        this(id, name, null, null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(final List<User> users) {
        this.users.clear();
        if (users != null)
            this.users.addAll(users);
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    @Override
    public String getDisplayValue() {
        return name;
    }

}
