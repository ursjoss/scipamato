package ch.difty.sipamato.paging;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.difty.sipamato.lib.NullArgumentException;
import ch.difty.sipamato.paging.Sort.Direction;
import ch.difty.sipamato.paging.Sort.Order;

public class SortTest {

    private final List<Order> orders = new ArrayList<>(4);

    private Sort sort;

    @Before
    public void setUp() {
        orders.add(new Order(Direction.ASC, "a"));
        orders.add(new Order(Direction.DESC, "b"));
        orders.add(new Order(Direction.DESC, "c"));
        orders.add(new Order(Direction.ASC, "d"));

        sort = new Sort(orders);
    }

    @Test
    public void degenerateConstruction_withNullOrders_throws() {
        try {
            new Sort(null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("orders must not be null.");
        }
    }

    @Test
    public void degenerateConstruction_withEmptyOrders_throws() {
        try {
            new Sort(Collections.emptyList());
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(IllegalArgumentException.class).hasMessage("orders can't be empty.");
        }
    }

    @Test
    public void degenerateConstruction_withNullProperties_throws() {
        try {
            new Sort(Direction.ASC, (String[]) null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("properties must not be null.");
        }
    }

    @Test
    public void degenerateConstruction_withEmptyProperties_throws() {
        try {
            new Sort(Direction.ASC, new String[] {});
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(IllegalArgumentException.class).hasMessage("properties can't be empty.");
        }
    }

    private void assertProperties(Direction dir, String[] props) {
        final Sort sort = new Sort(dir, props);

        assertThat(sort.iterator()).hasSize(props.length);

        final Iterator<Order> it = sort.iterator();

        while (it.hasNext()) {
            final Order o = it.next();
            assertThat(o.getDirection()).isEqualTo(dir);
        }
    }

    @Test
    public void creatingSortForThreeAscendingProperties_returnsIteratorForAllThreeElementsWithAscendingOrder() {
        assertProperties(Direction.ASC, new String[] { "a", "b", "c" });
    }

    @Test
    public void cratingSortForFiveDescendingProperties_returnsIteratorForAllFiveElementsWithDescendingOrder() {
        assertProperties(Direction.DESC, new String[] { "a", "b", "c", "d", "e" });
    }

    @Test
    public void creatingSortForFourOrdersWithDifferentSortDirections() {

        final Iterator<Order> it = sort.iterator();
        assertOrder(it, Direction.ASC, "a");
        assertOrder(it, Direction.DESC, "b");
        assertOrder(it, Direction.DESC, "c");
        assertOrder(it, Direction.ASC, "d");
        assertThat(it.hasNext()).isFalse();
    }

    private void assertOrder(Iterator<Order> it, Direction dir, String p) {
        Order o = it.next();
        assertThat(o.getDirection()).isEqualTo(dir);
        assertThat(o.getProperty()).isEqualTo(p);
    }

    @Test
    public void gettingOrderFor_withNullProperty_returnsNull() {
        assertThat(sort.getOrderFor(null)).isNull();
    }

    @Test
    public void gettingOrderFor_nonExistingProperty_returnsNull() {
        String p = "x";
        assertThat(orders).extracting("property").doesNotContain(p);
        assertThat(sort.getOrderFor(p)).isNull();
    }

    @Test
    public void gettingOrderFor_existingProperty_returnsRespectiveOrder() {
        String p = "c";
        assertThat(orders).extracting("property").contains(p);
        assertThat(sort.getOrderFor(p).getProperty()).isEqualTo(p);
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
    public void orderWithNullDirection_isAscending() {
        assertThat(new Order(null, "foo").getDirection()).isEqualTo(Direction.ASC);
    }

    @Test
    public void testingToString() {
        assertThat(sort.toString()).isEqualTo("a: ASC,b: DESC,c: DESC,d: ASC");
    }

    @Test
    public void sortEqualityTests() {
        assertThat(sort.equals(null)).isFalse();
        assertThat(sort.equals(new String())).isFalse();
        assertThat(sort.equals(sort)).isTrue();
        assertThat(sort.equals(new Sort(orders))).isTrue();

        List<Order> orders2 = new ArrayList<>();
        orders2.add(new Order(Direction.ASC, "a"));
        orders2.add(new Order(Direction.DESC, "b"));
        orders2.add(new Order(Direction.DESC, "c"));
        assertThat(sort.equals(new Sort(orders2))).isFalse();
        assertThat(sort.hashCode()).isNotEqualTo(new Sort(orders2).hashCode());

        orders2.add(new Order(Direction.ASC, "d"));
        assertThat(sort.equals(new Sort(orders2))).isTrue();
        assertThat(sort.hashCode()).isEqualTo(new Sort(orders2).hashCode());
    }

    @Test
    public void orerEqualityTests() {
        Order o1 = new Order(Direction.DESC, "foo");

        assertThat(o1.equals(null)).isFalse();
        assertThat(o1.equals(new String())).isFalse();
        assertThat(o1.equals(o1)).isTrue();
        assertThat(o1.equals(new Order(Direction.DESC, "foo"))).isTrue();
        assertThat(o1.equals(new Order(Direction.ASC, "foo"))).isFalse();
        assertThat(o1.equals(new Order(Direction.DESC, "bar"))).isFalse();

    }
}
