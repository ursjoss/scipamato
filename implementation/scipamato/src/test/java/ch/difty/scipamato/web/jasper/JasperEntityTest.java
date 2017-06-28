package ch.difty.scipamato.web.jasper;

import org.junit.Before;

import ch.difty.scipamato.entity.Code;
import ch.difty.scipamato.entity.CodeClassId;
import ch.difty.scipamato.entity.Paper;

public abstract class JasperEntityTest {

    private static final long ID = 1l;
    protected static final long NUMBER = 100l;
    protected static final String AUTHORS = "authors";
    protected static final String TITLE = "title";
    protected static final String LOCATION = "location";
    protected static final String GOALS = "goals";
    protected static final String POPULATION = "population";
    protected static final String METHODS = "methods";
    protected static final String RESULT = "results";
    protected static final String COMMENT = "comment";
    protected static final String POPULATION_LABEL = "populationLabel";
    protected static final String METHODS_LABEL = "methodsLabel";
    protected static final String RESULT_LABEL = "resultLabel";
    protected static final String COMMENT_LABEL = "commentLabel";
    protected static final String HEADER_PART = "headerPart";
    protected static final String BRAND = "brand";
    protected static final String CREATED_BY = "creatingUser";

    protected static final String POPULATION_PLACE = "populationPlace";
    protected static final String POPULATION_DURATION = "populationDuration";
    protected static final String POPULATION_PARTICIPANTS = "populationParticipants";
    protected static final String METHOD_OUTCOME = "methodOutcome";
    protected static final String METHOD_STUDY_DESIGN = "methodStudyDesign";
    protected static final String METHOD_STATISTICS = "methodStatistics";
    protected static final String METHOD_CONFOUNDERS = "methodConfounders";
    protected static final String EXPOSURE_POLLUTANT = "exposurePolutant";
    protected static final String EXPOSURE_ASSESSMENT = "exposureAssessment";
    protected static final String RESULT_EXPOSURE_RANGE = "resultExposureRange";
    protected static final String RESULT_EFFECT_ESTIMATE = "resultEffectEstimate";
    protected static final String POPULATION_PLACE_LABEL = "populationPlaceLabel";
    protected static final String RESULT_EFFECT_ESTIMATE_LABEL = "resultEffectEstimateLabel";

    protected static final int PM_ID = 1234;

    protected static final String FIRST_AUTHOR = "firstAuthor";
    protected static final int PUBLICATION_YEAR = 2017;

    protected Paper p = new Paper();

    @Before
    public void setUp() {
        p.setId(ID);
        p.setNumber(NUMBER);
        p.setAuthors(AUTHORS);
        p.setFirstAuthor(FIRST_AUTHOR);
        p.setPublicationYear(PUBLICATION_YEAR);
        p.setTitle(TITLE);
        p.setLocation(LOCATION);
        p.setGoals(GOALS);
        p.setPopulation(POPULATION);
        p.setMethods(METHODS);
        p.setResult(RESULT);
        p.setComment(COMMENT);
        p.setCreatedByName(CREATED_BY);

        p.setPopulationPlace(POPULATION_PLACE);
        p.setPopulationDuration(POPULATION_DURATION);
        p.setPopulationParticipants(POPULATION_PARTICIPANTS);
        p.setMethodOutcome(METHOD_OUTCOME);
        p.setMethodStudyDesign(METHOD_STUDY_DESIGN);
        p.setMethodStatistics(METHOD_STATISTICS);
        p.setMethodConfounders(METHOD_CONFOUNDERS);
        p.setExposurePollutant(EXPOSURE_POLLUTANT);
        p.setExposureAssessment(EXPOSURE_ASSESSMENT);
        p.setResultExposureRange(RESULT_EXPOSURE_RANGE);
        p.setResultEffectEstimate(RESULT_EFFECT_ESTIMATE);

        p.setPmId(PM_ID);

        p.addCode(new Code("1F", "Code1F", "", false, CodeClassId.CC1.getId(), "CC1", "CC1D", 1));
        p.addCode(new Code("4A", "Code4A", "", false, CodeClassId.CC4.getId(), "CC4", "CC4D", 1));
        p.addCode(new Code("4C", "Code4C", "", false, CodeClassId.CC4.getId(), "CC4", "CC4D", 3));
        p.addCode(new Code("7B", "Code7B", "", false, CodeClassId.CC7.getId(), "CC7", "CC7D", 2));
    }

}
