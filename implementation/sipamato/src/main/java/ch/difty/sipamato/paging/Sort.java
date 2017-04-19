package ch.difty.sipamato.paging;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

@Deprecated
public class Sort implements Iterable<ch.difty.sipamato.paging.Sort.Order>, Serializable {

    public Sort(List<Order> orders) {
        // TODO Auto-generated constructor stub
    }

    public Sort(Direction direction, String[] properties) {
        // TODO Auto-generated constructor stub
    }

    @Override
    public Iterator<Order> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getOrderFor(String string) {
        // TODO Auto-generated method stub
        return null;
    }

    public static class Order implements Serializable {

        private static final long serialVersionUID = 1L;

        public Order(Direction desc, String string) {
            // TODO Auto-generated constructor stub
        }

        public String getProperty() {
            // TODO Auto-generated method stub
            return null;
        }

        public Sort.Direction getDirection() {
            // TODO Auto-generated method stub
            return null;
        }

    }

    public static enum Direction {
        ASC,
        DESC;
    }

}
