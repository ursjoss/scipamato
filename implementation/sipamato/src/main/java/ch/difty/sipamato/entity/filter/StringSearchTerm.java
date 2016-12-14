package ch.difty.sipamato.entity.filter;

/**
 * Implementation of {@link SearchTerm} working with String fields.
 * The following {@link MatchType}s are implemented:
 *
 * <ul>
 * <li> EXACT: <code> "foo" </code> or <code> ="foo" </code> </li>
 * <li> CONTAINS: <code> foo </code> or <code> *foo* </code>  or <CODE> "*foo*" </code></li>
 * <li> STARTS_WITH: <code> foo* </code> or <code> "foo*" </code> </li>
 * <li> ENDS_WITH: <code> *foo </code> or <code> "*foo" </code> </li>
 * </ul>
 *
 * The rawSearchTerms and their individual parts outside of quotation marks are trimmed, so the following examples are equally valid:
 *
 * <ul>
 * <li> <code>   foo </code> </li>
 * <li> <code>     "foo*" </code> </li>
 * </ul>
 *
 * @author u.joss
 */
@Deprecated
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

    StringSearchTerm(final String fieldName, final String rawSearchTerm) {
        this(null, fieldName, rawSearchTerm);
    }

    StringSearchTerm(final Long searchConditionId, final String fieldName, final String rawSearchTerm) {
        this(null, searchConditionId, fieldName, rawSearchTerm);
    }

    StringSearchTerm(final Long id, final Long searchConditionId, final String fieldName, final String rawSearchTerm) {
        super(id, SearchTermType.STRING, searchConditionId, fieldName, rawSearchTerm);
        final String rst = rawSearchTerm.trim();
        if (rst.length() >= 3 && rst.startsWith("=\"") && rst.endsWith("\"")) {
            this.type = MatchType.EXACT;
            this.value = rst.substring(2, rst.length() - 1).trim();
        } else if (rst.length() >= 4 && rst.startsWith("\"*") && rst.endsWith("*\"")) {
            this.type = MatchType.CONTAINS;
            this.value = rst.substring(2, rst.length() - 2);
        } else if (rst.length() >= 2 && rst.startsWith("*") && rst.endsWith("*")) {
            this.type = MatchType.CONTAINS;
            this.value = rst.substring(1, rst.length() - 1);
        } else if (rst.length() >= 3 && rst.startsWith("\"") && rst.endsWith("*\"")) {
            this.type = MatchType.STARTS_WITH;
            this.value = rst.substring(1, rst.length() - 2);
        } else if (rst.length() >= 3 && rst.startsWith("\"*") && rst.endsWith("\"")) {
            this.type = MatchType.ENDS_WITH;
            this.value = rst.substring(2, rst.length() - 1);
        } else if (rst.length() >= 2 && rst.startsWith("\"") && rst.endsWith("\"")) {
            this.type = MatchType.EXACT;
            this.value = rst.substring(1, rst.length() - 1).trim();
        } else if (rst.length() >= 2 && rst.endsWith("*")) {
            this.type = MatchType.STARTS_WITH;
            this.value = rst.substring(0, rst.length() - 1);
        } else if (rst.length() >= 2 && rst.startsWith("*")) {
            this.type = MatchType.ENDS_WITH;
            this.value = rst.substring(1, rst.length());
        } else {
            this.type = MatchType.CONTAINS;
            this.value = rst;
        }
    }

    public String getValue() {
        return value;
    }

    public MatchType getType() {
        return type;
    }

}