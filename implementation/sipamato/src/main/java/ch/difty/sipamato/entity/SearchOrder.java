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

    public static final String OWNER = "owner";
    public static final String GLOBAL = "global";
    public static final String CONDITIONS = "searchConditions";

    private static final String JOIN_DELIMITER = "; OR ";

    private int owner;
    private boolean global;
    private final List<SearchCondition> searchConditions = new ArrayList<>();

    public SearchOrder() {
    }

    public SearchOrder(final List<SearchCondition> searchConditions) {
        setSearchConditions(searchConditions);
    }

    public SearchOrder(long id, int owner, boolean global, List<SearchCondition> searchConditions) {
        setId(id);
        setOwner(owner);
        setGlobal(global);
        setSearchConditions(searchConditions);
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
     * Merges the {@link SearchCondition}s contained in the <code>other</code> {@link SearchOrder}
     * into its own list.
     *
     * @param other the source of search conditions to merge from
     */
    public void merge(final SearchOrder other) {
        if (other != null)
            searchConditions.addAll(other.getSearchConditions());
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

    @Override
    public String getDisplayValue() {
        StringBuilder sb = new StringBuilder();
        sb.append(searchConditions.stream().map(SearchCondition::toString).collect(Collectors.joining(JOIN_DELIMITER)));
        if (sb.length() == 0)
            sb.append("--");
        sb.append(" (").append(getId()).append(")");
        if (isGlobal())
            sb.append("*");
        return sb.toString();
    }

}
