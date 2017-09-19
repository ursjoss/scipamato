package ch.difty.scipamato.entity;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Pattern.Flag;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Paper extends IdScipamatoEntity<Long> implements CodeBoxAware {

    private static final long serialVersionUID = 1L;

    public static final String NA_AUTHORS = "N A.";
    public static final String NA_STRING = "n.a.";
    public static final int NA_PUBL_YEAR = 1500;

    /**
     * One or more of the extended word characters including - and '
     */
    private static final String RE_S_WW = "\\s" + RE_WW;

    private static final String AUTHOR_REGEX = "^" + RE_WW + "(" + RE_S_WW + "){0,}(," + RE_S_WW + "(" + RE_S_WW + "){0,}){0,}\\.$";
    private static final String DOI_REGEX = "^10\\.\\d{4,9}/[-._;()/:A-Z0-9]+$";

    public static final String NUMBER = "number";
    public static final String DOI = "doi";
    public static final String PMID = "pmId";
    public static final String AUTHORS = "authors";
    public static final String FIRST_AUTHOR = "firstAuthor";
    public static final String FIRST_AUTHOR_OVERRIDDEN = "firstAuthorOverridden";
    public static final String TITLE = "title";
    public static final String LOCATION = "location";
    public static final String PUBL_YEAR = "publicationYear";

    public static final String GOALS = "goals";
    public static final String POPULATION = "population";
    public static final String METHODS = "methods";

    public static final String POPULATION_PLACE = "populationPlace";
    public static final String POPULATION_PARTICIPANTS = "populationParticipants";
    public static final String POPULATION_DURATION = "populationDuration";
    public static final String EXPOSURE_POLLUTANT = "exposurePollutant";
    public static final String EXPOSURE_ASSESSMENT = "exposureAssessment";
    public static final String METHOD_STUDY_DESIGN = "methodStudyDesign";
    public static final String METHOD_OUTCOME = "methodOutcome";
    public static final String METHOD_STATISTICS = "methodStatistics";
    public static final String METHOD_CONFOUNDERS = "methodConfounders";

    public static final String RESULT = "result";
    public static final String COMMENT = "comment";
    public static final String INTERN = "intern";

    public static final String RESULT_EXPOSURE_RANGE = "resultExposureRange";
    public static final String RESULT_EFFECT_ESTIMATE = "resultEffectEstimate";
    public static final String RESULT_MEASURED_OUTCOME = "resultMeasuredOutcome";

    public static final String ORIGINAL_ABSTRACT = "originalAbstract";

    public static final String ATTACHMENTS = "attachments";

    public static final String MAIN_CODE_OF_CODECLASS1 = "mainCodeOfCodeclass1";

    public static final String CODES = "codes";

    // Override to get unique names
    public static final String CREATED = "paper.created";
    public static final String CREATED_BY = "paper.created_by";
    public static final String LAST_MOD = "paper.last_modified";
    public static final String LAST_MOD_BY = "paper.last_modified_by";

    @NotNull
    @Min(0)
    private Long number;

    /*
     * Digital Object Identifier (see http://www.doi.org)
     *
     * The validation pattern is simplified and seems to catch roughly 74.4M out of 74.9M DOIs.
     * The uncaught ones seem to be old and hopefully don't turn up within SciPaMaTo. Otherwise additional
     * regex patterns catching more of the remaining ones can be found here:
     *
     * <a href="http://blog.crossref.org/2015/08/doi-regular-expressions.html">http://blog.crossref.org/2015/08/doi-regular-expressions.html (thx to Andrew Gilmartin)</a>
     *
     * /^10.\d{4,9}/[-._;()/:A-Z0-9]+$/i
     */
    @Pattern(regexp = DOI_REGEX, flags = { Flag.CASE_INSENSITIVE }, message = "{paper.invalidDOI}")
    private String doi;

    private Integer pmId;

    @NotNull
    @Pattern(regexp = AUTHOR_REGEX, message = "{paper.invalidAuthor}")
    private String authors;

    @NotNull
    private String firstAuthor;
    private boolean firstAuthorOverridden;
    @NotNull
    private String title;
    @NotNull
    private String location;

    @NotNull
    @Min(value = 1500, message = "{paper.invalidPublicationYear}")
    @Max(value = 2100, message = "{paper.invalidPublicationYear}")
    private Integer publicationYear;

    @NotNull
    private String goals;
    private String population;
    private String populationPlace;
    private String populationParticipants;
    private String populationDuration;
    private String exposurePollutant;
    private String exposureAssessment;
    private String methods;
    private String methodStudyDesign;
    private String methodOutcome;
    private String methodStatistics;
    private String methodConfounders;

    private String result;
    private String resultExposureRange;
    private String resultEffectEstimate;
    private String resultMeasuredOutcome;
    private String comment;
    private String intern;
    private String originalAbstract;
    private String mainCodeOfCodeclass1;

    // Note: Attachments are not persisted with the Paper in the repo
    private final List<PaperAttachment> attachments = new ArrayList<>();

    private final CodeBox codes = new PaperCodeBox();

    public List<PaperAttachment> getAttachments() {
        return new ArrayList<>(attachments);
    }

    public void setAttachments(final List<PaperAttachment> attachments) {
        this.attachments.clear();
        if (attachments != null)
            this.attachments.addAll(attachments);
    }

    @Override
    public void clearCodes() {
        this.codes.clear();
    }

    @Override
    public List<Code> getCodes() {
        return this.codes.getCodes();
    }

    @Override
    public List<Code> getCodesOf(CodeClassId ccId) {
        return this.codes.getCodesBy(ccId);
    }

    @Override
    public void clearCodesOf(CodeClassId ccId) {
        this.codes.clearBy(ccId);
    }

    @Override
    public void addCode(Code code) {
        this.codes.addCode(code);
    }

    @Override
    public void addCodes(List<Code> codes) {
        this.codes.addCodes(codes);
    }

    @Override
    public String getDisplayValue() {
        final StringBuilder sb = new StringBuilder();
        sb.append(firstAuthor).append(" (").append(publicationYear).append("): ");
        sb.append(title).append(".");
        return sb.toString();
    }

}
