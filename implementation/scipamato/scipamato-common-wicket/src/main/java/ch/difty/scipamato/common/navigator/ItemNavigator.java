package ch.difty.scipamato.common.navigator;

import java.io.Serializable;
import java.util.List;

/**
 * Implementations of this interface may manage a list of items (ordered,
 * unique, non-null). By default, the focus is on the first item. You can move
 * the focus to other items by explicitly setting it to any other existing item
 * or by advancing ({@link #next()}) or retreating ({@link #previous()}) item by
 * item.
 * <p>
 * If no items have been set initially, {@link #getItemWithFocus} returns null,
 * all other commands return false or are ignored.
 *
 * @param <T>
 *     type of managed items
 * @author u.joss
 */
public interface ItemNavigator<T> extends Serializable {

    /**
     * Initializes a new set of managed items. Must have at least one item.
     *
     * @param items
     *     list of items of type {@code T} or a subclass, must not be null
     *     nor empty.
     */
    void initialize(List<? extends T> items);

    /**
     * Sets the focus to item. Ignores the command if the navigator has not been
     * initialized with at least one item.
     *
     * @param item
     *     the item the focus is set to
     */
    void setFocusToItem(T item);

    /**
     * @return the item with the focus (the first by default) or {@literal null} if
     *     the navigator has not been initialized with at least one item.
     */
    T getItemWithFocus();

    /**
     * @return true if you can retreat the current item by one position. False if
     *     we're already on the first item or if the navigator has not been
     *     initialized with at least one item.
     */
    boolean hasPrevious();

    /**
     * Steps back one item - if possible.
     */
    void previous();

    /**
     * @return true if you can advance the current item by one position. False if
     *     we're already on the last item or if the navigator has not been
     *     initialized with at least one item.
     */
    boolean hasNext();

    /**
     * Advances by one item - if possible.
     */
    void next();

    /**
     * Does nothing if the handed over id is already present in the list of ids.
     * Otherwise puts the id to the head of the list.
     *
     * @param idCandidate
     *     the id candidate
     */
    void setIdToHeadIfNotPresent(T idCandidate);
}
