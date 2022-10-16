@file:Suppress("MagicNumber")

package ch.difty.scipamato.core.sync.code

import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

private const val AGGR_5ABC = "5abc"

/**
 * HARDCODED consider moving aggregation into some table in scipamato-core (see also CodeSyncConfig#selectSql)
 */

private val codeAggregation: Map<String, String> = setOf("5A", "5B", "5C").associateWith { AGGR_5ABC }

private val populationCodeMapper: Map<String, Short> = (
    setOf("3A", "3B").associateWith { 1 } +
        setOf("3C").associateWith { 2 }
    ).map { it.key to it.value.toShort() }.toMap()

private val studyDesignCodeMapper: Map<String, Short> = (
    setOf(AGGR_5ABC).associateWith { 1 } +
        setOf("5E", "5F", "5G", "5H", "5I").associateWith { 2 } +
        setOf("5U", "5M").associateWith { 3 }
    ).map { it.key to it.value.toShort() }.toMap()

/**
 * The [HidingInternalsCodeAggregator] has the purpose of
 *
 *  * Providing aggregated codes by
 *    * Enriching codes with aggregated codes
 *    * Filtering out internal codes
 *
 *  * providing the aggregated
 *    * codesPopulation values
 *    * codesStudyDesign values
 */
@Component
@Scope("prototype")
class HidingInternalsCodeAggregator : CodeAggregator {
    private val _internalCodes: MutableList<String> = ArrayList()
    private val _codes: MutableList<String> = ArrayList()
    private val _codesPopulation: MutableList<Short> = ArrayList()
    private val _codesStudyDesign: MutableList<Short> = ArrayList()

    override fun setInternalCodes(internalCodes: List<String>) {
        this._internalCodes.clear()
        this._internalCodes.addAll(internalCodes)
    }

    override fun load(codes: Array<String>) {
        clearAll()
        this._codes.addAll(aggregateCodes(codes))
        _codesPopulation.addAll(gatherCodesPopulation())
        _codesStudyDesign.addAll(gatherCodesStudyDesign())
    }

    private fun clearAll() {
        _codes.clear()
        _codesPopulation.clear()
        _codesStudyDesign.clear()
    }

    private fun aggregateCodes(codeArray: Array<String>): List<String> =
        filterAndEnrich(codeArray).sorted()

    private fun filterAndEnrich(codeArray: Array<String>): List<String> =
        codeArray
            .map { codeAggregation[it] ?: it }
            .filterNot { it in _internalCodes }
            .distinct()

    private fun gatherCodesPopulation(): List<Short> =
        _codes.mapNotNull { populationCodeMapper[it] }.distinct()

    private fun gatherCodesStudyDesign(): List<Short> =
        _codes.mapNotNull { studyDesignCodeMapper[it] }.distinct()

    override val aggregatedCodes get() = _codes.toTypedArray()

    override val codesPopulation get() = _codesPopulation.toTypedArray()

    override val codesStudyDesign get() = _codesStudyDesign.toTypedArray()
}
