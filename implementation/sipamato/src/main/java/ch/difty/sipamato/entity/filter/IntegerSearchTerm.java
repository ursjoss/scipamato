package ch.difty.sipamato.entity.filter;

/**
 * Implementation of {@link SearchTerm} working with Integer fields.
 * The following {@link MatchType}s are implemented:
 *
 * <ul>
 * <li> GREATER_THAN: <code> >2014 </code> </li>
 * <li> GREATER_OR_EQUAL: <code> >=2014 </code> </li>
 * <li> EXACT: <code> 2014 </code> or <code> =2014 </code> </li>
 * <li> LESS_OR_EQUAL: <code> <=2014 </code> </li>
 * <li> LESS_THAN: <code> <2014 </code> </li>
 * <li> RANGE: <code> 2014-2017 </code> </li>
 * </ul>
 *
 * All rawValues and their individual parts are trimmed, so the following examples are equally valid:
 *
 * <ul>
 * <li> <code> >  2014 </code> </li>
 * <li> <code> <= 2014 </code> </li>
 * <li> <code> 2014  - 2017 </code> </li>
 * </ul>
 *
 * @author u.joss
 */
public class IntegerSearchTerm extends SearchTerm<IntegerSearchTerm> {
    private static final long serialVersionUID = 1L;

    public enum MatchType {
        EXACT,
        GREATER_THAN,
        GREATER_OR_EQUAL,
        LESS_THAN,
        LESS_OR_EQUAL,
        RANGE;
    }

    private final MatchType type;
    private final int value;
    private final int value2;

    IntegerSearchTerm(final String fieldName, final String rawSearchTerm) {
        super(SearchTermType.INTEGER, fieldName, rawSearchTerm);
        final String rst = rawSearchTerm.trim();
        if (rst.length() > 2 && rst.startsWith(">=")) {
            this.type = MatchType.GREATER_OR_EQUAL;
            this.value = Integer.parseInt(rst.substring(2, rst.length()).trim());
            this.value2 = this.value;
        } else if (rst.length() > 1 && rst.startsWith(">")) {
            this.type = MatchType.GREATER_THAN;
            this.value = Integer.parseInt(rst.substring(1, rst.length()).trim());
            this.value2 = this.value;
        } else if (rst.length() > 2 && rst.startsWith("<=")) {
            this.type = MatchType.LESS_OR_EQUAL;
            this.value = Integer.parseInt(rst.substring(2, rst.length()).trim());
            this.value2 = this.value;
        } else if (rst.length() > 1 && rst.startsWith("<")) {
            this.type = MatchType.LESS_THAN;
            this.value = Integer.parseInt(rst.substring(1, rst.length()).trim());
            this.value2 = this.value;
        } else if (rst.length() > 1 && rst.startsWith("=")) {
            this.type = MatchType.EXACT;
            this.value = Integer.parseInt(rst.substring(1, rst.length()).trim());
            this.value2 = this.value;
        } else if (rst.length() > 1 && rst.contains("-")) {
            this.type = MatchType.RANGE;
            this.value = Integer.parseInt(rst.substring(0, rst.indexOf("-")).trim());
            this.value2 = Integer.parseInt(rst.substring(rst.indexOf("-") + 1, rst.length()).trim());
        } else {
            this.type = MatchType.EXACT;
            this.value = Integer.parseInt(rst.trim());
            this.value2 = this.value;
        }
    }

    public int getValue() {
        return value;
    }

    public int getValue2() {
        return value2;
    }

    public MatchType getType() {
        return type;
    }

}