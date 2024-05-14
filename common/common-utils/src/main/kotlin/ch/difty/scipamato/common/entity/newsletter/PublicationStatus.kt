package ch.difty.scipamato.common.entity.newsletter

/**
 * Newsletter specific Publication Status
 */
enum class PublicationStatus(val id: Int, val description: String) {
    WIP(0, "in progress"),
    PUBLISHED(1, "published"),
    CANCELLED(-1, "cancelled");

    val isInProgress: Boolean get() = WIP == this

    companion object {
        fun byId(id: Int): PublicationStatus {
            for (t in entries)
                if (t.id == id) return t
            throw IllegalArgumentException("id $id is not supported")
        }
    }
}
