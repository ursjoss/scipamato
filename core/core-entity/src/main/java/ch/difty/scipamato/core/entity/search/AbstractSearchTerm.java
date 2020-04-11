package ch.difty.scipamato.core.entity.search;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.core.entity.IdScipamatoEntity;

/**
 * Implementations of {@link AbstractSearchTerm} accept a {@code fieldName} as
 * key and a {@code rawSearchTerm} as value. The rawSearchTerm holds a
 * comparison specification holding a value and some meta information on how to
 * compare the field with the provided value.
 *
 *
 * <b>Note:</b>Identity is based on {@code fieldName} and {@code rawSearchTerm}
 * only, thus ignoring {@code id} or {@code searchConditionId}. This might be an
 * issue in some use cases in the future!
 *
 * @author u.joss
 */
@Data
@EqualsAndHashCode(callSuper = false, exclude = { "searchConditionId", "searchTermType" })
public abstract class AbstractSearchTerm extends IdScipamatoEntity<Long> implements SearchTerm {

    private static final long serialVersionUID = 1L;

    private final Long           searchConditionId;
    private final SearchTermType searchTermType;
    private final String         fieldName;
    private final String         rawSearchTerm;

    AbstractSearchTerm(@Nullable final Long id, @NotNull final SearchTermType type,
        @Nullable final Long searchConditionId, @NotNull final String fieldName, @NotNull final String rawSearchTerm) {
        super(id);
        this.searchConditionId = searchConditionId;
        this.searchTermType = type;
        this.fieldName = fieldName;
        this.rawSearchTerm = rawSearchTerm;
    }

    @NotNull
    @Override
    public String getDisplayValue() {
        return rawSearchTerm;
    }

    @NotNull
    @Override
    public String getRawSearchTerm() {
        return rawSearchTerm;
    }

    @Nullable
    @Override
    public Long getSearchConditionId() {
        return searchConditionId;
    }

    @NotNull
    @Override
    public SearchTermType getSearchTermType() {
        return searchTermType;
    }

    @NotNull
    @Override
    public String getFieldName() {
        return fieldName;
    }
}
