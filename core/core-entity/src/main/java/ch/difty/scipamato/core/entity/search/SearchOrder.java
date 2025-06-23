package ch.difty.scipamato.core.entity.search;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    static final int    DISPL_VALUE_THRESHOLD = 100;
    static final String DISPL_VALUE_ELLIPSIS  = "...";
    static final int    DISPL_VALUE_CUTOFF    = DISPL_VALUE_THRESHOLD - DISPL_VALUE_ELLIPSIS.length();

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

        @NotNull
        @Override
        public String getFieldName() {
            return name;
        }
    }

    public SearchOrder() {
        // default constructor
    }

    public SearchOrder(@Nullable final List<SearchCondition> searchConditions) {
        setSearchConditions(searchConditions);
    }

    public SearchOrder(final long id, @Nullable final String name, final int owner, final boolean global,
        @Nullable final List<SearchCondition> searchConditions, @Nullable final List<Long> excludedPaperIds) {
        setId(id);
        setName(name);
        setOwner(owner);
        setGlobal(global);
        setSearchConditions(searchConditions);
        setExcludedPaperIds(excludedPaperIds);
    }

    private void setSearchConditions(@Nullable final List<SearchCondition> searchConditions) {
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
    public void add(@Nullable final SearchCondition searchCondition) {
        if (searchCondition != null && !searchConditions.contains(searchCondition))
            searchConditions.add(searchCondition);
    }

    /**
     * Removes the specified {@link SearchCondition} - if not null and present.
     *
     * @param searchCondition
     *     the condition to remove
     */
    public void remove(@Nullable final SearchCondition searchCondition) {
        if (searchCondition != null) {
            searchConditions.remove(searchCondition);
        }
    }

    @NotNull
    public List<Long> getExcludedPaperIds() {
        return excludedPaperIds;
    }

    public void setExcludedPaperIds(@Nullable final List<Long> excludedPaperIds) {
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

    public String getFullDisplayValue() {
        final StringBuilder sb = part1(false);
        part2(sb);
        return sb.toString();
    }

    @NotNull
    private StringBuilder part1(final boolean truncate) {
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
        if (sb.isEmpty())
            sb.append("--");

        if (truncate && sb.length() > DISPL_VALUE_THRESHOLD) {
            sb.delete(DISPL_VALUE_CUTOFF, sb.length());
            sb.append(DISPL_VALUE_ELLIPSIS);
        }

        if (!excludedPaperIds.isEmpty())
            sb.append(" !");

        return sb;
    }

    private void part2(@NotNull final StringBuilder sb) {
        sb
            .append(" (")
            .append(getId())
            .append(")");
        if (isGlobal())
            sb.append("*");
    }

    @NotNull
    public String getDisplayValue() {
        final StringBuilder sb = part1(true);
        part2(sb);
        return sb.toString();
    }
}
