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
 * all respective short fields with content are concatenated into the respective field in SciPaMaTo-Public. Note
 * that there's a known deficiency: The labels that are included with the short fields are always in english, they
 * will not adapt to the browser locale of a viewer.
 */
@Component
public class CoreShortFieldWithEmptyMainFieldConcatenator extends AbstractShortFieldConcatenator
    implements CoreShortFieldConcatenator {

    private static final String PAPER = "paper";

    public CoreShortFieldWithEmptyMainFieldConcatenator() {
        super(true);
    }

    @Override
    public String methodsFrom(final Paper p) {
        AssertAs.notNull(p, PAPER);
        return methodsFrom(p.getMethods(), p.getMethodStudyDesign(), p.getMethodOutcome(), p.getPopulationPlace(),
            p.getExposurePollutant(), p.getExposureAssessment(), p.getMethodStatistics(), p.getMethodConfounders());
    }

    @Override
    public String populationFrom(final Paper p) {
        AssertAs.notNull(p, PAPER);
        return populationFrom(p.getPopulation(), p.getPopulationPlace(), p.getPopulationParticipants(),
            p.getPopulationDuration());
    }

    @Override
    public String resultFrom(final Paper p) {
        AssertAs.notNull(p, PAPER);
        return resultFrom(p.getResult(), p.getResultExposureRange(), p.getResultEffectEstimate(),
            p.getResultMeasuredOutcome(), p.getConclusion());
    }

}
