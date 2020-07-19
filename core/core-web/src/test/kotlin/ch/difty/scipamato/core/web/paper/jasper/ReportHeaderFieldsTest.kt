package ch.difty.scipamato.core.web.paper.jasper

import nl.jqno.equalsverifier.EqualsVerifier
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

internal class ReportHeaderFieldsTest {

    @Test
    fun equalsVerify() {
        EqualsVerifier.simple()
            .forClass(ReportHeaderFields::class.java)
            .withRedefinedSuperclass()
            .verify()
    }

    @Test
    fun makeMinimalReportHeaderFields() {
        val rhf = ReportHeaderFields
            .builder("headerPart", "brand")
            .build()
        rhf.headerPart shouldBeEqualTo "headerPart"
        rhf.brand shouldBeEqualTo "brand"
    }

    @Test
    fun makeReportHeaderFields() {
        val rhf = ReportHeaderFields
            .builder("headerPart1", "brand1")
            .goalsLabel("g")
            .methodsLabel("m")
            .methodOutcomeLabel("mo")
            .resultMeasuredOutcomeLabel("rmo")
            .methodStudyDesignLabel("msd")
            .populationPlaceLabel("pp")
            .populationParticipantsLabel("pap")
            .populationDurationLabel("pd")
            .exposurePollutantLabel("ep")
            .exposureAssessmentLabel("ea")
            .resultExposureRangeLabel("rer")
            .methodStatisticsLabel("ms")
            .methodConfoundersLabel("mc")
            .resultEffectEstimateLabel("ree")
            .conclusionLabel("cc")
            .commentLabel("c")
            .populationLabel("p")
            .resultLabel("r")
            .captionLabel("cap")
            .numberLabel("n")
            .authorYearLabel("ay")
            .pubmedBaseUrl("pbu/")
            .build()
        rhf.headerPart shouldBeEqualTo "headerPart1"
        rhf.brand shouldBeEqualTo "brand1"
        rhf.goalsLabel shouldBeEqualTo "g"
        rhf.methodsLabel shouldBeEqualTo "m"
        rhf.methodOutcomeLabel shouldBeEqualTo "mo"
        rhf.resultMeasuredOutcomeLabel shouldBeEqualTo "rmo"
        rhf.methodStudyDesignLabel shouldBeEqualTo "msd"
        rhf.populationPlaceLabel shouldBeEqualTo "pp"
        rhf.populationParticipantsLabel shouldBeEqualTo "pap"
        rhf.populationDurationLabel shouldBeEqualTo "pd"
        rhf.exposurePollutantLabel shouldBeEqualTo "ep"
        rhf.exposureAssessmentLabel shouldBeEqualTo "ea"
        rhf.resultExposureRangeLabel shouldBeEqualTo "rer"
        rhf.methodStatisticsLabel shouldBeEqualTo "ms"
        rhf.methodConfoundersLabel shouldBeEqualTo "mc"
        rhf.resultEffectEstimateLabel shouldBeEqualTo "ree"
        rhf.conclusionLabel shouldBeEqualTo "cc"
        rhf.commentLabel shouldBeEqualTo "c"
        rhf.populationLabel shouldBeEqualTo "p"
        rhf.resultLabel shouldBeEqualTo "r"
        rhf.captionLabel shouldBeEqualTo "cap"
        rhf.numberLabel shouldBeEqualTo "n"
        rhf.authorYearLabel shouldBeEqualTo "ay"
        rhf.pubmedBaseUrl shouldBeEqualTo "pbu/"
    }
}
