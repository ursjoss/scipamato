package ch.difty.scipamato.core.entity.search;

/**
 * Implementation of {@link AbstractSearchTerm} working with Integer fields. The
 * following {@link MatchType}s are implemented:
 *
 * <ul>
 * <li><b>GREATER_THAN:</b> {@code  >2014 }</li>
 * <li><b>GREATER_OR_EQUAL:</b> {@code  >=2014 }</li>
 * <li><b>EXACT:</b> {@code  2014 } or {@code  =2014 }</li>
 * <li><b>LESS_OR_EQUAL:</b> {@code <=2014 }</li>
 * <li><b>LESS_THAN:</b> {@code <2014 }</li>
 * <li><b>RANGE:</b> {@code  2014-2017 }</li>
 * <li><b>MISSING:</b> the field has no value.</li>
 * <li><b>PRESENT:</b> the field has any value.</li>
 * </ul>
 * <p>
 * All rawValues and their individual parts are trimmed, so the following
 * examples are equally valid:
 *
 * <ul>
 * <li>{@code  > 2014 }</li>
 * <li>{@code <= 2014 }</li>
 * <li>{@code  2014  - 2017 }</li>
 * </ul>
 *
 * @author u.joss
 */
public class IntegerSearchTerm extends AbstractSearchTerm {
    private static final long serialVersionUID = 1L;

    public enum MatchType {
        EXACT,
        GREATER_THAN,
        GREATER_OR_EQUAL,
        LESS_THAN,
        LESS_OR_EQUAL,
        RANGE,
        MISSING,
        PRESENT
    }

    private final MatchType type;
    private final int       value;
    private final int       value2;

    IntegerSearchTerm(final String fieldName, final String rawSearchTerm) {
        this(null, fieldName, rawSearchTerm);
    }

    IntegerSearchTerm(final Long searchConditionId, final String fieldName, final String rawSearchTerm) {
        this(null, searchConditionId, fieldName, rawSearchTerm);
    }

    IntegerSearchTerm(final Long id, final Long searchConditionId, final String fieldName, final String rawSearchTerm) {
        super(id, SearchTermType.INTEGER, searchConditionId, fieldName, rawSearchTerm);
        final String rst = rawSearchTerm.trim();
        if ("=\"\"".equals(rst) || "\"\"".equals(rst)) {
            this.type = MatchType.MISSING;
            this.value = 0;
            this.value2 = 0;
        } else if (">\"\"".equals(rst)) {
            this.type = MatchType.PRESENT;
            this.value = 0;
            this.value2 = 0;
        } else if (rst.length() > 2 && rst.startsWith(">=")) {
            this.type = MatchType.GREATER_OR_EQUAL;
            this.value = Integer.parseInt(rst
                .substring(2, rst.length())
                .trim());
            this.value2 = this.value;
        } else if (rst.length() > 1 && rst.startsWith(">")) {
            this.type = MatchType.GREATER_THAN;
            this.value = Integer.parseInt(rst
                .substring(1, rst.length())
                .trim());
            this.value2 = this.value;
        } else if (rst.length() > 2 && rst.startsWith("<=")) {
            this.type = MatchType.LESS_OR_EQUAL;
            this.value = Integer.parseInt(rst
                .substring(2, rst.length())
                .trim());
            this.value2 = this.value;
        } else if (rst.length() > 1 && rst.startsWith("<")) {
            this.type = MatchType.LESS_THAN;
            this.value = Integer.parseInt(rst
                .substring(1, rst.length())
                .trim());
            this.value2 = this.value;
        } else if (rst.length() > 1 && rst.startsWith("=")) {
            this.type = MatchType.EXACT;
            this.value = Integer.parseInt(rst
                .substring(1, rst.length())
                .trim());
            this.value2 = this.value;
        } else if (rst.length() > 1 && rst.contains("-")) {
            this.type = MatchType.RANGE;
            this.value = Integer.parseInt(rst
                .substring(0, rst.indexOf('-'))
                .trim());
            this.value2 = Integer.parseInt(rst
                .substring(rst.indexOf('-') + 1, rst.length())
                .trim());
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