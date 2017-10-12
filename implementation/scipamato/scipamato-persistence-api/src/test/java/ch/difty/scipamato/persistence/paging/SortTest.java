package ch.difty.scipamato.persistence.paging;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.difty.scipamato.NullArgumentException;
import ch.difty.scipamato.persistence.paging.Sort.Direction;
import ch.difty.scipamato.persistence.paging.Sort.SortProperty;

public class SortTest {

    private final List<SortProperty> sortProperties = new ArrayList<>(4);

    private Sort sort;

    @Before
    public void setUp() {
        sortProperties.add(new SortProperty("a", Direction.ASC));
        sortProperties.add(new SortProperty("b", Direction.DESC));
        sortProperties.add(new SortProperty("c", Direction.DESC));
        sortProperties.add(new SortProperty("d", Direction.ASC));

        sort = new Sort(sortProperties);
    }

    @Test
    public void degenerateConstruction_withNullSortProperties_throws() {
        try {
            new Sort(null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("sortProperties must not be null.");
        }
    }

    @Test
    public void degenerateConstruction_withNoSortProperties_throws() {
        try {
            new Sort(Collections.emptyList());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(IllegalArgumentException.class).hasMessage("sortProperties can't be empty.");
        }
    }

    @Test
    public void degenerateConstruction_withNullPropertyNames_throws() {
        try {
            new Sort(Direction.ASC, (String[]) null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("propertyNames must not be null.");
        }
    }

    @Test
    public void degenerateConstruction_withEmptyPropertyNames_throws() {
        try {
            new Sort(Direction.ASC, new String[] {});
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(IllegalArgumentException.class).hasMessage("propertyNames can't be empty.");
        }
    }

    private void assertSortProperty(Direction dir, String[] propertyNames) {
        final Sort sort = new Sort(dir, propertyNames);

        assertThat(sort.iterator()).hasSize(propertyNames.length);

        final Iterator<SortProperty> it = sort.iterator();

        while (it.hasNext()) {
            final SortProperty sp = it.next();
            assertThat(sp.getDirection()).isEqualTo(dir);
        }
    }

    @Test
    public void creatingSortForThreeAscendingProperties_returnsIteratorForAllThreePropertiesInAscendingOrder() {
        assertSortProperty(Direction.ASC, new String[] { "a", "b", "c" });
    }

    @Test
    public void cratingSortForFiveDescendingProperties_returnsIteratorForAllFiveElementsWithDescendingOrder() {
        assertSortProperty(Direction.DESC, new String[] { "a", "b", "c", "d", "e" });
    }

    @Test
    public void creatingSortForFourSortPropertiesWithDifferentSortDirections() {
        final Iterator<SortProperty> it = sort.iterator();
        assertSortProperty(it, Direction.ASC, "a");
        assertSortProperty(it, Direction.DESC, "b");
        assertSortProperty(it, Direction.DESC, "c");
        assertSortProperty(it, Direction.ASC, "d");
        assertThat(it.hasNext()).isFalse();
    }

    private void assertSortProperty(Iterator<SortProperty> it, Direction dir, String p) {
        SortProperty sp = it.next();
        assertThat(sp.getDirection()).isEqualTo(dir);
        assertThat(sp.getName()).isEqualTo(p);
    }

    @Test
    public void gettingSortPropertyFor_withNullName_returnsNull() {
        assertThat(sort.getSortPropertyFor(null)).isNull();
    }

    @Test
    public void gettingSortPropertyFor_nonExistingName_returnsNull() {
        String p = "x";
        assertThat(sortProperties).extracting("name").doesNotContain(p);
        assertThat(sort.getSortPropertyFor(p)).isNull();
    }

    @Test
    public void gettingSortPropertyFor_existingName_returnsRespectiveSortProperty() {
        String p = "c";
        assertThat(sortProperties).extracting("name").contains(p);
        assertThat(sort.getSortPropertyFor(p).getName()).isEqualTo(p);
    }

    @Test
    public void directionAsc_isAscending() {
        assertThat(Direction.ASC.isAscending()).isTrue();
    }

    @Test
    public void directionDesc_isNotAscending() {
        assertThat(Direction.DESC.isAscending()).isFalse();
    }

    @Test
    public void SortPropertyWithNullDirection_isAscending() {
        assertThat(new SortProperty("foo", null).getDirection()).isEqualTo(Direction.ASC);
    }

    @Test
    public void testingToString() {
        assertThat(sort.toString()).isEqualTo("a: ASC,b: DESC,c: DESC,d: ASC");
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void sortEqualityTests() {
        assertThat(sort.equals(null)).isFalse();
        assertThat(sort.equals(new String())).isFalse();
        assertThat(sort.equals(sort)).isTrue();
        assertThat(sort.equals(new Sort(sortProperties))).isTrue();

        List<SortProperty> sortPropertys2 = new ArrayList<>();
        sortPropertys2.add(new SortProperty("a", Direction.ASC));
        sortPropertys2.add(new SortProperty("b", Direction.DESC));
        sortPropertys2.add(new SortProperty("c", Direction.DESC));
        assertThat(sort.equals(new Sort(sortPropertys2))).isFalse();
        assertThat(sort.hashCode()).isNotEqualTo(new Sort(sortPropertys2).hashCode());

        sortPropertys2.add(new SortProperty("d", Direction.ASC));
        assertThat(sort.equals(new Sort(sortPropertys2))).isTrue();
        assertThat(sort.hashCode()).isEqualTo(new Sort(sortPropertys2).hashCode());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void sortPropertyEqualityTests() {
        SortProperty sf1 = new SortProperty("foo", Direction.DESC);

        assertThat(sf1.equals(null)).isFalse();
        assertThat(sf1.equals(new String())).isFalse();
        assertThat(sf1.equals(sf1)).isTrue();
        assertThat(sf1.equals(new SortProperty("foo", Direction.DESC))).isTrue();
        assertThat(sf1.equals(new SortProperty("foo", Direction.ASC))).isFalse();
        assertThat(sf1.equals(new SortProperty("bar", Direction.DESC))).isFalse();

    }
}
