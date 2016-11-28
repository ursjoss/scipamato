package ch.difty.sipamato.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The composite filter makes up the entire search, containing one or combining several {@link CompmlexPaperFilter}s
 *
 * @author u.joss
 */
public class CompositeComplexPaperFilter extends SipamatoFilter {

    private static final long serialVersionUID = 1L;

    private static final String JOIN_DELIMITER = "; OR ";

    private final List<ComplexPaperFilter> filterList = new ArrayList<>();

    public CompositeComplexPaperFilter(final List<ComplexPaperFilter> filters) {
        if (filters != null)
            this.filterList.addAll(filters);
    }

    /**
     * @return the list of individual {@link ComplexPaperFilter}s
     */
    public List<ComplexPaperFilter> getFilters() {
        return filterList;
    }

    /**
     * Add a new instance of a {@link ComplexPaperFilter}.
     *
     * @param filter to be added.
     */
    public void add(final ComplexPaperFilter filter) {
        if (filter != null && !filterList.contains(filter))
            filterList.add(filter);
    }

    /**
     * Merges the {@link ComplexPaperFilter}s contained in the <code>other</code> {@link CompositeComplexPaperFilter}
     * into its own list.
     *
     * @param other the source of filters to merge from
     */
    public void merge(final CompositeComplexPaperFilter other) {
        filterList.addAll(other.getFilters());
    }

    /**
     * Removes the specified {@link ComplexPaperFilter} - if not null and present.
     *
     * @param filter the filter to remove
     */
    public void remove(ComplexPaperFilter filter) {
        if (filter != null) {
            filterList.remove(filter);
        }
    }

    @Override
    public String toString() {
        return filterList.stream().map(ComplexPaperFilter::toString).collect(Collectors.joining(JOIN_DELIMITER));
    }
}
