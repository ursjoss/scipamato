package ch.difty.scipamato.core.entity.search;

import java.io.Serializable;
import java.util.LinkedHashMap;

public abstract class SearchTerms<C extends Serializable> extends LinkedHashMap<String, C> {

    private static final long serialVersionUID = 1L;

    /**
     * Static factory method to produce instances of {@link SearchTerm}s of the
     * various subtypes.
     *
     * @param id
     *            the database id
     * @param searchTermTypeId
     *            one of the ids as defined in enum {@link SearchTermType}
     * @param searchConditionId
     *            the database id of the associated search condition
     * @param fieldName
     *            the name of the field (in table paper) the search is to be
     *            performed on
     * @param rawSearchTerm
     *            the search term definition
     * @return one of the subtypes of {@link SearchTerm}
     */
    public static SearchTerm newSearchTerm(final long id, final int searchTermTypeId, final long searchConditionId,
            final String fieldName, final String rawSearchTerm) {
        final SearchTermType type = SearchTermType.byId(searchTermTypeId);
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

    /**
     * Instantiates a new {@link StringSearchTerm} based on fieldName and
     * rawSearchTerm only
     *
     * @param fieldName
     * @param rawSearchTerm
     * @return the search term
     */
    public static StringSearchTerm newStringSearchTerm(final String fieldName, final String rawSearchTerm) {
        return new StringSearchTerm(fieldName, rawSearchTerm);
    }

    /**
     * Instantiates a new {@link IntegerSearchTerm} based on fieldName and
     * rawSearchTerm only
     *
     * @param fieldName
     * @param rawSearchTerm
     * @return the search term
     */
    public static IntegerSearchTerm newIntegerSearchTerm(final String fieldName, final String rawSearchTerm) {
        return new IntegerSearchTerm(fieldName, rawSearchTerm);
    }

    /**
     * Instantiates a new {@link BooleanSearchTerm} based on fieldName and
     * rawSearchTerm only
     *
     * @param fieldName
     * @param rawSearchTerm
     * @return the search term
     */
    public static BooleanSearchTerm newBooleanSearchTerm(final String fieldName, final String rawSearchTerm) {
        return new BooleanSearchTerm(fieldName, rawSearchTerm);
    }

    /**
     * Instantiates a new {@link AuditSearchTerm} based on fieldName and
     * rawSearchTerm only
     *
     * @param fieldName
     * @param rawSearchTerm
     * @return the search term
     */
    public static AuditSearchTerm newAuditSearchTerm(final String fieldName, final String rawSearchTerm) {
        return new AuditSearchTerm(fieldName, rawSearchTerm);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        for (final C st : values()) {
            result = prime * result + st.hashCode();
        }
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
        final LinkedHashMap<String, C> other = (LinkedHashMap<String, C>) obj;
        if (values().size() != other.values()
            .size())
            return false;
        else
            for (final C st : values())
                if (!other.values()
                    .contains(st))
                    return false;
        return true;
    }
}
