package ch.difty.sipamato.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ch.difty.sipamato.entity.filter.PaperSlimFilter;
import ch.difty.sipamato.entity.filter.SearchCondition;

/**
 * The {@link SearchOrder} is the entry point for the entire (complex) search, containing one or combining several {@link SearchCondition}s
 *
 * @author u.joss
 */
public class SearchOrder extends IdSipamatoEntity<Long> implements PaperSlimFilter {

    private static final long serialVersionUID = 1L;

    public static final String NAME = "name";
    public static final String OWNER = "owner";
    public static final String GLOBAL = "global";
    public static final String CONDITIONS = "searchConditions";
    public static final String INVERT_EXCLUSIONS = "invertExclusions";

    private static final String JOIN_DELIMITER = "; OR ";

    private String name;
    private int owner;
    private boolean global;
    private final List<SearchCondition> searchConditions = new ArrayList<>();

    private final List<Long> excludedPaperIds = new ArrayList<>();

    // this will not get not persisted
    private boolean invertExclusions = false;

    public SearchOrder() {
        // default constructor
    }

    public SearchOrder(final List<SearchCondition> searchConditions) {
        setSearchConditions(searchConditions);
    }

    public SearchOrder(long id, String name, int owner, boolean global, List<SearchCondition> searchConditions, List<Long> excludedPaperIds) {
        setId(id);
        setName(name);
        setOwner(owner);
        setGlobal(global);
        setSearchConditions(searchConditions);
        setExcludedPaperIds(excludedPaperIds);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    public List<SearchCondition> getSearchConditions() {
        return searchConditions;
    }

    public void setSearchConditions(final List<SearchCondition> searchConditions) {
        if (searchConditions != null) {
            this.searchConditions.clear();
            this.searchConditions.addAll(searchConditions);
        }
    }

    /**
     * Add a new instance of a {@link SearchCondition}.
     *
     * @param searchCondition to be added.
     */
    public void add(final SearchCondition searchCondition) {
        if (searchCondition != null && !searchConditions.contains(searchCondition))
            searchConditions.add(searchCondition);
    }

    /**
     * Removes the specified {@link SearchCondition} - if not null and present.
     *
     * @param searchCondition the condition to remove
     */
    public void remove(SearchCondition searchCondition) {
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
     * @param id paper id to be added to exclusions
     */
    public void addExclusionOfPaperWithId(final long paperId) {
        if (!excludedPaperIds.contains(paperId))
            excludedPaperIds.add(paperId);
    }

    /**
     * Removes the specified paperId from the list of exclusions.
     *
     * @param paperId the id to remove from exclusions
     */
    public void removeExlusionOfPaperWithId(final long paperId) {
        excludedPaperIds.remove(paperId);
    }

    public boolean isInvertExclusions() {
        return invertExclusions;
    }

    public void setInvertExclusions(boolean invertExclusions) {
        this.invertExclusions = invertExclusions;
    }

    @Override
    public String getDisplayValue() {
        StringBuilder sb = new StringBuilder();
        if (getName() != null) {
            sb.append(getName()).append(": ");
        }
        sb.append(searchConditions.stream().map(SearchCondition::getDisplayValue).collect(Collectors.joining(JOIN_DELIMITER)));
        if (sb.length() == 0)
            sb.append("--");
        sb.append(" (").append(getId()).append(")");
        if (isGlobal())
            sb.append("*");
        return sb.toString();
    }

}
