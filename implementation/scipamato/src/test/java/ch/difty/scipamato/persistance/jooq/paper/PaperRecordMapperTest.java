package ch.difty.scipamato.persistance.jooq.paper;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.jooq.RecordMapper;

import ch.difty.scipamato.db.tables.records.PaperRecord;
import ch.difty.scipamato.entity.Paper;
import ch.difty.scipamato.persistance.jooq.RecordMapperTest;

public class PaperRecordMapperTest extends RecordMapperTest<PaperRecord, Paper> {

    public static final Long ID = 1l;
    public static final Long NUMBER = 10l;
    public static final int PM_ID = 2;
    public static final String DOI = "101000/1234";
    public static final String AUTHORS = "authors";
    public static final String FIRST_AUTHOR = "first author";
    public static final boolean FIRST_AUTHOR_OVERRIDDEN = false;
    public static final String TITLE = "title";
    public static final String LOCATION = "location";
    public static final Integer PUBLICATION_YEAR = 3;
    public static final String GOALS = "goals";
    public static final String POPULATION = "population";
    public static final String METHODS = "methods";
    public static final String POPULATION_PLACE = "population place";
    public static final String POPULATION_PARTICIPANTS = "population  participants";
    public static final String POPULATION_DURATION = "population duration";
    public static final String EXPOSURE_POLLUTANT = "exposure pollutant";
    public static final String EXPOSURE_ASSESSMENT = "exporsure assessment";
    public static final String METHOD_STUDY_DESIGN = "method study design";
    public static final String METHOD_OUTCOME = "method outcome";
    public static final String METHOD_STATISTICS = "method statistics";
    public static final String METHOD_CONFOUNDERS = "method confounders";
    public static final String RESULT = "result";
    public static final String COMMENT = "comment";
    public static final String INTERN = "intern";
    public static final String RESULT_EXPOSURE_RANGE = "result exposure range";
    public static final String RESULT_EFFECT_ESTIMATE = "result effect estimate";
    public static final String RESULT_MEASURED_OUTCOME = "result measured outcome";
    public static final String ORIGINAL_ABSTRACT = "oa";
    public static final String MAIN_CODE_OF_CODECLASS1 = "1F";

    public static void entityFixtureWithoutIdFields(Paper entityMock) {
        when(entityMock.getNumber()).thenReturn(NUMBER);
        when(entityMock.getPmId()).thenReturn(PM_ID);
        when(entityMock.getDoi()).thenReturn(DOI);
        when(entityMock.getAuthors()).thenReturn(AUTHORS);
        when(entityMock.getFirstAuthor()).thenReturn(FIRST_AUTHOR);
        when(entityMock.isFirstAuthorOverridden()).thenReturn(FIRST_AUTHOR_OVERRIDDEN);
        when(entityMock.getTitle()).thenReturn(TITLE);
        when(entityMock.getLocation()).thenReturn(LOCATION);
        when(entityMock.getPublicationYear()).thenReturn(PUBLICATION_YEAR);

        when(entityMock.getGoals()).thenReturn(GOALS);
        when(entityMock.getPopulation()).thenReturn(POPULATION);
        when(entityMock.getMethods()).thenReturn(METHODS);

        when(entityMock.getPopulationPlace()).thenReturn(POPULATION_PLACE);
        when(entityMock.getPopulationParticipants()).thenReturn(POPULATION_PARTICIPANTS);
        when(entityMock.getPopulationDuration()).thenReturn(POPULATION_DURATION);
        when(entityMock.getExposurePollutant()).thenReturn(EXPOSURE_POLLUTANT);
        when(entityMock.getExposureAssessment()).thenReturn(EXPOSURE_ASSESSMENT);
        when(entityMock.getMethodStudyDesign()).thenReturn(METHOD_STUDY_DESIGN);
        when(entityMock.getMethodOutcome()).thenReturn(METHOD_OUTCOME);
        when(entityMock.getMethodStatistics()).thenReturn(METHOD_STATISTICS);
        when(entityMock.getMethodConfounders()).thenReturn(METHOD_CONFOUNDERS);

        when(entityMock.getResult()).thenReturn(RESULT);
        when(entityMock.getComment()).thenReturn(COMMENT);
        when(entityMock.getIntern()).thenReturn(INTERN);

        when(entityMock.getResultExposureRange()).thenReturn(RESULT_EXPOSURE_RANGE);
        when(entityMock.getResultEffectEstimate()).thenReturn(RESULT_EFFECT_ESTIMATE);
        when(entityMock.getResultMeasuredOutcome()).thenReturn(RESULT_MEASURED_OUTCOME);

        when(entityMock.getOriginalAbstract()).thenReturn(ORIGINAL_ABSTRACT);

        when(entityMock.getMainCodeOfCodeclass1()).thenReturn(MAIN_CODE_OF_CODECLASS1);

        auditFixtureFor(entityMock);
    }

    @Override
    protected RecordMapper<PaperRecord, Paper> getMapper() {
        return new PaperRecordMapper();
    }

    @Override
    protected PaperRecord makeRecord() {
        PaperRecord record = new PaperRecord();
        record.setId(ID);
        record.setNumber(NUMBER);
        record.setPmId(PM_ID);
        record.setDoi(DOI);
        record.setAuthors(AUTHORS);
        record.setFirstAuthor(FIRST_AUTHOR);
        record.setFirstAuthorOverridden(FIRST_AUTHOR_OVERRIDDEN);
        record.setTitle(TITLE);
        record.setLocation(LOCATION);
        record.setPublicationYear(PUBLICATION_YEAR);

        record.setGoals(GOALS);
        record.setPopulation(POPULATION);
        record.setMethods(METHODS);

        record.setPopulationPlace(POPULATION_PLACE);
        record.setPopulationParticipants(POPULATION_PARTICIPANTS);
        record.setPopulationDuration(POPULATION_DURATION);
        record.setExposurePollutant(EXPOSURE_POLLUTANT);
        record.setExposureAssessment(EXPOSURE_ASSESSMENT);
        record.setMethodStudyDesign(METHOD_STUDY_DESIGN);
        record.setMethodOutcome(METHOD_OUTCOME);
        record.setMethodStatistics(METHOD_STATISTICS);
        record.setMethodConfounders(METHOD_CONFOUNDERS);

        record.setResult(RESULT);
        record.setComment(COMMENT);
        record.setIntern(INTERN);

        record.setResultExposureRange(RESULT_EXPOSURE_RANGE);
        record.setResultEffectEstimate(RESULT_EFFECT_ESTIMATE);
        record.setResultMeasuredOutcome(RESULT_MEASURED_OUTCOME);

        record.setOriginalAbstract(ORIGINAL_ABSTRACT);

        record.setMainCodeOfCodeclass1(MAIN_CODE_OF_CODECLASS1);

        return record;
    }

    @Override
    protected void setAuditFieldsIn(PaperRecord r) {
        r.setCreated(CREATED);
        r.setCreatedBy(CREATED_BY);
        r.setLastModified(LAST_MOD);
        r.setLastModifiedBy(LAST_MOD_BY);
        r.setVersion(VERSION);
    }

    @Override
    protected void assertEntity(Paper e) {
        assertThat(e.getId()).isEqualTo(ID.longValue());
        assertThat(e.getNumber()).isEqualTo(NUMBER.longValue());
        assertThat(e.getPmId()).isEqualTo(PM_ID);
        assertThat(e.getDoi()).isEqualTo(DOI);
        assertThat(e.getAuthors()).isEqualTo(AUTHORS);
        assertThat(e.getFirstAuthor()).isEqualTo(FIRST_AUTHOR);
        assertThat(e.isFirstAuthorOverridden()).isEqualTo(FIRST_AUTHOR_OVERRIDDEN);
        assertThat(e.getTitle()).isEqualTo(TITLE);
        assertThat(e.getLocation()).isEqualTo(LOCATION);
        assertThat(e.getPublicationYear()).isEqualTo(PUBLICATION_YEAR);

        assertThat(e.getGoals()).isEqualTo(GOALS);
        assertThat(e.getPopulation()).isEqualTo(POPULATION);
        assertThat(e.getMethods()).isEqualTo(METHODS);

        assertThat(e.getPopulationPlace()).isEqualTo(POPULATION_PLACE);
        assertThat(e.getPopulationParticipants()).isEqualTo(POPULATION_PARTICIPANTS);
        assertThat(e.getPopulationDuration()).isEqualTo(POPULATION_DURATION);
        assertThat(e.getExposurePollutant()).isEqualTo(EXPOSURE_POLLUTANT);
        assertThat(e.getExposureAssessment()).isEqualTo(EXPOSURE_ASSESSMENT);
        assertThat(e.getMethodStudyDesign()).isEqualTo(METHOD_STUDY_DESIGN);
        assertThat(e.getMethodOutcome()).isEqualTo(METHOD_OUTCOME);
        assertThat(e.getMethodStatistics()).isEqualTo(METHOD_STATISTICS);
        assertThat(e.getMethodConfounders()).isEqualTo(METHOD_CONFOUNDERS);

        assertThat(e.getResult()).isEqualTo(RESULT);
        assertThat(e.getComment()).isEqualTo(COMMENT);
        assertThat(e.getIntern()).isEqualTo(INTERN);

        assertThat(e.getResultExposureRange()).isEqualTo(RESULT_EXPOSURE_RANGE);
        assertThat(e.getResultEffectEstimate()).isEqualTo(RESULT_EFFECT_ESTIMATE);
        assertThat(e.getResultMeasuredOutcome()).isEqualTo(RESULT_MEASURED_OUTCOME);

        assertThat(e.getOriginalAbstract()).isEqualTo(ORIGINAL_ABSTRACT);

        assertThat(e.getMainCodeOfCodeclass1()).isEqualTo(MAIN_CODE_OF_CODECLASS1);

        assertThat(e.getCodes()).isEmpty();
    }

}
