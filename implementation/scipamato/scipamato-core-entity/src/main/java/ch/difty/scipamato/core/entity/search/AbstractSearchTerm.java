package ch.difty.scipamato.core.entity.search;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.core.entity.IdScipamatoEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Implementations of {@link AbstractSearchTerm} accept a {@code fieldName} as key and a
 * {@code rawSearchTerm} as value. The rawSearchTerm holds a comparison
 * specification holding a value and some meta information on how to compare the
 * field with the provided value.
 * <p>
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

    AbstractSearchTerm(final Long id, final SearchTermType type, final Long searchConditionId, final String fieldName,
            final String rawSearchTerm) {
        super(id);
        this.searchConditionId = searchConditionId;
        this.searchTermType = AssertAs.notNull(type);
        this.fieldName = AssertAs.notNull(fieldName, "fieldName");
        this.rawSearchTerm = AssertAs.notNull(rawSearchTerm, "rawSearchTerm");
    }

    @Override
    public String getDisplayValue() {
        return rawSearchTerm;
    }

}
