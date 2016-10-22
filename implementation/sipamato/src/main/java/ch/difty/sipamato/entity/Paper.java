package ch.difty.sipamato.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Pattern.Flag;

import org.apache.commons.collections4.list.UnmodifiableList;
import org.hibernate.validator.constraints.Range;

public class Paper extends SipamatoEntity {

    private static final long serialVersionUID = 1L;

    /**
     * One or more of the extended word characters including - and '
     */
    private static final String RE_WW = "[" + RE_W + "-']+";
    private static final String RE_S_WW = "\\s" + RE_WW;

    private static final String AUTHOR_REGEX = "^" + RE_WW + "(" + RE_S_WW + "){0,}(," + RE_S_WW + "(" + RE_S_WW + "){0,}){0,}\\.$";
    private static final String DOI_REGEX = "^10\\.\\d{4,9}/[-._;()/:A-Z0-9]+$";

    public static final String ID = "id";
    public static final String PMID = "pmId";
    public static final String DOI = "doi";
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

    public static final String CODES_OF_CLASS_1 = "codesOfClass1";
    public static final String CODES_OF_CLASS_2 = "codesOfClass2";
    public static final String CODES_OF_CLASS_3 = "codesOfClass3";
    public static final String CODES_OF_CLASS_4 = "codesOfClass4";
    public static final String CODES_OF_CLASS_5 = "codesOfClass5";
    public static final String CODES_OF_CLASS_6 = "codesOfClass6";
    public static final String CODES_OF_CLASS_7 = "codesOfClass7";
    public static final String CODES_OF_CLASS_8 = "codesOfClass8";

    private Long id;

    /*
     * Digital Object Identifier (see http://www.doi.org)
     *
     * The validation pattern is simplified and seems to catch roughly 74.4M out of 74.9M DOIs.
     * The uncaught ones seem to be old and hopefully don't turn up within Sipamato. Otherwise additional
     * regex patterns catching more of the remaining ones can be found here:
     *
     * http://blog.crossref.org/2015/08/doi-regular-expressions.html (thx to Andrew Gilmartin)
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

    @Range(min = 1500, max = 2100, message = "{paper.invalidPublicationYear}")
    private int publicationYear;

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
    private String comment;
    private String intern;

    /**
     * The index of the following lists of {@link Code} must correspond with the ids defined in {@link CodeClassId}
     */
    // TODO turn into map of lists and make it dynamic
    private final List<Code> codesOfClass1 = new ArrayList<>();
    private final List<Code> codesOfClass2 = new ArrayList<>();
    private final List<Code> codesOfClass3 = new ArrayList<>();
    private final List<Code> codesOfClass4 = new ArrayList<>();
    private final List<Code> codesOfClass5 = new ArrayList<>();
    private final List<Code> codesOfClass6 = new ArrayList<>();
    private final List<Code> codesOfClass7 = new ArrayList<>();
    private final List<Code> codesOfClass8 = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public Integer getPmId() {
        return pmId;
    }

    public void setPmId(Integer pmId) {
        this.pmId = pmId;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getFirstAuthor() {
        return firstAuthor;
    }

    public void setFirstAuthor(String firstAuthor) {
        this.firstAuthor = firstAuthor;
    }

    public boolean isFirstAuthorOverridden() {
        return firstAuthorOverridden;
    }

    public void setFirstAuthorOverridden(boolean firstAuthorOverridden) {
        this.firstAuthorOverridden = firstAuthorOverridden;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getMethods() {
        return methods;
    }

    public void setMethods(String methods) {
        this.methods = methods;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getIntern() {
        return intern;
    }

    public void setIntern(String intern) {
        this.intern = intern;
    }

    public String getPopulationPlace() {
        return populationPlace;
    }

    public void setPopulationPlace(String populationPlace) {
        this.populationPlace = populationPlace;
    }

    public String getPopulationParticipants() {
        return populationParticipants;
    }

    public void setPopulationParticipants(String populationParticipants) {
        this.populationParticipants = populationParticipants;
    }

    public String getPopulationDuration() {
        return populationDuration;
    }

    public void setPopulationDuration(String populationDuration) {
        this.populationDuration = populationDuration;
    }

    public String getExposurePollutant() {
        return exposurePollutant;
    }

    public void setExposurePollutant(String exposurePollutant) {
        this.exposurePollutant = exposurePollutant;
    }

    public String getExposureAssessment() {
        return exposureAssessment;
    }

    public void setExposureAssessment(String exposureAssessment) {
        this.exposureAssessment = exposureAssessment;
    }

    public String getMethodStudyDesign() {
        return methodStudyDesign;
    }

    public void setMethodStudyDesign(String methodStudyDesign) {
        this.methodStudyDesign = methodStudyDesign;
    }

    public String getMethodOutcome() {
        return methodOutcome;
    }

    public void setMethodOutcome(String methodOutcome) {
        this.methodOutcome = methodOutcome;
    }

    public String getMethodStatistics() {
        return methodStatistics;
    }

    public void setMethodStatistics(String methodStatistics) {
        this.methodStatistics = methodStatistics;
    }

    public String getMethodConfounders() {
        return methodConfounders;
    }

    public void setMethodConfounders(String methodConfounders) {
        this.methodConfounders = methodConfounders;
    }

    public String getResultExposureRange() {
        return resultExposureRange;
    }

    public void setResultExposureRange(String resultExposureRange) {
        this.resultExposureRange = resultExposureRange;
    }

    public String getResultEffectEstimate() {
        return resultEffectEstimate;
    }

    public void setResultEffectEstimate(String resultEffectEstimate) {
        this.resultEffectEstimate = resultEffectEstimate;
    }

    public List<Code> getCodesOfClass1() {
        return new UnmodifiableList<Code>(codesOfClass1);
    }

    public void clearCodesOfClass1() {
        codesOfClass1.clear();
    }

    public void addCodeOfClass1(Code code) {
        codesOfClass1.add(new Code(code));
    }

    public void addCodesOfClass1(List<Code> codes) {
        codesOfClass1.addAll(codes.stream().map(c -> new Code(c)).collect(Collectors.toList()));
    }

    public List<Code> getCodesOfClass5() {
        return new UnmodifiableList<Code>(codesOfClass5);
    }

    public void clearCodesOfClass5() {
        codesOfClass5.clear();
    }

    public void addCodeOfClass5(Code code) {
        codesOfClass5.add(new Code(code));
    }

    public void addCodesOfClass5(List<Code> codes) {
        codesOfClass5.addAll(codes.stream().map(c -> new Code(c)).collect(Collectors.toList()));
    }

}
