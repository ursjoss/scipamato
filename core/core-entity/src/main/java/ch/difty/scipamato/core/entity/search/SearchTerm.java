package ch.difty.scipamato.core.entity.search;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SearchTerm {

    /**
     * Static factory method to produce implementations of {@link SearchTerm}s of
     * the various sub types.
     *
     * @param id
     *     the database id
     * @param searchTermTypeId
     *     one of the IDs as defined in enum {@link SearchTermType}
     * @param searchConditionId
     *     the database id of the associated search condition
     * @param fieldName
     *     the name of the field (in table paper) the search is to be
     *     performed on
     * @param rawSearchTerm
     *     the search term definition
     * @return one of the implementations of {@link SearchTerm}
     */
    @NotNull
    static SearchTerm newSearchTerm(final long id, final int searchTermTypeId, final long searchConditionId, @NotNull final String fieldName,
        @NotNull final String rawSearchTerm) {
        return newSearchTerm(id, SearchTermType.byId(searchTermTypeId), searchConditionId, fieldName, rawSearchTerm);
    }

    /**
     * Static factory method to produce implementations of {@link SearchTerm}s of
     * the various sub types.
     *
     * @param id
     *     the database id
     * @param type
     *     {@link SearchTermType}
     * @param searchConditionId
     *     the database id of the associated search condition
     * @param fieldName
     *     the name of the field (in table paper) the search is to be performed on
     * @param rawSearchTerm
     *     the search term definition
     * @return one of the implementations of {@link SearchTerm}
     */
    @NotNull
    static SearchTerm newSearchTerm(final long id, @NotNull final SearchTermType type, final long searchConditionId, @NotNull final String fieldName,
        @NotNull final String rawSearchTerm) {
        return switch (type) {
            case BOOLEAN -> new BooleanSearchTerm(id, searchConditionId, fieldName, rawSearchTerm);
            case INTEGER -> new IntegerSearchTerm(id, searchConditionId, fieldName, rawSearchTerm);
            case STRING -> new StringSearchTerm(id, searchConditionId, fieldName, rawSearchTerm);
            case AUDIT -> new AuditSearchTerm(id, searchConditionId, fieldName, rawSearchTerm);
            default -> throw new AssertionError("SearchTermType." + type + " is not supported");
        };
    }

    /**
     * Instantiates a new {@link StringSearchTerm} based on fieldName and
     * rawSearchTerm only
     *
     * @param fieldName
     *     the name of the field (in table paper) the search is to be performed on
     * @param rawSearchTerm
     *     the search term definition
     * @return the search term
     */
    @NotNull
    static StringSearchTerm newStringSearchTerm(@NotNull final String fieldName, @NotNull final String rawSearchTerm) {
        return new StringSearchTerm(fieldName, rawSearchTerm);
    }

    /**
     * Instantiates a new {@link IntegerSearchTerm} based on fieldName and
     * rawSearchTerm only
     *
     * @param fieldName
     *     the name of the field (in table paper) the search is to be performed on
     * @param rawSearchTerm
     *     the search term definition
     * @return the search term
     */
    @NotNull
    static IntegerSearchTerm newIntegerSearchTerm(@NotNull final String fieldName, @NotNull final String rawSearchTerm) {
        return new IntegerSearchTerm(fieldName, rawSearchTerm);
    }

    /**
     * Instantiates a new {@link BooleanSearchTerm} based on fieldName and
     * rawSearchTerm only
     *
     * @param fieldName
     *     the name of the field (in table paper) the search is to be performed on
     * @param rawSearchTerm
     *     the search term definition
     * @return the search term
     */
    @NotNull
    static BooleanSearchTerm newBooleanSearchTerm(@NotNull final String fieldName, @NotNull final String rawSearchTerm) {
        return new BooleanSearchTerm(fieldName, rawSearchTerm);
    }

    /**
     * Instantiates a new {@link AuditSearchTerm} based on fieldName and
     * rawSearchTerm only
     *
     * @param fieldName
     *     the name of the field (in table paper) the search is to be performed on
     * @param rawSearchTerm
     *     the raw search term string
     * @return the search term
     */
    @NotNull
    static AuditSearchTerm newAuditSearchTerm(@NotNull final String fieldName, @NotNull final String rawSearchTerm) {
        return new AuditSearchTerm(fieldName, rawSearchTerm);
    }

    /**
     * @return the database id of the search condition
     */
    @Nullable
    Long getSearchConditionId();

    /**
     * @return the {@link SearchTermType}
     */
    @NotNull
    SearchTermType getSearchTermType();

    /**
     * @return the name of the field that is searched
     */
    @NotNull
    String getFieldName();

    /**
     * @return the raw search term
     */
    @NotNull
    String getRawSearchTerm();

    /**
     * @return the display value for given search term
     */
    @NotNull
    String getDisplayValue();
}
