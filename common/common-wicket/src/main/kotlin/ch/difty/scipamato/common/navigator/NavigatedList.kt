package ch.difty.scipamato.common.navigator

import java.io.Serializable
import java.util.concurrent.atomic.AtomicInteger

/**
 * List based implementation of the [NavigatedItems] interface.
 *
 * @param [T] type of the items that are managed. Must implement [Serializable].
 */
internal class NavigatedList<T : Serializable>(
    initialItems: Collection<T>,
) : NavigatedItems<T> {

    override val items: List<T> = initialItems.distinct()
    override val itemWithFocus: T get() = items[index.get()]

    private val index = AtomicInteger()

    init {
        require(initialItems.isNotEmpty()) { "items must not be empty" }
    }

    override fun setFocusToItem(item: T) {
        items.indexOf(item).apply {
            require(this != -1) {
                "Cannot set focus to item that is not part of the managed list (item $item)."
            }
        }.also {
            index.set(it)
        }
    }

    override fun size(): Int = items.size
    override fun containsId(id: T): Boolean = items.contains(id)
    override fun without(id: T): List<T> = items.filter { it != id }

    override fun hasPrevious(): Boolean = index.get() > 0
    override fun hasNext(): Boolean = index.get() < items.size - 1
    override fun next() {
        if (hasNext()) index.incrementAndGet()
    }

    override fun previous() {
        if (hasPrevious()) index.decrementAndGet()
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
