package ch.difty.scipamato.core.entity.search

import ch.difty.scipamato.core.entity.search.IntegerSearchTerm.MatchType

/**
 * Implementation of [AbstractSearchTerm] working with Integer fields. The
 * following [MatchType]s are implemented:
 *
 *  * **GREATER_THAN:** `>2014 `
 *  * **GREATER_OR_EQUAL:** `>=2014 `
 *  * **EXACT:** `2014 ` or `=2014 `
 *  * **LESS_OR_EQUAL:** `<=2014 `
 *  * **LESS_THAN:** `<2014 `
 *  * **RANGE:** `2014-2017 `
 *  * **MISSING:** the field has no value.
 *  * **PRESENT:** the field has any value.
 *  * **INCOMPLETE:** the search term ist not complete yet (partial entry).
 *
 * All rawValues and their individual parts are trimmed, so the following
 * examples are equally valid:
 *
 *  * `> 2014 `
 *  * `<= 2014 `
 *  * `2014  - 2017 `
 *
 * NOTE: The INCOMPLETE will not find any papers. It's more about gracefully handling
 * unfinished search terms. See https://github.com/ursjoss/scipamato/issues/84
 *
 * @author u.joss
 */
class IntegerSearchTerm(
    id: Long?,
    searchConditionId: Long?,
    fieldName: String,
    rawSearchTerm: String
) : AbstractSearchTerm(id, SearchTermType.INTEGER, searchConditionId, fieldName, rawSearchTerm) {

    constructor(
        fieldName: String,
        rawSearchTerm: String
    ) : this(null, fieldName, rawSearchTerm)

    constructor(
        searchConditionId: Long?,
        fieldName: String,
        rawSearchTerm: String
    ) : this(null, searchConditionId, fieldName, rawSearchTerm)

    val type: MatchType
    val value: Int
    val value2: Int

    enum class MatchType {
        EXACT, GREATER_THAN, GREATER_OR_EQUAL, LESS_THAN, LESS_OR_EQUAL, RANGE, MISSING, PRESENT, INCOMPLETE
    }

    init {
        val rst = rawSearchTerm.trim { it <= ' ' }
        if (
            rst.startsWith(Ops.RANGE.symbol) ||
            rst.endsWith(Ops.RANGE.symbol) ||
            Ops.values().any { it.symbol == rst }
        ) {
            type = MatchType.INCOMPLETE
            value = 0
            value2 = 0
        } else if ("""=""""" == rst || """""""" == rst) {
            type = MatchType.MISSING
            value = 0
            value2 = 0
        } else if (""">""""" == rst) {
            type = MatchType.PRESENT
            value = 0
            value2 = 0
        } else if (rst.startsWith(Ops.GREATER_THAN_OR_EQUAL.symbol)) {
            type = MatchType.GREATER_OR_EQUAL
            value = extractInteger(rst, COMP_SYMBOL_MAX_LENGTH)
            value2 = value
        } else if (rst.startsWith(Ops.GREATER_THAN.symbol)) {
            type = MatchType.GREATER_THAN
            value = extractInteger(rst, 1)
            value2 = value
        } else if (rst.startsWith(Ops.LESS_THAN_OR_EQUAL.symbol)) {
            type = MatchType.LESS_OR_EQUAL
            value = extractInteger(rst, COMP_SYMBOL_MAX_LENGTH)
            value2 = value
        } else if (rst.startsWith(Ops.LESS_THAN.symbol)) {
            type = MatchType.LESS_THAN
            value = extractInteger(rst, 1)
            value2 = value
        } else if (rst.startsWith(Ops.EQUAL.symbol)) {
            type = MatchType.EXACT
            value = extractInteger(rst, 1)
            value2 = value
        } else if (rst.contains(Ops.RANGE.symbol)) {
            val symbol = Ops.RANGE.symbol
            val indexOfSymbol = rst.indexOf(symbol)
            type = MatchType.RANGE
            value = rst.substring(0, indexOfSymbol).trim { it <= ' ' }.toInt()
            value2 = rst.substring(indexOfSymbol + 1).trim { it <= ' ' }.toInt()
        } else if (rst.trim { it <= ' ' }.toIntOrNull() != null) {
            value = rst.trim { it <= ' ' }.toInt()
            value2 = value
            type = MatchType.EXACT
        } else {
            type = MatchType.INCOMPLETE
            value = 1 // arbitrary value to make it different from the first block and mute the intellij warning)
            value2 = 1
        }
    }

    private enum class Ops(val symbol: String) {
        LESS_THAN("<"), LESS_THAN_OR_EQUAL("<="), EQUAL("="),
        GREATER_THAN_OR_EQUAL(">="), GREATER_THAN(">"), RANGE("-");
    }

    private fun extractInteger(rst: String, compSymbolMaxLength: Int): Int =
        rst.substring(compSymbolMaxLength).trim { it <= ' ' }.toInt()

    companion object {
        private const val serialVersionUID = 1L
        private const val COMP_SYMBOL_MAX_LENGTH = 2
    }
}