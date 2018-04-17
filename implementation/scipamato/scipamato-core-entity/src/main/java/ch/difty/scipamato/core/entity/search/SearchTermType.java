package ch.difty.scipamato.core.entity.search;

/**
 * The supported types of search terms.
 *
 * @author u.joss
 */
public enum SearchTermType {
    BOOLEAN(0),
    INTEGER(1),
    STRING(2),
    AUDIT(3),
    // Dummy that helps testing business logic that throws an exception with
    // undefined values (handled in the default case of a switch).
    UNSUPPORTED(-1);

    // cache the array
    private static final SearchTermType[] SEARCH_TERM_TYPES = values();

    private final int id;

    SearchTermType(final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static SearchTermType byId(final int id) {
        for (final SearchTermType t : SEARCH_TERM_TYPES)
            if (id > UNSUPPORTED.id && t.getId() == id) return t;
        throw new IllegalArgumentException("id " + id + " is not supported");
    }
}