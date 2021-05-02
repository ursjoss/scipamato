package ch.difty.scipamato.core.sync.code

import org.amshove.kluent.shouldBeEqualTo
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

private const val P1: Short = 1
private const val P2: Short = 2
private const val S1: Short = 1
private const val S2: Short = 2
private const val S3: Short = 3

object HidingInternalsCodeAggregatorSpec : Spek({

    val ca by memoized { HidingInternalsCodeAggregator() }

    describe("aggregatedCodes") {
        val internals = listOf(
            "1N", "1U", "1Z", "3U", "3Z", "4U", "4X", "4Y", "4Z",
            "5A", "5B", "5C", "5D", "5K", "6P", "6Z", "7M", "7Z", "8Z"
        )

        mapOf(
            // can handle empty input
            emptyArray<String>() to emptyArray(),

            arrayOf("1A", "2B") to arrayOf("1A", "2B"),

            // aggregation: 5A/B/C -> 5abc
            arrayOf("5A") to arrayOf("5abc"),
            arrayOf("5B") to arrayOf("5abc"),
            arrayOf("5C") to arrayOf("5abc"),
            arrayOf("5A", "5B") to arrayOf("5abc"),
            arrayOf("5A", "5C") to arrayOf("5abc"),
            arrayOf("5A", "5B", "5C") to arrayOf("5abc"),

            arrayOf("5A", "5B", "5C") to arrayOf("5abc"),

            // filtering out internal codes
            arrayOf("1A", "1N", "3B", "3Z", "5B", "8Z") to arrayOf("1A", "3B", "5abc"),
        ).forEach { (f, e) ->
            describe("with $f") {
                it("should be aggregated to $e") {
                    ca.setInternalCodes(internals)
                    ca.load(f)
                    ca.aggregatedCodes shouldBeEqualTo e
                }
            }
        }
    }

    describe("codesPopulation") {
        mapOf(
            // can handle empty input
            arrayOf<String>() to arrayOf(),

            // irrelevant codes
            arrayOf("1A", "2B") to arrayOf(),

            // Codes Population: 3A/B -> 1, 3C -> 2
            arrayOf("3A") to arrayOf(P1),
            arrayOf("3B") to arrayOf(P1),
            arrayOf("3C") to arrayOf(P2),
            arrayOf("3A", "3B") to arrayOf(P1),
            arrayOf("3A", "3B", "3C") to arrayOf(P1, P2),
            arrayOf("3A", "3C") to arrayOf(P1, P2),
            arrayOf("3B", "3C") to arrayOf(P1, P2),
        ).forEach { (f, e) ->
            describe("with $f") {
                it("should be aggregated to $e") {
                    ca.load(f)
                    ca.codesPopulation shouldBeEqualTo e
                }
            }
        }
    }

    describe("codesStudyDesign") {
        mapOf(
            // can handle empty input
            arrayOf<String>() to arrayOf(),

            arrayOf("1A", "2B") to arrayOf(),

            // Codes Study Design: 5A/B/C -> 1, 5E/F/G/H/I -> 2, 5U/M -> 3
            arrayOf("5A") to arrayOf(S1),
            arrayOf("5B") to arrayOf(S1),
            arrayOf("5C") to arrayOf(S1),
            arrayOf("5A", "5B") to arrayOf(S1),
            arrayOf("5A", "5C") to arrayOf(S1),
            arrayOf("5B", "5C") to arrayOf(S1),
            arrayOf("5A", "5B", "5C") to arrayOf(S1),

            arrayOf("5E") to arrayOf(S2),
            arrayOf("5F") to arrayOf(S2),
            arrayOf("5G") to arrayOf(S2),
            arrayOf("5H") to arrayOf(S2),
            arrayOf("5I") to arrayOf(S2),
            arrayOf("5E", "5F", "5G", "5H", "5I") to arrayOf(S2),

            arrayOf("5U") to arrayOf(S3),
            arrayOf("5M") to arrayOf(S3),
            arrayOf("5U", "5M") to arrayOf(S3),

            // combine
            arrayOf("3A", "3C", "5C", "5F", "5U", "5M") to arrayOf(S1, S2, S3),
        ).forEach { (f, e) ->
            describe("with $f") {
                it("should be aggregated to $e") {
                    ca.load(f)
                    ca.codesStudyDesign shouldBeEqualTo e
                }
            }
        }
    }

    describe("codesStudyDesign") {
        val internals = listOf(
            "1N", "1U", "1Z", "3U", "3Z", "4U", "4X", "4Y", "4Z",
            "5A", "5B", "5C", "5D", "5K", "6P", "6Z", "7M", "7Z", "8Z"
        )

        mapOf(
            arrayOf<String>() to Expected(),

            arrayOf("1A", "2B") to Expected(aggregatedCodes = listOf("1A", "2B")),

            // aggregation: 5A/B/C -> 5abc
            arrayOf("5A") to Expected(
                aggregatedCodes = listOf("5abc"),
                codesStudyDesign = listOf(S1)
            ),

            arrayOf("3A", "3B", "3C", "3Z", "5A", "5B", "5H", "5M") to Expected(
                aggregatedCodes = listOf("3A", "3B", "3C", "5H", "5M", "5abc"),
                codesPopulation = listOf(P1, P2),
                codesStudyDesign = listOf(S1, S2, S3)
            ),
        ).forEach { (f, e) ->
            describe("with $f") {
                it("should be aggregated to $e") {
                    ca.setInternalCodes(internals)
                    ca.load(f)
                    ca.aggregatedCodes shouldBeEqualTo e.aggregated
                    ca.codesPopulation shouldBeEqualTo e.population
                    ca.codesStudyDesign shouldBeEqualTo e.studyDesign
                }
            }
        }
    }
})

private data class Expected(
    val aggregatedCodes: List<String> = emptyList(),
    val codesPopulation: List<Short> = emptyList(),
    val codesStudyDesign: List<Short> = emptyList()
) {
    val aggregated get() = aggregatedCodes.toTypedArray()
    val population get() = codesPopulation.toTypedArray()
    val studyDesign get() = codesStudyDesign.toTypedArray()
}
