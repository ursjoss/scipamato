package ch.difty.scipamato.publ.entity

/**
 * PopulationCode contains aggregated Codes of Code Class 3:
 *
 *  1. Children: Codes 3A + 3B
 *  1. Adults: Codes 3C
 */
enum class PopulationCode(val id: Short) {
    CHILDREN(1.toShort()),
    ADULTS(2.toShort()),
    ;

    companion object {
        private val ID2CODE: Map<Short, PopulationCode> = entries.associateBy { it.id }
        fun of(id: Short): PopulationCode? = ID2CODE[id]
    }
}
