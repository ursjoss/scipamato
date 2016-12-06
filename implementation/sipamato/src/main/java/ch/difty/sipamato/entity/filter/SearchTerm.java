package ch.difty.sipamato.entity.filter;

import java.io.Serializable;

import ch.difty.sipamato.lib.AssertAs;

/**
 * Implementations of {@link SearchTerm} accept a <code>fieldName</code> as key and a <code>rawSearchTerm</code> as value.
 * The rawSearchTerm holds a comparison specification holding a value and some meta information on how to compare the field
 * with the provided value.
 * 
 * Identity is based on fielName and rawSearchTerm only.
 *
 * @author u.joss
 */
public abstract class SearchTerm<T extends SearchTerm<?>> implements Serializable {

    private static final long serialVersionUID = 1L;

    enum SearchTermType {
        BOOLEAN,
        INTEGER,
        STRING;
    }

    private final SearchTermType searchTermType;
    private final String fieldName;
    private final String searchTerm;

    SearchTerm(final SearchTermType type, final String fieldName, final String rawSearchTerm) {
        this.searchTermType = AssertAs.notNull(type);
        this.fieldName = AssertAs.notNull(fieldName, "fieldName");
        this.searchTerm = AssertAs.notNull(rawSearchTerm, "rawSearchTerm");
    }

    public SearchTermType getSearchTermType() {
        return searchTermType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getRawSearchTerm() {
        return searchTerm;
    }

    @Override
    public String toString() {
        return searchTerm;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + fieldName.hashCode();
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
        if (!fieldName.equals(other.getFieldName()))
            return false;
        else if (!searchTerm.equals(other.getRawSearchTerm()))
            return false;
        return true;
    }

}
