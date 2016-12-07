package ch.difty.sipamato.entity.filter;

import ch.difty.sipamato.entity.IdSipamatoEntity;
import ch.difty.sipamato.lib.AssertAs;

/**
 * Implementations of {@link SearchTerm} accept a <code>fieldName</code> as key and a <code>rawSearchTerm</code> as value.
 * The rawSearchTerm holds a comparison specification holding a value and some meta information on how to compare the field
 * with the provided value.
 * 
 * Identity is based on fielName and rawSearchTerm, as well as the searchConditionId only.
 *
 * @author u.joss
 */
public abstract class SearchTerm<T extends SearchTerm<?>> extends IdSipamatoEntity<Long> {

    private static final long serialVersionUID = 1L;

    private final Long searchConditionId;
    private final SearchTermType searchTermType;
    private final String fieldName;
    private final String searchTerm;

    SearchTerm(final SearchTermType type, final String fieldName, final String rawSearchTerm) {
        this(null, type, null, fieldName, rawSearchTerm);
    }

    SearchTerm(final Long id, final SearchTermType type, final Long searchConditionId, final String fieldName, final String rawSearchTerm) {
        super(id);
        this.searchConditionId = searchConditionId;
        this.searchTermType = AssertAs.notNull(type);
        this.fieldName = AssertAs.notNull(fieldName, "fieldName");
        this.searchTerm = AssertAs.notNull(rawSearchTerm, "rawSearchTerm");
    }

    public static SearchTerm<?> of(final long id, final int searchTermTypeId, final long searchConditionId, final String fieldName, final String rawSearchTerm) {
        SearchTermType type = SearchTermType.byId(searchTermTypeId);
        switch (type) {
        case BOOLEAN:
            return new BooleanSearchTerm(id, searchConditionId, fieldName, rawSearchTerm);
        case INTEGER:
            return new IntegerSearchTerm(id, searchConditionId, fieldName, rawSearchTerm);
        case STRING:
            return new StringSearchTerm(id, searchConditionId, fieldName, rawSearchTerm);
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
        return searchTerm;
    }

    @Override
    public String getDisplayValue() {
        return searchTerm;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + fieldName.hashCode();
        result = prime * result + (searchConditionId == null ? 0 : (int) (searchConditionId ^ (searchConditionId >>> 32)));
        result = prime * result + searchTerm.hashCode();
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
        @SuppressWarnings("unchecked")
        final T other = (T) obj;
        if (searchConditionId == null) {
            if (other.getSearchConditionId() != null)
                return false;
        } else if (!searchConditionId.equals(other.getSearchConditionId()))
            return false;
        if (!fieldName.equals(other.getFieldName()))
            return false;
        else if (!searchTerm.equals(other.getRawSearchTerm()))
            return false;
        return true;
    }

}
