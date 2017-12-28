package ch.difty.scipamato.core.entity.filter;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.core.entity.IdScipamatoEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Implementations of {@link SearchTerm} accept a {@code fieldName} as key and a
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
public abstract class SearchTerm extends IdScipamatoEntity<Long> {

    private static final long serialVersionUID = 1L;

    private final Long           searchConditionId;
    private final SearchTermType searchTermType;
    private final String         fieldName;
    private final String         rawSearchTerm;

    SearchTerm(final Long id, final SearchTermType type, final Long searchConditionId, final String fieldName,
            final String rawSearchTerm) {
        super(id);
        this.searchConditionId = searchConditionId;
        this.searchTermType = AssertAs.notNull(type);
        this.fieldName = AssertAs.notNull(fieldName, "fieldName");
        this.rawSearchTerm = AssertAs.notNull(rawSearchTerm, "rawSearchTerm");
    }

    public static SearchTerm of(final long id, final int searchTermTypeId, final long searchConditionId,
            final String fieldName, final String rawSearchTerm) {
        SearchTermType type = SearchTermType.byId(searchTermTypeId);
        switch (type) {
        case BOOLEAN:
            return new BooleanSearchTerm(id, searchConditionId, fieldName, rawSearchTerm);
        case INTEGER:
            return new IntegerSearchTerm(id, searchConditionId, fieldName, rawSearchTerm);
        case STRING:
            return new StringSearchTerm(id, searchConditionId, fieldName, rawSearchTerm);
        case AUDIT:
            return new AuditSearchTerm(id, searchConditionId, fieldName, rawSearchTerm);
        default:
            throw new UnsupportedOperationException("SearchTermType." + type + " is not supported");
        }
    }

    @Override
    public String getDisplayValue() {
        return rawSearchTerm;
    }

}
