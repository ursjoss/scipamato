package ch.difty.scipamato.core.sync.code

interface CodeAggregator {
    fun setInternalCodes(internalCodes: List<String>)
    fun load(codes: Array<String>)
    val aggregatedCodes: Array<String>
    val codesPopulation: Array<Short>
    val codesStudyDesign: Array<Short>
}
