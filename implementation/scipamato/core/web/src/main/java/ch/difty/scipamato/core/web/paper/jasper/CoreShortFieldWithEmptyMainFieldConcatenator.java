package ch.difty.scipamato.core.web.paper.jasper;

import org.springframework.stereotype.Component;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.paper.AbstractShortFieldConcatenator;
import ch.difty.scipamato.core.entity.Paper;

/**
 * Gathers the content for the the fields methods, population and result.
 * <p>
 * There are some main fields (result, population and method) that could alternatively
 * be represented with a number of Short field (Kurzerfassung). If the main field is populated, it will always
 * have precedence, regardless of whether there's content in the respective short fields. If the main field is null,
 * all respective short fields with content are concatenated into the respective field in SciPaMaTo-Public, each on
 * a new line. The labels will be localized depending on the browser locale of the user (if language is supported).
 */
@Component
public class CoreShortFieldWithEmptyMainFieldConcatenator extends AbstractShortFieldConcatenator
    implements CoreShortFieldConcatenator {

    private static final String PAPER = "paper";

    public CoreShortFieldWithEmptyMainFieldConcatenator() {
        super(true);
    }

    @Override
    public String methodsFrom(final Paper p, final ReportHeaderFields rhf) {
        AssertAs.notNull(p, PAPER);
        return methodsFrom(p.getMethods(), new Tuple(rhf.getMethodStudyDesignLabel(), p.getMethodStudyDesign()),
            new Tuple(rhf.getMethodOutcomeLabel(), p.getMethodOutcome()),
            new Tuple(rhf.getPopulationPlaceLabel(), p.getPopulationPlace()),
            new Tuple(rhf.getExposurePollutantLabel(), p.getExposurePollutant()),
            new Tuple(rhf.getExposureAssessmentLabel(), p.getExposureAssessment()),
            new Tuple(rhf.getMethodStatisticsLabel(), p.getMethodStatistics()),
            new Tuple(rhf.getMethodConfoundersLabel(), p.getMethodConfounders()));
    }

    @Override
    public String populationFrom(final Paper p, final ReportHeaderFields rhf) {
        AssertAs.notNull(p, PAPER);
        return populationFrom(p.getPopulation(), new Tuple(rhf.getPopulationPlaceLabel(), p.getPopulationPlace()),
            new Tuple(rhf.getPopulationParticipantsLabel(), p.getPopulationParticipants()),
            new Tuple(rhf.getPopulationDurationLabel(), p.getPopulationDuration()));
    }

    @Override
    public String resultFrom(final Paper p, final ReportHeaderFields rhf) {
        AssertAs.notNull(p, PAPER);
        return resultFrom(p.getResult(), new Tuple(rhf.getResultMeasuredOutcomeLabel(), p.getResultMeasuredOutcome()),
            new Tuple(rhf.getResultExposureRangeLabel(), p.getResultExposureRange()),
            new Tuple(rhf.getResultEffectEstimateLabel(), p.getResultEffectEstimate()),
            new Tuple(rhf.getConclusionLabel(), p.getConclusion()));
    }

}
