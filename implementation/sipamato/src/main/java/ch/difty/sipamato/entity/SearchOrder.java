package ch.difty.sipamato.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ch.difty.sipamato.entity.filter.ComplexPaperFilter;
import ch.difty.sipamato.entity.filter.PaperSlimFilter;

/**
 * The {@link SearchOrder} is the entry point for the entire (complex) search, containing one or combining several {@link ComplexPaperFilter}s
 *
 * @author u.joss
 */
public class SearchOrder extends SipamatoEntity implements PaperSlimFilter {

    private static final long serialVersionUID = 1L;

    public static final String ID = "id";
    public static final String OWNER = "owner";
    public static final String GLOBAL = "global";
    public static final String FILTERS = "filters";

    private static final String JOIN_DELIMITER = "; OR ";

    private Integer id;
    private int owner;
    private boolean global;
    private final List<ComplexPaperFilter> filters = new ArrayList<>();

    public SearchOrder() {
    }

    public SearchOrder(final List<ComplexPaperFilter> filters) {
        setFilters(filters);
    }

    public SearchOrder(int id, int owner, boolean global, List<ComplexPaperFilter> filters) {
        setId(id);
        setOwner(owner);
        setGlobal(global);
        setFilters(filters);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public List<ComplexPaperFilter> getFilters() {
        return filters;
    }

    public void setFilters(final List<ComplexPaperFilter> filters) {
        if (filters != null) {
            this.filters.clear();
            this.filters.addAll(filters);
        }
    }

    /**
     * Add a new instance of a {@link ComplexPaperFilter}.
     *
     * @param filter to be added.
     */
    public void add(final ComplexPaperFilter filter) {
        if (filter != null && !filters.contains(filter))
            filters.add(filter);
    }

    /**
     * Merges the {@link ComplexPaperFilter}s contained in the <code>other</code> {@link SearchOrder}
     * into its own list.
     *
     * @param other the source of filters to merge from
     */
    public void merge(final SearchOrder other) {
        if (other != null)
            filters.addAll(other.getFilters());
    }

    /**
     * Removes the specified {@link ComplexPaperFilter} - if not null and present.
     *
     * @param filter the filter to remove
     */
    public void remove(ComplexPaperFilter filter) {
        if (filter != null) {
            filters.remove(filter);
        }
    }

    @Override
    public String getDisplayValue() {
        return filters.stream().map(ComplexPaperFilter::toString).collect(Collectors.joining(JOIN_DELIMITER));
    }

}
