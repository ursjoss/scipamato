package ch.difty.scipamato.core.web.paper.jasper;

import static org.assertj.core.api.Assertions.assertThat;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

public class ReportHeaderFieldsTest {

    @Test
    public void equalsVerify() {
        EqualsVerifier
            .forClass(ReportHeaderFields.class)
            .withRedefinedSuperclass()
            .suppress(Warning.STRICT_INHERITANCE)
            .verify();
    }

    @Test
    public void makeMinimalReportHeaderFields() {
        ReportHeaderFields rhf = ReportHeaderFields
            .builder("headerPart", "brand")
            .build();
        assertThat(rhf.getHeaderPart()).isEqualTo("headerPart");
        assertThat(rhf.getBrand()).isEqualTo("brand");
    }

    @Test
    public void makeReportHeaderFields() {
        ReportHeaderFields rhf = ReportHeaderFields
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
            .build();

        assertThat(rhf.getHeaderPart()).isEqualTo("headerPart1");
        assertThat(rhf.getBrand()).isEqualTo("brand1");

        assertThat(rhf.getGoalsLabel()).isEqualTo("g");
        assertThat(rhf.getMethodsLabel()).isEqualTo("m");
        assertThat(rhf.getMethodOutcomeLabel()).isEqualTo("mo");
        assertThat(rhf.getResultMeasuredOutcomeLabel()).isEqualTo("rmo");
        assertThat(rhf.getMethodStudyDesignLabel()).isEqualTo("msd");
        assertThat(rhf.getPopulationPlaceLabel()).isEqualTo("pp");
        assertThat(rhf.getPopulationParticipantsLabel()).isEqualTo("pap");
        assertThat(rhf.getPopulationDurationLabel()).isEqualTo("pd");
        assertThat(rhf.getExposurePollutantLabel()).isEqualTo("ep");
        assertThat(rhf.getExposureAssessmentLabel()).isEqualTo("ea");
        assertThat(rhf.getResultExposureRangeLabel()).isEqualTo("rer");
        assertThat(rhf.getMethodStatisticsLabel()).isEqualTo("ms");
        assertThat(rhf.getMethodConfoundersLabel()).isEqualTo("mc");
        assertThat(rhf.getResultEffectEstimateLabel()).isEqualTo("ree");
        assertThat(rhf.getConclusionLabel()).isEqualTo("cc");
        assertThat(rhf.getCommentLabel()).isEqualTo("c");
        assertThat(rhf.getPopulationLabel()).isEqualTo("p");
        assertThat(rhf.getResultLabel()).isEqualTo("r");
        assertThat(rhf.getCaptionLabel()).isEqualTo("cap");
        assertThat(rhf.getNumberLabel()).isEqualTo("n");
        assertThat(rhf.getAuthorYearLabel()).isEqualTo("ay");
        assertThat(rhf.getPubmedBaseUrl()).isEqualTo("pbu/");
    }
}
