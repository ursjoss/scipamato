package ch.difty.scipamato.core.entity.search;

public interface SearchTerm {

    /**
     * Static factory method to produce implementations of {@link SearchTerm}s of
     * the various sub types.
     *
     * @param id
     *            the database id
     * @param searchTermTypeId
     *            one of the IDs as defined in enum {@link SearchTermType}
     * @param searchConditionId
     *            the database id of the associated search condition
     * @param fieldName
     *            the name of the field (in table paper) the search is to be
     *            performed on
     * @param rawSearchTerm
     *            the search term definition
     * @return one of the implementations of {@link SearchTerm}
     */
    static SearchTerm newSearchTerm(final long id, final int searchTermTypeId, final long searchConditionId,
            final String fieldName, final String rawSearchTerm) {
        return newSearchTerm(id, SearchTermType.byId(searchTermTypeId), searchConditionId, fieldName, rawSearchTerm);
    }

    /**
     * Static factory method to produce implementations of {@link SearchTerm}s of
     * the various sub types.
     *
     * @param id
     *            the database id
     * @param searchTermType
     *            {@link SearchTermType}
     * @param searchConditionId
     *            the database id of the associated search condition
     * @param fieldName
     *            the name of the field (in table paper) the search is to be
     *            performed on
     * @param rawSearchTerm
     *            the search term definition
     * @return one of the implementations of {@link SearchTerm}
     */
    static SearchTerm newSearchTerm(final long id, final SearchTermType type, final long searchConditionId,
            final String fieldName, final String rawSearchTerm) {
        switch (type) {
        case BOOLEAN:
            return new BooleanSearchTerm(id, searchConditionId, fieldName, rawSearchTerm);
        case INTEGER:
            return new IntegerSearchTerm(id, searchConditionId, fieldName, rawSearchTerm);
        case STRING:
            return new StringSearchTerm(id, searchConditionId, fieldName, rawSearchTerm);
        case AUDIT:
            return new AuditSearchTerm(id, searchConditionId, fieldName, rawSearchTerm);
        case UNSUPPORTED:
        default:
            throw new AssertionError("SearchTermType." + type + " is not supported");
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
    static StringSearchTerm newStringSearchTerm(final String fieldName, final String rawSearchTerm) {
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
    static IntegerSearchTerm newIntegerSearchTerm(final String fieldName, final String rawSearchTerm) {
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
    static BooleanSearchTerm newBooleanSearchTerm(final String fieldName, final String rawSearchTerm) {
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
    static AuditSearchTerm newAuditSearchTerm(final String fieldName, final String rawSearchTerm) {
        return new AuditSearchTerm(fieldName, rawSearchTerm);
    }

    /**
     * @return the database id of the search condition
     */
    Long getSearchConditionId();

    /**
     * @return the {@link SearchTermType}
     */
    SearchTermType getSearchTermType();

    /**
     * @return the name of the field that is searched
     */
    String getFieldName();

    /**
     * @return the raw search term
     */
    String getRawSearchTerm();

    /**
     * @return the display value for given search term
     */
    String getDisplayValue();

}
