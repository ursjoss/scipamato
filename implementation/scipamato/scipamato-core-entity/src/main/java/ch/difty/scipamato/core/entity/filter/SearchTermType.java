package ch.difty.scipamato.core.entity.filter;

/**
 * The supported types of search terms.
 *
 * @author u.joss
 */
public enum SearchTermType {
    BOOLEAN(0),
    INTEGER(1),
    STRING(2),
    AUDIT(3);

    private final int id;

    SearchTermType(final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static SearchTermType byId(int id) {
        for (final SearchTermType t : values()) {
            if (t.getId() == id) {
                return t;
            }
        }
        throw new IllegalArgumentException("id " + id + " is not supported");
    }
}