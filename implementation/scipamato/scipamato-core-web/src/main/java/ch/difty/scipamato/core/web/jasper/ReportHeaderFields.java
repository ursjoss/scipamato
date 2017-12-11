package ch.difty.scipamato.core.web.jasper;

import org.apache.wicket.util.io.IClusterable;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/**
 * Context holder for localized jasper report captions. Uses builder pattern
 * in order to avoid having constructor with numerous String arguments.
 *
 * This class can be used to serve various jasper reports with different needs
 * for localized labels. Consequently hardly any validation occurs in this class.
 * It is up to the DTOs that are passed into the reports to validate the required
 * labels are non-null.
 *
 * @author u.joss
 */
@Value
@Builder(builderMethodName = "hiddenBuilder")
public class ReportHeaderFields implements IClusterable {

    private static final long serialVersionUID = 1L;

    @NonNull
    private final String headerPart;
    @NonNull
    private final String brand;

    private final String goalsLabel;
    private final String methodsLabel;
    private final String methodOutcomeLabel;
    private final String resultMeasuredOutcomeLabel;
    private final String methodStudyDesignLabel;
    private final String populationPlaceLabel;
    private final String populationParticipantsLabel;
    private final String populationDurationLabel;
    private final String exposurePollutantLabel;
    private final String exposureAssessmentLabel;
    private final String resultExposureRangeLabel;
    private final String methodStatisticsLabel;
    private final String methodConfoundersLabel;
    private final String resultEffectEstimateLabel;
    private final String commentLabel;

    private final String populationLabel;
    private final String resultLabel;

    private final String captionLabel;
    private final String numberLabel;

    private final String authorYearLabel;

    private final String pubmedBaseUrl;

    /**
     * Static builder requiring the headerPart and brand to be passed into the constructor
     * @param headerPart
     * @param brand
     * @return ReportHeaderFieldsBuilder
     */
    public static ReportHeaderFieldsBuilder builder(final String headerPart, final String brand) {
        return hiddenBuilder().headerPart(headerPart).brand(brand);
    }

}
