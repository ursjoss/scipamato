package ch.difty.scipamato.common.navigator

import java.util.ArrayList

/**
 * [ItemNavigator] implementation managing long items.
 */
class LongNavigator : ItemNavigator<Long> {

    private var items: NavigatedItems<Long>? = null
    override var isModified = false
        private set

    override fun initialize(items: List<Long>) {
        if (items.isNotEmpty())
            this.items = NavigatedList(items)
        isModified = false
    }

    override fun setFocusToItem(item: Long?) {
        if (items != null && item != null)
            items?.setFocusToItem(item)
    }

    override val itemWithFocus: Long?
        get() = items?.itemWithFocus

    override fun hasPrevious(): Boolean = items?.hasPrevious() ?: false

    override fun hasNext(): Boolean = items?.hasNext() ?: false

    override fun previous() {
        if (hasPrevious()) items?.previous()
    }

    override fun next() {
        if (hasNext()) items?.next()
    }

    override fun setIdToHeadIfNotPresent(idCandidate: Long) {
        if (items?.containsId(idCandidate) != true) {
            val newItems: MutableList<Long> = ArrayList()
            newItems.add(idCandidate)
            items?.let { newItems.addAll(it.items) }
            initialize(newItems)
            setFocusToItem(idCandidate)
        }
    }

    override fun remove(id: Long) {
        if (items?.containsId(id) != true) return
        if (id == items?.itemWithFocus) moveFocus()
        val focus = items?.itemWithFocus
        val newItems = items?.without(id)
        if (newItems?.isEmpty() == true) items = null else initialize(newItems!!)
        isModified = true
        setFocusToItem(focus)
    }

    private fun moveFocus() {
        if (items?.hasNext() == true) items?.next() else items?.previous()
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
