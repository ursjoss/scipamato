package ch.difty.scipamato.common.persistence.paging

import ch.difty.scipamato.common.persistence.paging.Sort.SortProperty
import java.io.Serializable

/**
 * Sort specification offering a list of [SortProperty] items, each providing a name
 * of the to be sorted property and the sort direction. This Sort implementation initially
 * was a simplified version of the spring data Sort class.
 */
class Sort : Iterable<SortProperty>, Serializable {

    private val sortProperties: MutableList<SortProperty>

    /**
     * Instantiate [Sort] with predefined [sortProperties]
     */
    constructor(sortProperties: List<SortProperty>) {
        require(sortProperties.isNotEmpty()) { "sortProperties can't be empty." }
        this.sortProperties = ArrayList(sortProperties.size)
        this.sortProperties.addAll(sortProperties)
    }

    /**
     * Instantiate [Sort] with various [propertyNames] all to be sorted in the same [direction]
     */
    constructor(direction: Direction, vararg propertyNames: String) {
        require(propertyNames.isNotEmpty()) { "propertyNames can't be empty." }
        sortProperties = ArrayList(propertyNames.size)
        propertyNames.forEach {
            sortProperties.add(SortProperty(it, direction))
        }
    }

    override fun iterator(): Iterator<SortProperty> = sortProperties.iterator()

    fun getSortPropertyFor(propertyName: String): SortProperty? = sortProperties.firstOrNull { it.name == propertyName }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (this === other) return true
        if (this.javaClass != other.javaClass) return false
        val that = other as Sort?
        return this.sortProperties == that!!.sortProperties
    }

    override fun hashCode(): Int {
        var result = 17
        result = 31 * result + sortProperties.hashCode()
        return result
    }

    override fun toString(): String = sortProperties.joinToString(",") { it.toString() }

    /**
     * Individual sort specification for a particular property, consisting of a
     * property name and a sort direction.
     */
    data class SortProperty(val name: String, val direction: Direction = DEFAULT_DIRECTION) : Serializable {

        override fun toString() = "$name: $direction"

        companion object {
            private const val serialVersionUID = 1L
            private val DEFAULT_DIRECTION = Direction.ASC
        }
    }

    /**
     * Sort direction: Ascending and Descending
     */
    enum class Direction {
        ASC, DESC;

        fun isAscending() = this == ASC
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
