package ch.difty.sipamato.entity;

/**
 * Implementation of {@link SearchTerm} working with Integer values.
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
public class IntegerSearchTerm extends SearchTerm {
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

    IntegerSearchTerm(final String key, final String value) {
        super(key, value);
        final String rv = value.trim();
        if (rv.length() > 2 && rv.startsWith(">=")) {
            this.type = MatchType.GREATER_OR_EQUAL;
            this.value = Integer.parseInt(rv.substring(2, rv.length()).trim());
            this.value2 = this.value;
        } else if (rv.length() > 1 && rv.startsWith(">")) {
            this.type = MatchType.GREATER_THAN;
            this.value = Integer.parseInt(rv.substring(1, rv.length()).trim());
            this.value2 = this.value;
        } else if (rv.length() > 2 && rv.startsWith("<=")) {
            this.type = MatchType.LESS_OR_EQUAL;
            this.value = Integer.parseInt(rv.substring(2, rv.length()).trim());
            this.value2 = this.value;
        } else if (rv.length() > 1 && rv.startsWith("<")) {
            this.type = MatchType.LESS_THAN;
            this.value = Integer.parseInt(rv.substring(1, rv.length()).trim());
            this.value2 = this.value;
        } else if (rv.length() > 1 && rv.startsWith("=")) {
            this.type = MatchType.EXACT;
            this.value = Integer.parseInt(rv.substring(1, rv.length()).trim());
            this.value2 = this.value;
        } else if (rv.length() > 1 && rv.contains("-")) {
            this.type = MatchType.RANGE;
            this.value = Integer.parseInt(rv.substring(0, rv.indexOf("-")).trim());
            this.value2 = Integer.parseInt(rv.substring(rv.indexOf("-") + 1, rv.length()).trim());
        } else {
            this.type = MatchType.EXACT;
            this.value = Integer.parseInt(rv.trim());
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