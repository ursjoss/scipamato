package ch.difty.scipamato.common.navigator

import java.io.Serializable

/**
 * Implementations manage an immutable ordered collection of unique non-null
 * items (nulls or duplicates are ignored). It maintains a reference to the
 * current item, which can be set but is on the first item by default.
 *
 * Two methods `advance` and `retreat` allow navigating within the list.
 * Advancing beyond the last item leaves the focus on the last tem.
 * Similarly, retreating beyond the first item leaves the current item on the first item.
 *
 * @param [T] the type of the managed items
 */
internal interface NavigatedItems<T> : Serializable {
    fun size(): Int
    val items: List<T>
    val itemWithFocus: T
    fun setFocusToItem(item: T)
    fun hasPrevious(): Boolean
    fun previous()
    operator fun hasNext(): Boolean
    operator fun next()
    fun containsId(id: T): Boolean
    fun without(id: T): List<T>
}
