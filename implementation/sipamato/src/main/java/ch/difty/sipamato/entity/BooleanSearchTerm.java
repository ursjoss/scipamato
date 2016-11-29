package ch.difty.sipamato.entity;

/**
 * Implementation of {@link SearchTerm} working with Boolean values.
 *
 * @author u.joss
 */
public class BooleanSearchTerm extends SearchTerm<BooleanSearchTerm> {
    private static final long serialVersionUID = 1L;

    private final boolean value;

    BooleanSearchTerm(final String key, final String rawValue) {
        super(key, rawValue);
        final String rv = rawValue.trim();
        this.value = Boolean.valueOf(rv);
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        if (getValue()) {
            return getKey();
        } else {
            return "not " + getKey();
        }
    }
}