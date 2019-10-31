package ch.difty.scipamato.core.auth;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The different {@link Role}s users can be assigned to in SciPaMaTo.
 * <p>
 * The keys are defined in an external interface, so they can be used for
 * authorization in pages, e.g.:
 * <p>
 * {@literal @AuthorizeInstantiation({ Roles.USER, Roles.ADMIN })}
 *
 * @author u.joss
 */
public enum Role {

    ADMIN(1, Roles.ADMIN, "System Administration"),
    USER(2, Roles.USER, "Main SciPaMaTo Users"),
    VIEWER(3, Roles.VIEWER, "Read-only Viewer");

    private static final Role[] ROLES = values();

    private final Integer id;
    private final String  key;
    private final String  description;

    Role(@NotNull final Integer id, @NotNull final String key, @NotNull final String description) {
        this.id = id;
        this.key = key;
        this.description = description;
    }

    public static Role of(@Nullable final Integer id) {
        if (id == null)
            return null;
        for (final Role r : ROLES)
            if (id.equals(r.getId()))
                return r;
        throw new IllegalArgumentException("No matching type for id " + id);
    }

    @NotNull
    public Integer getId() {
        return id;
    }

    @NotNull
    public String getDescription() {
        return description;
    }

    @NotNull
    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return key;
    }
}
