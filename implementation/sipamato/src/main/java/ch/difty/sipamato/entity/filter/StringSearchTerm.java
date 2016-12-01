package ch.difty.sipamato.entity.filter;

/**
 * Implementation of {@link SearchTerm} working with String values.
 * The following {@link MatchType}s are implemented:
 *
 * <ul>
 * <li> EXACT: <code> "foo" </code> or <code> ="foo" </code> </li>
 * <li> CONTAINS: <code> foo </code> or <code> *foo* </code>  or <CODE> "*foo*" </code></li>
 * <li> STARTS_WITH: <code> foo* </code> or <code> "foo*" </code> </li>
 * <li> ENDS_WITH: <code> *foo </code> or <code> "*foo" </code> </li>
 * </ul>
 *
 * The rawValues and their individual parts outside of quotation marks are trimmed, so the following examples are equally valid:
 *
 * <ul>
 * <li> <code>   foo </code> </li>
 * <li> <code>     "foo*" </code> </li>
 * </ul>
 *
 * @author u.joss
 */
public class StringSearchTerm extends SearchTerm<StringSearchTerm> {
    private static final long serialVersionUID = 1L;

    public enum MatchType {
        EXACT,
        CONTAINS,
        STARTS_WITH,
        ENDS_WITH;
    }

    private final MatchType type;
    private final String value;

    StringSearchTerm(final String key, final String value) {
        super(key, value);
        final String rv = value.trim();
        if (rv.length() >= 3 && rv.startsWith("=\"") && rv.endsWith("\"")) {
            this.type = MatchType.EXACT;
            this.value = rv.substring(2, rv.length() - 1).trim();
        } else if (rv.length() >= 4 && rv.startsWith("\"*") && rv.endsWith("*\"")) {
            this.type = MatchType.CONTAINS;
            this.value = rv.substring(2, rv.length() - 2);
        } else if (rv.length() >= 2 && rv.startsWith("*") && rv.endsWith("*")) {
            this.type = MatchType.CONTAINS;
            this.value = rv.substring(1, rv.length() - 1);
        } else if (rv.length() >= 3 && rv.startsWith("\"") && rv.endsWith("*\"")) {
            this.type = MatchType.STARTS_WITH;
            this.value = rv.substring(1, rv.length() - 2);
        } else if (rv.length() >= 3 && rv.startsWith("\"*") && rv.endsWith("\"")) {
            this.type = MatchType.ENDS_WITH;
            this.value = rv.substring(2, rv.length() - 1);
        } else if (rv.length() >= 2 && rv.startsWith("\"") && rv.endsWith("\"")) {
            this.type = MatchType.EXACT;
            this.value = rv.substring(1, rv.length() - 1).trim();
        } else if (rv.length() >= 2 && rv.endsWith("*")) {
            this.type = MatchType.STARTS_WITH;
            this.value = rv.substring(0, rv.length() - 1);
        } else if (rv.length() >= 2 && rv.startsWith("*")) {
            this.type = MatchType.ENDS_WITH;
            this.value = rv.substring(1, rv.length());
        } else {
            this.type = MatchType.CONTAINS;
            this.value = rv;
        }
    }

    public String getValue() {
        return value;
    }

    public MatchType getType() {
        return type;
    }

}