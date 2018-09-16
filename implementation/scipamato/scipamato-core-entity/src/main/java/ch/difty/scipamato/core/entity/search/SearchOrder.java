package ch.difty.scipamato.core.entity.search;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import ch.difty.scipamato.common.entity.FieldEnumType;
import ch.difty.scipamato.core.entity.IdScipamatoEntity;
import ch.difty.scipamato.core.entity.PaperSlimFilter;

/**
 * The {@link SearchOrder} is the entry point for the entire (complex) search,
 * containing one or combining several {@link SearchCondition}s
 *
 * @author u.joss
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, exclude = { "showExcluded" })
public class SearchOrder extends IdScipamatoEntity<Long> implements PaperSlimFilter {

    private static final long serialVersionUID = 1L;

    private static final String JOIN_DELIMITER = "; OR ";

    private String  name;
    private int     owner;
    private boolean global;

    private final List<SearchCondition> searchConditions = new ArrayList<>();
    private final List<Long>            excludedPaperIds = new ArrayList<>();

    // this will not get persisted
    private boolean showExcluded = false;

    public enum SearchOrderFields implements FieldEnumType {
        NAME("name"),
        OWNER("owner"),
        GLOBAL("global"),
        CONDITIONS("searchConditions"),
        EXCLUDED_PAPER_IDS("excludedPaperIds"),
        SHOW_EXCLUDED("showExcluded");

        private final String name;

        SearchOrderFields(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    public SearchOrder() {
        // default constructor
    }

    public SearchOrder(final List<SearchCondition> searchConditions) {
        setSearchConditions(searchConditions);
    }

    public SearchOrder(final long id, final String name, final int owner, final boolean global,
        final List<SearchCondition> searchConditions, final List<Long> excludedPaperIds) {
        setId(id);
        setName(name);
        setOwner(owner);
        setGlobal(global);
        setSearchConditions(searchConditions);
        setExcludedPaperIds(excludedPaperIds);
    }

    private void setSearchConditions(final List<SearchCondition> searchConditions) {
        if (searchConditions != null) {
            this.searchConditions.clear();
            this.searchConditions.addAll(searchConditions);
        }
    }

    /**
     * Add a new instance of a {@link SearchCondition}.
     *
     * @param searchCondition
     *     to be added.
     */
    public void add(final SearchCondition searchCondition) {
        if (searchCondition != null && !searchConditions.contains(searchCondition))
            searchConditions.add(searchCondition);
    }

    /**
     * Removes the specified {@link SearchCondition} - if not null and present.
     *
     * @param searchCondition
     *     the condition to remove
     */
    public void remove(final SearchCondition searchCondition) {
        if (searchCondition != null) {
            searchConditions.remove(searchCondition);
        }
    }

    public List<Long> getExcludedPaperIds() {
        return excludedPaperIds;
    }

    public void setExcludedPaperIds(final List<Long> excludedPaperIds) {
        if (excludedPaperIds != null) {
            this.excludedPaperIds.clear();
            this.excludedPaperIds.addAll(excludedPaperIds);
        }
    }

    /**
     * Add a new paper id for exclusion.
     *
     * @param paperId
     *     paper id to be added to exclusions
     */
    public void addExclusionOfPaperWithId(final long paperId) {
        if (!excludedPaperIds.contains(paperId))
            excludedPaperIds.add(paperId);
    }

    /**
     * Removes the specified paperId from the list of exclusions.
     *
     * @param paperId
     *     the id to remove from exclusions
     */
    public void removeExclusionOfPaperWithId(final long paperId) {
        excludedPaperIds.remove(paperId);
    }

    @Override
    public String getDisplayValue() {
        final StringBuilder sb = new StringBuilder();
        if (getName() != null) {
            sb
                .append(getName())
                .append(": ");
        }
        sb.append(searchConditions
            .stream()
            .map(SearchCondition::getDisplayValue)
            .collect(Collectors.joining(JOIN_DELIMITER)));
        if (sb.length() == 0)
            sb.append("--");
        sb
            .append(" (")
            .append(getId())
            .append(")");
        if (isGlobal())
            sb.append("*");
        return sb.toString();
    }

}
