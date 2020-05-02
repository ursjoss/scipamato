package ch.difty.scipamato.core.sync.code

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class HidingInternalsCodeAggregatorTest {

    private val ca = HidingInternalsCodeAggregator()

    @ParameterizedTest(name = "{index}: code {0} is aggregated to {1}")
    @MethodSource("paramsCodeAggregation")
    fun gettingAggregatedCodes(input: Array<String>, codes: Array<String>?) {
        val internals = arrayOf(
            "1N", "1U", "1Z", "3U", "3Z", "4U", "4X", "4Y", "4Z",
            "5A", "5B", "5C", "5D", "5K", "6P", "6Z", "7M", "7Z", "8Z"
        )
        ca.setInternalCodes(listOf(*internals))
        ca.load(input)
        ca.aggregatedCodes shouldBeEqualTo codes
    }

    @ParameterizedTest(name = "{index}: code array {0} results in code population {1}")
    @MethodSource("paramsCodePopulation")
    fun gettingCodePopulation(input: Array<String>, codesPopulation: Array<Short>?) {
        ca.load(input)
        ca.codesPopulation shouldBeEqualTo codesPopulation
    }

    @ParameterizedTest(name = "{index}: code array {0} results in code study design {1}")
    @MethodSource("paramsCodeStudyDesign")
    fun gettingCodeStudyDesign(input: Array<String>, codesStudyDesign: Array<Short>?) {
        ca.load(input)
        ca.codesStudyDesign shouldBeEqualTo codesStudyDesign
    }

    @ParameterizedTest(name =
    "{index}: code array {0} results aggregated codes {1}, codesPopulation {2} and codesStudyDesign {3}"
    )
    @MethodSource("paramsAll")
    fun gettingAllCodeTypes(
        input: Array<String>,
        codes: Array<String>,
        codesPopulation: Array<Short>?,
        codesStudyDesign: Array<Short>?
    ) {
        val internals = arrayOf(
            "1N", "1U", "1Z", "3U", "3Z", "4U", "4X", "4Y", "4Z",
            "5A", "5B", "5C", "5D", "5K", "6P", "6Z", "7M", "7Z", "8Z"
        )
        ca.setInternalCodes(listOf(*internals))
        ca.load(input)
        ca.aggregatedCodes shouldBeEqualTo codes
        ca.codesPopulation shouldBeEqualTo codesPopulation
        ca.codesStudyDesign shouldBeEqualTo codesStudyDesign
    }

    @Suppress("unused")
    companion object {
        private const val P1: Short = 1
        private const val P2: Short = 2
        private const val S1: Short = 1
        private const val S2: Short = 2
        private const val S3: Short = 3

        /*
         * input           String[]: the code array to be loaded
         * aggregatedCodes String[]: the expected resulting array of (partially) aggregated codes
         */
        @JvmStatic
        private fun paramsCodeAggregation() = listOf(
            // can handle empty input
            Arguments.of(arrayOf<String>(), arrayOf<String>()),

            Arguments.of(arrayOf("1A", "2B"), arrayOf("1A", "2B")),

            // aggregation: 5A/B/C -> 5abc
            Arguments.of(arrayOf("5A"), arrayOf("5abc")),
            Arguments.of(arrayOf("5B"), arrayOf("5abc")),
            Arguments.of(arrayOf("5C"), arrayOf("5abc")),
            Arguments.of(arrayOf("5A", "5B"), arrayOf("5abc")),
            Arguments.of(arrayOf("5A", "5C"), arrayOf("5abc")),
            Arguments.of(arrayOf("5A", "5B", "5C"), arrayOf("5abc")),

            Arguments.of(arrayOf("5A", "5B", "5C"), arrayOf("5abc")),

            // filtering out internal codes
            Arguments.of(arrayOf("1A", "1N", "3B", "3Z", "5B", "8Z"), arrayOf("1A", "3B", "5abc"))
        )

        /*
         * input          String[]: the code array to be loaded
         * codesPopulation Short[]: the expected resulting array of code ids for codesPopulation
         */
        @JvmStatic
        private fun paramsCodePopulation() = listOf(
            // can handle empty input
            Arguments.of(arrayOf<String>(), arrayOf<Short>()),

            // irrelevant codes
            Arguments.of(arrayOf("1A", "2B"), arrayOf<Short>()),

            // Codes Population: 3A/B -> 1, 3C -> 2
            Arguments.of(arrayOf("3A"), arrayOf(P1)),
            Arguments.of(arrayOf("3B"), arrayOf(P1)),
            Arguments.of(arrayOf("3C"), arrayOf(P2)),
            Arguments.of(arrayOf("3A", "3B"), arrayOf(P1)),
            Arguments.of(arrayOf("3A", "3B", "3C"), arrayOf(P1, P2)),
            Arguments.of(arrayOf("3A", "3C"), arrayOf(P1, P2)),
            Arguments.of(arrayOf("3B", "3C"), arrayOf(P1, P2))
        )

        /*
         * input           String[]: the code array to be loaded
         * codesStudyDesign Short[]: the expected resulting array of code ids for codesStudyDesign
         */
        @JvmStatic
        private fun paramsCodeStudyDesign() = listOf(
            // can handle empty input
            Arguments.of(arrayOf<String>(), arrayOf<Short>()),

            Arguments.of(arrayOf("1A", "2B"), arrayOf<Short>()),

            // Codes Study Design: 5A/B/C -> 1, 5E/F/G/H/I -> 2, 5U/M -> 3
            Arguments.of(arrayOf("5A"), arrayOf(S1)),
            Arguments.of(arrayOf("5B"), arrayOf(S1)),
            Arguments.of(arrayOf("5C"), arrayOf(S1)),
            Arguments.of(arrayOf("5A", "5B"), arrayOf(S1)),
            Arguments.of(arrayOf("5A", "5C"), arrayOf(S1)),
            Arguments.of(arrayOf("5B", "5C"), arrayOf(S1)),
            Arguments.of(arrayOf("5A", "5B", "5C"), arrayOf(S1)),

            Arguments.of(arrayOf("5E"), arrayOf(S2)),
            Arguments.of(arrayOf("5F"), arrayOf(S2)),
            Arguments.of(arrayOf("5G"), arrayOf(S2)),
            Arguments.of(arrayOf("5H"), arrayOf(S2)),
            Arguments.of(arrayOf("5I"), arrayOf(S2)),
            Arguments.of(arrayOf("5E", "5F", "5G", "5H", "5I"), arrayOf(S2)),

            Arguments.of(arrayOf("5U"), arrayOf(S3)),
            Arguments.of(arrayOf("5M"), arrayOf(S3)),
            Arguments.of(arrayOf("5U", "5M"), arrayOf(S3)),

            // combine
            Arguments.of(arrayOf("3A", "3C", "5C", "5F", "5U", "5M"), arrayOf(S1, S2, S3))
        )

        /*
         * input           String[]: the code array to be loaded
         * codes           String[]: the expected resulting array of (partially) aggregated codes
         * codesPopulation  Short[]: the expected resulting array of code ids for codesPopulation
         * codesStudyDesign Short[]: the expected resulting array of code ids for codesStudyDesign
         */
        @JvmStatic
        private fun paramsAll() = listOf(
            // can handle empty input
            Arguments.of(arrayOf<String>(), arrayOf<String>(), arrayOf<Short>(), arrayOf<Short>()),

            Arguments.of(arrayOf("1A", "2B"), arrayOf("1A", "2B"), arrayOf<Short>(), arrayOf<Short>()),

            // aggregation: 5A/B/C -> 5abc
            Arguments.of(arrayOf("5A"), arrayOf("5abc"), arrayOf<Short>(), arrayOf(S1)),
            Arguments.of(arrayOf("3A", "3B", "3C", "3Z", "5A", "5B", "5H", "5M"),
                arrayOf("3A", "3B", "3C", "5H", "5M", "5abc"), arrayOf(P1, P2),
                arrayOf(S1, S2, S3))
        )
    }
}
