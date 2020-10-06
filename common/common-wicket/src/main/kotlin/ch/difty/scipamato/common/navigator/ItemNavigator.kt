package ch.difty.scipamato.common.navigator

import java.io.Serializable

/**
 * Implementations of this interface may manage a list of items (ordered,
 * unique, non-null). By default, the focus is on the first item. You can move
 * the focus to other items by explicitly setting it to any other existing item
 * or by advancing ([.next]) or retreating ([.previous]) item by
 * item.
 *
 * If no items have been set initially, [.getItemWithFocus] returns null,
 * all other commands return false or are ignored.
 *
 * @param [T] type of managed items
 */
interface ItemNavigator<T> : Serializable {
    /**
     * Initializes a new set of managed items. Must have at least one item.
     *
     * @param [items] list of items of type [T] or a subclass, must not be null nor empty.
     */
    fun initialize(items: List<T>)

    /**
     * Sets the focus to item. Ignores the command if the navigator has not been
     * initialized with at least one item.
     *
     * @param [item] the item the focus is set to - null if we want to unset the focus
     */
    fun setFocusToItem(item: T?)

    /**
     * @return the item with the focus (the first by default) or null if
     * the navigator has not been initialized with at least one item.
     */
    val itemWithFocus: T?

    /**
     * @return true if you can retreat the current item by one position.
     * False if we're already on the first item or if the navigator has not been
     * initialized with at least one item.
     */
    fun hasPrevious(): Boolean

    /**
     * Steps back one item - if possible.
     */
    fun previous()

    /**
     * @return `true` if you can advance the current item by one position.
     * False if we're already on the last item or if the navigator has not been
     * initialized with at least one item.
     */
    operator fun hasNext(): Boolean

    /**
     * Advances by one item - if possible.
     */
    operator fun next()

    /**
     * Does nothing if the handed over id is already present in the list of ids.
     * Otherwise puts the id to the head of the list.
     *
     * @param idCandidate the id candidate
     */
    fun setIdToHeadIfNotPresent(idCandidate: T)

    /**
     * Removes the id from the list (if present)
     *
     * @param id the id to be removed
     */
    fun remove(id: T)

    /**
     * @return true if the list was modified, false otherwise
     */
    val isModified: Boolean
}
