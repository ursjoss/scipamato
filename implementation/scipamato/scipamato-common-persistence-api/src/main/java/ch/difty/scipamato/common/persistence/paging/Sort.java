package ch.difty.scipamato.common.persistence.paging;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.util.StringUtils;

import ch.difty.scipamato.common.AssertAs;

/**
 * Sort specification offering a list of {@link SortProperty} items, each
 * providing a name of the to be sorted property and the sort direction.
 * <p>
 * This Sort implementation initially was a simplified version of the spring
 * data Sort class.
 *
 * @author u.joss
 */
public class Sort implements Iterable<ch.difty.scipamato.common.persistence.paging.Sort.SortProperty>, Serializable {

    private static final long serialVersionUID = 1L;

    private final List<SortProperty> sortProperties;

    /**
     * Instantiate {@link Sort} with a predefined list of {@link SortProperty} items
     *
     * @param sortProperties
     */
    public Sort(final List<SortProperty> sortProperties) {
        checkPreconditionsFor(sortProperties);
        this.sortProperties = new ArrayList<>(sortProperties.size());
        this.sortProperties.addAll(sortProperties);
    }

    /**
     * Instantiate {@link Sort} with various property names all to be sorted in the
     * same {@link Direction}
     *
     * @param direction
     *            the sort direction
     * @param propertyNames
     *            a list of property names to be sorted
     */
    public Sort(final Direction direction, final String... propertyNames) {
        checkPreconditionsFor(propertyNames);

        this.sortProperties = new ArrayList<>(propertyNames.length);
        for (final String pn : propertyNames)
            sortProperties.add(new SortProperty(pn, direction));
    }

    private void checkPreconditionsFor(final List<SortProperty> sortProperties) {
        AssertAs.notNull(sortProperties, "sortProperties");
        if (sortProperties.isEmpty())
            throw new IllegalArgumentException("sortProperties can't be empty.");
    }

    private void checkPreconditionsFor(final String... propertyNames) {
        AssertAs.notNull(propertyNames, "propertyNames");
        if (propertyNames.length == 0)
            throw new IllegalArgumentException("propertyNames can't be empty.");
    }

    @Override
    public Iterator<SortProperty> iterator() {
        return this.sortProperties.iterator();
    }

    public SortProperty getSortPropertyFor(final String propertyName) {
        for (final SortProperty sortProperty : this.sortProperties)
            if (sortProperty.getName()
                .equals(propertyName))
                return sortProperty;
        return null;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (this.getClass() != obj.getClass())
            return false;
        final Sort that = (Sort) obj;
        return this.sortProperties.equals(that.sortProperties);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + sortProperties.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return StringUtils.collectionToCommaDelimitedString(sortProperties);
    }

    /**
     * Individual sort specification for a particular property, consisting of a
     * property name and a sort direction.
     */
    public static class SortProperty implements Serializable {

        private static final long serialVersionUID = 1L;

        private static final Direction DEFAULT_DIRECTION = Direction.ASC;

        private final String    name;
        private final Direction direction;

        public SortProperty(final String name, final Direction direction) {
            this.name = AssertAs.notNull(name, "name");
            this.direction = direction != null ? direction : DEFAULT_DIRECTION;
        }

        public String getName() {
            return name;
        }

        public Sort.Direction getDirection() {
            return direction;
        }

        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result + direction.hashCode();
            result = 31 * result + name.hashCode();
            return result;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == null)
                return false;
            if (this == obj)
                return true;
            if (this.getClass() != obj.getClass())
                return false;
            final SortProperty that = (SortProperty) obj;
            return this.direction.equals(that.direction) && this.name.equals(that.name);
        }

        @Override
        public String toString() {
            return String.format("%s: %s", name, direction);
        }

    }

    /**
     * Sort direction: Ascending and Descending
     */
    public enum Direction {
        ASC,
        DESC;

        public boolean isAscending() {
            return this.equals(ASC);
        }
    }

}
