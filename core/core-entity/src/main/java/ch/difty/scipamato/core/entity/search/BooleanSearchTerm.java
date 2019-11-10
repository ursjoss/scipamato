package ch.difty.scipamato.core.entity.search;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implementation of {@link AbstractSearchTerm} working with Boolean fields.
 * <p>
 * There is no meta-specification possible in boolean
 * {@link AbstractSearchTerm}s. The value can either be {@code true} or
 * {@code false}.
 *
 * @author u.joss
 */
public class BooleanSearchTerm extends AbstractSearchTerm {
    private static final long serialVersionUID = 1L;

    private final boolean value;

    BooleanSearchTerm(@NotNull final String fieldName, @NotNull final String rawSearchTerm) {
        this(null, null, fieldName, rawSearchTerm);
    }

    BooleanSearchTerm(@Nullable final Long searchConditionId, @NotNull final String fieldName,
        @NotNull final String rawSearchTerm) {
        this(null, searchConditionId, fieldName, rawSearchTerm);
    }

    BooleanSearchTerm(@Nullable final Long id, @Nullable final Long searchConditionId, @NotNull final String fieldName,
        @NotNull final String rawSearchTerm) {
        super(id, SearchTermType.BOOLEAN, searchConditionId, fieldName, rawSearchTerm);
        final String rst = rawSearchTerm.trim();
        this.value = Boolean.parseBoolean(rst);
    }

    public boolean getValue() {
        return value;
    }

    /**
     * If true: {@code fieldName}. If false: {@code -fieldName}
     */
    @NotNull
    @Override
    public String getDisplayValue() {
        if (getValue()) {
            return getFieldName();
        } else {
            return "-" + getFieldName();
        }
    }
}