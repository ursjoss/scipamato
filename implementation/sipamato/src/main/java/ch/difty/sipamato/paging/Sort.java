package ch.difty.sipamato.paging;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.util.StringUtils;

import ch.difty.sipamato.lib.AssertAs;

/**
 * This Sort implementation initially was a simplified version of the spring data Sort class.
 *
 * @author u.joss
 */
public class Sort implements Iterable<ch.difty.sipamato.paging.Sort.Order>, Serializable {

    private static final long serialVersionUID = 1L;

    private final List<Order> orders;

    /**
     * Instantiate {@link Sort} with a predefined list of {@link Order}s
     * @param orders
     */
    public Sort(final List<Order> orders) {
        checkPreconditionsFor(orders);
        this.orders = new ArrayList<>(orders.size());
        this.orders.addAll(orders);
    }

    /**
     * Instantiate {@link Sort} with various sort properties all of the same {@link Direction}
     * @param direction
     * @param properties
     */
    public Sort(final Direction direction, final String... properties) {
        checkPreconditionsFor(properties);

        this.orders = new ArrayList<>(properties.length);
        for (final String prop : properties) {
            orders.add(new Order(direction, prop));
        }
    }

    private void checkPreconditionsFor(final List<Order> orders) {
        AssertAs.notNull(orders, "orders");
        if (orders.isEmpty()) {
            throw new IllegalArgumentException("orders can't be empty.");
        }
    }

    private void checkPreconditionsFor(final String... properties) {
        AssertAs.notNull(properties, "properties");
        if (properties.length == 0) {
            throw new IllegalArgumentException("properties can't be empty.");
        }
    }

    @Override
    public Iterator<Order> iterator() {
        return this.orders.iterator();
    }

    public Order getOrderFor(final String property) {
        for (final Order order : this.orders) {
            if (order.getProperty().equals(property)) {
                return order;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (this.getClass() != obj.getClass())
            return false;
        Sort that = (Sort) obj;
        return this.orders.equals(that.orders);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + orders.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return StringUtils.collectionToCommaDelimitedString(orders);
    }

    public static class Order implements Serializable {

        private static final long serialVersionUID = 1L;

        private static final Direction DEFAULT_DIRECTION = Direction.ASC;
        private final Direction direction;
        private final String property;

        public Order(final Direction direction, final String property) {
            this.direction = direction != null ? direction : DEFAULT_DIRECTION;
            this.property = AssertAs.notNull(property, "property");
        }

        public String getProperty() {
            return property;
        }

        public Sort.Direction getDirection() {
            return direction;
        }

        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result + direction.hashCode();
            result = 31 * result + property.hashCode();
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null)
                return false;
            if (this == obj)
                return true;
            if (this.getClass() != obj.getClass())
                return false;
            Order that = (Order) obj;
            return this.direction.equals(that.direction) && this.property.equals(that.property);
        }

        @Override
        public String toString() {
            return String.format("%s: %s", property, direction);
        }

    }

    public enum Direction {
        ASC,
        DESC;

        public boolean isAscending() {
            return this.equals(ASC);
        }
    }

}
