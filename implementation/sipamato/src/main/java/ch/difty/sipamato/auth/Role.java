package ch.difty.sipamato.auth;

/**
 * The different {@link Role}s users can be assigned to in SiPaMaTo.
 *
 * The keys are defined in an external interface, so they can be used for authorization in pages, e.g.:<p/>
 *
 * {@literal @AuthorizeInstantiation({ Roles.USER, Roles.ADMIN })}
 *
 * @author u.joss
 */
public enum Role {

    ADMIN(1, Roles.ADMIN, "System Administration"),
    USER(2, Roles.USER, "Main Sipamato Users"),
    VIEWER(3, Roles.VIEWER, "Read-only Viewer");

    private final Integer id;
    private final String key;
    private final String description;

    Role(final Integer id, final String key, final String description) {
        this.id = id;
        this.key = key;
        this.description = description;
    }

    public static Role of(final Integer id) {
        if (id == null) {
            return null;
        }
        for (final Role r : Role.values()) {
            if (id.equals(r.getId())) {
                return r;
            }
        }
        throw new IllegalArgumentException("No matching type for id " + id);
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getKey() {
        return key;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return key;
    }
}
