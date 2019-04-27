package ch.difty.scipamato.core.sync.code;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class HidingInternalsCodeAggregatorTest {

    private static final short P1 = 1;
    private static final short P2 = 2;
    private static final short S1 = 1;
    private static final short S2 = 2;
    private static final short S3 = 3;

    private final CodeAggregator ca = new HidingInternalsCodeAggregator();

    private static Stream<Arguments> paramsCodeAggregation() {
        /*
         * input           String[]: the code array to be loaded
         * aggregatedCodes String[]: the expected resulting array of (partially) aggregated codes
         */
        // @formatter:off
        return Stream.of(
            // can handle null and empty input
            Arguments.of(null, new String[] {}),
            Arguments.of(new String[] {}, new String[] {}),

            Arguments.of(new String[] { "1A", "2B" }, new String[] { "1A", "2B" }),

            // aggregation: 5A/B/C -> 5abc
            Arguments.of(new String[] { "5A" }, new String[] { "5abc" }),
            Arguments.of(new String[] { "5B" }, new String[] { "5abc" }),
            Arguments.of(new String[] { "5C" }, new String[] { "5abc" }),
            Arguments.of(new String[] { "5A", "5B" }, new String[] { "5abc" }),
            Arguments.of(new String[] { "5A", "5C" }, new String[] { "5abc" }),
            Arguments.of(new String[] { "5A", "5B", "5C" }, new String[] { "5abc" }),

            Arguments.of(new String[] { "5A", "5B", "5C" }, new String[] { "5abc" }),

            // filtering out internal codes
            Arguments.of(new String[] { "1A", "1N", "3B", "3Z", "5B", "8Z" }, new String[] { "1A", "3B", "5abc" }));
            //@formatter:on
    }

    @ParameterizedTest(name = "{index}: code {0} is aggregated to {1}")
    @MethodSource("paramsCodeAggregation")
    void gettingAggregatedCodes(String[] input, String[] codes) {
        String[] internals = new String[] { "1N", "1U", "1Z", "3U", "3Z", "4U", "4X", "4Y", "4Z", "5A", "5B", "5C",
            "5D", "5K", "6P", "6Z", "7M", "7Z", "8Z" };
        ca.setInternalCodes(Arrays.asList(internals));
        ca.load(input);
        assertThat(ca.getAggregatedCodes()).isEqualTo(codes);
    }

    private static Stream<Arguments> paramsCodePopulation() {
        /*
         * input          String[]: the code array to be loaded
         * codesPopulation Short[]: the expected resulting array of code ids for codesPopulation
         */
        // @formatter:off
        return Stream.of(
            // can handle null and empty input
            Arguments.of(null, new Short[] {}),
            Arguments.of(new String[] {}, new Short[] {}),

            // irrelevant codes
            Arguments.of(new String[] { "1A", "2B" }, new Short[] {}),

            // Codes Population: 3A/B -> 1, 3C -> 2
            Arguments.of(new String[] { "3A" }, new Short[] { P1 }),
            Arguments.of(new String[] { "3B" }, new Short[] { P1 }),
            Arguments.of(new String[] { "3C" }, new Short[] { P2 }),
            Arguments.of(new String[] { "3A", "3B" }, new Short[] { P1 }),
            Arguments.of(new String[] { "3A", "3B", "3C" }, new Short[] { P1, P2 }),
            Arguments.of(new String[] { "3A", "3C" }, new Short[] { P1, P2 }),
            Arguments.of(new String[] { "3B", "3C" }, new Short[] { P1, P2 }));
        // @formatter:on
    }

    @ParameterizedTest(name = "{index}: code array {0} results in code population {1}")
    @MethodSource("paramsCodePopulation")
    void gettingCodePopulation(String[] input, Short[] codesPopulation) {
        ca.load(input);
        assertThat(ca.getCodesPopulation()).isEqualTo(codesPopulation);
    }

    private static Stream<Arguments> paramsCodeStudyDesign() {
        /*
         * input           String[]: the code array to be loaded
         * codesStudyDesign Short[]: the expected resulting array of code ids for codesStudyDesign
         */
        // @formatter:off
        return Stream.of(
            // can handle null and empty input
            Arguments.of(null, new Short[] {}),
            Arguments.of(new String[] {}, new Short[] {}),

            Arguments.of(new String[] { "1A", "2B" }, new Short[] {}),

            // Codes Study Design: 5A/B/C -> 1, 5E/F/G/H/I -> 2, 5U/M -> 3
            Arguments.of(new String[] { "5A" }, new Short[] { S1 }),
            Arguments.of(new String[] { "5B" }, new Short[] { S1 }),
            Arguments.of(new String[] { "5C" }, new Short[] { S1 }),
            Arguments.of(new String[] { "5A", "5B" }, new Short[] { S1 }),
            Arguments.of(new String[] { "5A", "5C" }, new Short[] { S1 }),
            Arguments.of(new String[] { "5B", "5C" }, new Short[] { S1 }),
            Arguments.of(new String[] { "5A", "5B", "5C" }, new Short[] { S1 }),

            Arguments.of(new String[] { "5E" }, new Short[] { S2 }),
            Arguments.of(new String[] { "5F" }, new Short[] { S2 }),
            Arguments.of(new String[] { "5G" }, new Short[] { S2 }),
            Arguments.of(new String[] { "5H" }, new Short[] { S2 }),
            Arguments.of(new String[] { "5I" }, new Short[] { S2 }),
            Arguments.of(new String[] { "5E", "5F", "5G", "5H", "5I" }, new Short[] { S2 }),

            Arguments.of(new String[] { "5U" }, new Short[] { S3 }),
            Arguments.of(new String[] { "5M" }, new Short[] { S3 }),
            Arguments.of(new String[] { "5U", "5M" }, new Short[] { S3 }),

            // combine
            Arguments.of(new String[] { "3A", "3C", "5C", "5F", "5U", "5M" }, new Short[] { S1, S2, S3 }));
        // @formatter:on
    }

    @ParameterizedTest(name = "{index}: code array {0} results in code study design {1}")
    @MethodSource("paramsCodeStudyDesign")
    void gettingCodeStudyDesign(String[] input, Short[] codesStudyDesign) {
        ca.load(input);
        assertThat(ca.getCodesStudyDesign()).isEqualTo(codesStudyDesign);
    }

    private static Stream<Arguments> paramsAll() {
        /*
         * input           String[]: the code array to be loaded
         * codes           String[]: the expected resulting array of (partially) aggregated codes
         * codesPopulation  Short[]: the expected resulting array of code ids for codesPopulation
         * codesStudyDesign Short[]: the expected resulting array of code ids for codesStudyDesign
         */
        // @formatter:off
        return Stream.of(
            // can handle null and empty input
            Arguments.of(null, new String[] {}, new Short[] {}, new Short[] {}),
            Arguments.of(new String[] {}, new String[] {}, new Short[] {}, new Short[] {}),

            Arguments.of(new String[] { "1A", "2B" }, new String[] { "1A", "2B" }, new Short[] {}, new Short[] {}),

            // aggregation: 5A/B/C -> 5abc
            Arguments.of(new String[] { "5A" }, new String[] { "5abc" }, new Short[] {}, new Short[] { S1 }),
            Arguments.of(new String[] { "3A", "3B", "3C", "3Z", "5A", "5B", "5H", "5M" },
                new String[] { "3A", "3B", "3C", "5H", "5M", "5abc" }, new Short[] { P1, P2 },
                new Short[] { S1, S2, S3 }) );
        // @formatter:on
    }

    @ParameterizedTest(name = "{index}: code array {0} results aggregated codes {1}, codesPopulation {2} and codesStudyDesign {3}")
    @MethodSource("paramsAll")
    void gettingAllCodeTypes(String[] input, String[] codes, Short[] codesPopulation, Short[] codesStudyDesign) {
        String[] internals = new String[] { "1N", "1U", "1Z", "3U", "3Z", "4U", "4X", "4Y", "4Z", "5A", "5B", "5C",
            "5D", "5K", "6P", "6Z", "7M", "7Z", "8Z" };
        ca.setInternalCodes(Arrays.asList(internals));
        ca.load(input);
        assertThat(ca.getAggregatedCodes()).isEqualTo(codes);
        assertThat(ca.getCodesPopulation()).isEqualTo(codesPopulation);
        assertThat(ca.getCodesStudyDesign()).isEqualTo(codesStudyDesign);
    }
}
