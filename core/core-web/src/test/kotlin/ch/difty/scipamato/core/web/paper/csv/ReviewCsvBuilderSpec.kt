package ch.difty.scipamato.core.web.paper.csv

import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.web.paper.jasper.ReportHeaderFields
import io.kotest.core.spec.style.DescribeSpec
import org.amshove.kluent.shouldBeEqualTo

private const val BOM = "\ufeff"

@Suppress("unused")
object ReviewCsvBuilderSpec : DescribeSpec({

    describe("csv builder") {
        val builder = ReviewCsvAdapter(rhf = rhf)
        it("can build CSV with multiline fields with dynamic quoting") {
            builder.build(listOf(PaperWithIndex(1), PaperWithIndex(2))) shouldBeEqualTo """
                ${BOM}nl;ayl;ppl;mol;epl;msdl;pdl;ppl;eal;rerl;msl;mcl;reel;cl1;cl2;il;gl;pl;ml;rl
                1;Bond1 J 2021;pp1;mo1;eo1;msd1;pd1;pp1;ea1;rer1;ms1;mc1;ree1;c11;c21;i1;g1;p1;m1;r1
                2;Bond2 J 2022;pp2;mo2;eo2;msd2;pd2;pp2;ea2;rer2;ms2;mc2;ree2;c12;"c22
                foo
                bar
                baz";i2;g2;p2;m2;r2

                """.trimIndent()
        }
    }
})

private val rhf = ReportHeaderFields(
    headerPart = "hp",
    brand = "b",
    numberLabel = "nl",
    authorYearLabel = "ayl",
    populationPlaceLabel = "ppl",
    methodOutcomeLabel = "mol",
    exposurePollutantLabel = "epl",
    methodStudyDesignLabel = "msdl",
    populationDurationLabel = "pdl",
    populationParticipantsLabel = "ppl",
    exposureAssessmentLabel = "eal",
    resultExposureRangeLabel = "rerl",
    methodStatisticsLabel = "msl",
    methodConfoundersLabel = "mcl",
    resultEffectEstimateLabel = "reel",
    conclusionLabel = "cl1",
    commentLabel = "cl2",
    internLabel = "il",
    goalsLabel = "gl",
    populationLabel = "pl",
    methodsLabel = "ml",
    resultLabel = "rl",
)

@Suppress("TestFunctionName")
private fun PaperWithIndex(i: Int) = Paper().apply {
    number = i.toLong()
    firstAuthor = "Bond$i J"
    publicationYear = 2020 + i
    populationPlace = "pp$i"
    methodOutcome = "mo$i"
    exposurePollutant = "eo$i"
    methodStudyDesign = "msd$i"
    populationDuration = "pd$i"
    populationParticipants = "pp$i"
    exposureAssessment = "ea$i"
    resultExposureRange = "rer$i"
    methodStatistics = "ms$i"
    methodConfounders = "mc$i"
    resultEffectEstimate = "ree$i"
    conclusion = "c1$i"
    comment = if (i == 1) "c2$i" else """c2$i
        |foo
        |bar
        |baz
    """.trimMargin()
    intern = "i$i"
    goals = "g$i"
    population = "p$i"
    methods = "m$i"
    result = "r$i"
}
