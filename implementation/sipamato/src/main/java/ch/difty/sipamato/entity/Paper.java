package ch.difty.sipamato.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Pattern.Flag;

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
    public static final String EXPOSURE = "exposure";
    public static final String METHODS = "methods";

    public static final String POPULATION_PLACE = "populationPlace";
    public static final String POPULATION_PARTICIPANTS = "populationParticipants";
    public static final String POPULATION_DURATION = "populationDuration";
    public static final String EXPOSURE_POLLUTANT = "exposurePollutant";
    public static final String EXPOSURE_ASSESSMENT = "exposureAssessment";
    public static final String METHOD_OUTCOME = "methodOutcome";
    public static final String METHOD_STATISTICS = "methodStatistics";
    public static final String METHOD_CONFOUNDERS = "methodConfounders";

    public static final String RESULT = "result";
    public static final String COMMENT = "comment";
    public static final String INTERN = "intern";

    public static final String RESULT_EXPOSURE_RANGE = "resultExposureRange";
    public static final String RESULT_EFFECT_ESTIMATE = "resultEffectEstimate";

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
    private String exposure;
    private String exposurePollutant;
    private String exposureAssessment;
    private String methods;
    private String methodOutcome;
    private String methodStatistics;
    private String methodConfounders;

    private String result;
    private String resultExposureRange;
    private String resultEffectEstimate;
    private String comment;
    private String intern;

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

    public String getExposure() {
        return exposure;
    }

    public void setExposure(String exposure) {
        this.exposure = exposure;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Paper [id=");
        sb.append(id);
        sb.append(", doi=");
        sb.append(doi);
        sb.append(", pmId=");
        sb.append(pmId);
        sb.append(", authors=");
        sb.append(authors);
        sb.append(", title=");
        sb.append(title);
        sb.append(", location=");
        sb.append(location);
        sb.append(", publicationYear=");
        sb.append(publicationYear);
        sb.append(", firstAuthor=");
        sb.append(firstAuthor);
        sb.append(", goals=");
        sb.append(goals);
        sb.append(", population=");
        sb.append(population);
        sb.append(", exposure=");
        sb.append(exposure);
        sb.append(", methods=");
        sb.append(methods);
        sb.append(", result=");
        sb.append(result);
        sb.append(", comment=");
        sb.append(comment);
        sb.append(", intern=");
        sb.append(intern);
        sb.append("]");
        return sb.toString();
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

}
