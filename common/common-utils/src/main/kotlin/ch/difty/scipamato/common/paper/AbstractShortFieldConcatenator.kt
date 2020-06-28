package ch.difty.scipamato.common.paper

/**
 * Abstract base class for ShortFieldConcatenator implementations, enforcing the same short fields to be concatenated
 * into the respective main fields in different places in the application.
 *
 * Each of the three methods (for method, population and result) has two variants, one passing in the values only,
 * pre-pending an English static label to each value. The other one with dynamic labels that can be localized according
 * to the users language preference.
 */
@Suppress("LongParameterList", "kotlin:S107", "UnnecessaryAbstractClass")
abstract class AbstractShortFieldConcatenator protected constructor(private val withNewLine: Boolean) {

    fun methodsFrom(
        method: String?,
        methodStudyDesign: String?,
        methodOutcome: String?,
        populationPlace: String?,
        exposurePollutant: String?,
        exposureAssessment: String?,
        methodStatistics: String?,
        methodConfounders: String?
    ) = methodsFrom(
        method,
        Tuple("Study Design", methodStudyDesign),
        Tuple("Outcome", methodOutcome),
        Tuple("Place", populationPlace),
        Tuple("Pollutant", exposurePollutant),
        Tuple("Exposure Assessment", exposureAssessment),
        Tuple("Statistical Method", methodStatistics),
        Tuple("Confounders", methodConfounders)
    )

    fun methodsFrom(
        method: String?,
        methodStudyDesign: Tuple?,
        methodOutcome: Tuple?,
        populationPlace: Tuple?,
        exposurePollutant: Tuple?,
        exposureAssessment: Tuple?,
        methodStatistics: Tuple?,
        methodConfounders: Tuple?
    ) = determineAppropriate(
        Tuple(null, method),
        listOf(
            methodStudyDesign,
            methodOutcome,
            populationPlace,
            exposurePollutant,
            exposureAssessment,
            methodStatistics,
            methodConfounders
        )
    )

    fun populationFrom(
        population: String?,
        populationPlace: String?,
        populationParticipants: String?,
        populationDuration: String?
    ) = populationFrom(
        population,
        Tuple("Place", populationPlace),
        Tuple("Participants", populationParticipants),
        Tuple("Study Duration", populationDuration)
    )

    fun populationFrom(
        population: String?,
        populationPlace: Tuple?,
        populationParticipants: Tuple?,
        populationDuration: Tuple?
    ) = determineAppropriate(
        Tuple(null, population),
        listOf(
            populationPlace,
            populationParticipants,
            populationDuration
        )
    )

    fun resultFrom(
        result: String?,
        resultMeasuredOutcome: String?,
        resultExposureRange: String?,
        resultEffectEstimate: String?,
        conclusion: String?
    ) = resultFrom(
        result,
        Tuple("Measured Outcome", resultMeasuredOutcome),
        Tuple("Exposure (Range)", resultExposureRange),
        Tuple("Effect Estimate", resultEffectEstimate),
        Tuple("Conclusion", conclusion)
    )

    fun resultFrom(
        result: String?,
        resultMeasuredOutcome: Tuple?,
        resultExposureRange: Tuple?,
        resultEffectEstimate: Tuple?,
        conclusion: Tuple?
    ) = determineAppropriate(
        Tuple(null, result),
        listOf(
            resultMeasuredOutcome,
            resultExposureRange,
            resultEffectEstimate,
            conclusion
        )
    )

    /**
     * Use the [mainField] (with precedence) if not blank. Concatenate all other fields passed in via [shortFields]
     * (separated by ' - ') otherwise. Working with tuples of a label (English only) and the value.
     */
    private fun determineAppropriate(mainField: Tuple, shortFields: List<Tuple?>): String =
        mainField.content ?: concat(shortFields)

    private fun concat(tuples: List<Tuple?>): String {
        val sb = StringBuilder()
        tuples.forEach { t ->
            if (t?.content?.isNotBlank() == true) {
                if (sb.isNotEmpty()) sb.appendSeparator()
                t.label?.let { sb.append(t.label).append(": ") }
                sb.append(t.content)
            }
        }

        return sb.toString()
    }

    private fun StringBuilder.appendSeparator() = if (withNewLine) append('\n') else append(" / ")

    data class Tuple(val label: String?, val content: String?)
}
