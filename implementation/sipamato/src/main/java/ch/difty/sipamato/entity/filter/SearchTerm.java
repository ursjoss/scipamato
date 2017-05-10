package ch.difty.sipamato.entity.filter;

import ch.difty.sipamato.entity.IdSipamatoEntity;
import ch.difty.sipamato.lib.AssertAs;

/**
 * Implementations of {@link SearchTerm} accept a {@code fieldName} as key and a {@code rawSearchTerm} as value.
 * The rawSearchTerm holds a comparison specification holding a value and some meta information on how to compare the field
 * with the provided value.<p>
 *
 * <b>Note:</b>Identity is based on {@code fieldName} and {@code rawSearchTerm} only, thus ignoring {@code id}
 *             or {@code searchConditionId}. This might be an issue in some use cases in the future!
 *
 * @author u.joss
 */
public abstract class SearchTerm extends IdSipamatoEntity<Long> {

    private static final long serialVersionUID = 1L;

    private final Long searchConditionId;
    private final SearchTermType searchTermType;
    private final String fieldName;
    private final String rawSearchTerm;

    SearchTerm(final SearchTermType type, final String fieldName, final String rawSearchTerm) {
        this(null, type, null, fieldName, rawSearchTerm);
    }

    SearchTerm(final Long id, final SearchTermType type, final Long searchConditionId, final String fieldName, final String rawSearchTerm) {
        super(id);
        this.searchConditionId = searchConditionId;
        this.searchTermType = AssertAs.notNull(type);
        this.fieldName = AssertAs.notNull(fieldName, "fieldName");
        this.rawSearchTerm = AssertAs.notNull(rawSearchTerm, "rawSearchTerm");
    }

    public static SearchTerm of(final long id, final int searchTermTypeId, final long searchConditionId, final String fieldName, final String rawSearchTerm) {
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

    public SearchTermType getSearchTermType() {
        return searchTermType;
    }

    public Long getSearchConditionId() {
        return searchConditionId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getRawSearchTerm() {
        return rawSearchTerm;
    }

    @Override
    public String getDisplayValue() {
        return rawSearchTerm;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + fieldName.hashCode();
        result = prime * result + rawSearchTerm.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final SearchTerm other = (SearchTerm) obj;
        if (fieldName.hashCode() != other.getFieldName().hashCode())
            return false;
        if (!rawSearchTerm.equals(other.getRawSearchTerm()))
            return false;
        return true;
    }

}
