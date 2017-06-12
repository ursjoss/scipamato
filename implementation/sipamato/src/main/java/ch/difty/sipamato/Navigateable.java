package ch.difty.sipamato;

import java.io.Serializable;
import java.util.List;

/**
 * Implementations manage an immutable ordered collection of unique non-null items (nulls or duplicates are ignored).
 * It maintains a reference to the current item, which can be set but is on the first item by default.<p>
 *
 * Two methods {@code advance} and {@code retreat} allow navigating within the list. Advancing beyond the last
 * item leaves the current item on the last item. Similarly, retreating beyond the first item leaves the current
 * item on the first item.
 *
 * @author u.joss
 *
 * @param <T> the type of the managed items
 */
public interface Navigateable<T> extends Serializable {

    /**
     * @return the number of items managed
     */
    int size();

    /**
     * @return the managed items
     */
    List<T> getItems();

    /**
     * @return the current item
     */
    T getCurrentItem();

    /**
     * Sets the current item
     * @param item
     */
    void setCurrentItem(T item);

    /**
     * Advances the current item by one position (unless the current item is the last item)
     */
    void advance();

    /**
     * Retreats the current item by one position (unless the current item is the first item)
     */
    void retreat();

}
