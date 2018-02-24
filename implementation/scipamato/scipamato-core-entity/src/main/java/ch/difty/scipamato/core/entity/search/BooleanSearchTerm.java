package ch.difty.scipamato.core.entity.search;

/**
 * Implementation of {@link AbstractSearchTerm} working with Boolean fields.
 *
 * There is no meta-specification possible in boolean
 * {@link AbstractSearchTerm}s. The value can either be {@code true} or
 * {@code false}.
 *
 * @author u.joss
 */
public class BooleanSearchTerm extends AbstractSearchTerm {
    private static final long serialVersionUID = 1L;

    private final boolean value;

    BooleanSearchTerm(final String fieldName, final String rawSearchTerm) {
        this(null, null, fieldName, rawSearchTerm);
    }

    BooleanSearchTerm(final Long searchConditionId, final String fieldName, final String rawSearchTerm) {
        this(null, searchConditionId, fieldName, rawSearchTerm);
    }

    BooleanSearchTerm(final Long id, final Long searchConditionId, final String fieldName, final String rawSearchTerm) {
        super(id, SearchTermType.BOOLEAN, searchConditionId, fieldName, rawSearchTerm);
        final String rst = rawSearchTerm.trim();
        this.value = Boolean.valueOf(rst);
    }

    public boolean getValue() {
        return value;
    }

    /**
     * If true: {@code fieldName}. If false: {@code -fieldName}
     */
    @Override
    public String getDisplayValue() {
        if (getValue()) {
            return getFieldName();
        } else {
            return "-" + getFieldName();
        }
    }
}