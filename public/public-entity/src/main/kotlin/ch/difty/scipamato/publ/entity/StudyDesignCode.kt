package ch.difty.scipamato.publ.entity

/**
 * StudyDesignCode contains aggregated Codes of Code Class 5:
 *
 *  1. Experimental Studies: Codes 5A + 5B + 5C
 *  1. Epidemiological Studies: Codes 5E + 5F + 5G + 5H + 5I
 *  1. Overview Methodology: Codes 5U + 5M
 */
enum class StudyDesignCode(val id: Short) {
    EXPERIMENTAL(1.toShort()),
    EPIDEMIOLOGICAL(2.toShort()),
    OVERVIEW_METHODOLOGY(3.toShort()),
    ;

    companion object {
        private val ID2CODE: Map<Short, StudyDesignCode> = values().associateBy { it.id }
        fun of(id: Short): StudyDesignCode? = ID2CODE[id]
    }
}
