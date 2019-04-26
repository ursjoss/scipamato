package ch.difty.scipamato.core.persistence.paper;

import static ch.difty.scipamato.core.db.tables.Paper.PAPER;
import static ch.difty.scipamato.core.persistence.paper.PaperRecordMapperTest.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import ch.difty.scipamato.core.db.tables.records.PaperRecord;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.persistence.InsertSetStepSetter;
import ch.difty.scipamato.core.persistence.InsertSetStepSetterTest;

@SuppressWarnings("ResultOfMethodCallIgnored")
class PaperInsertSetStepSetterTest extends InsertSetStepSetterTest<PaperRecord, Paper> {

    private final InsertSetStepSetter<PaperRecord, Paper> setter = new PaperInsertSetStepSetter();

    @Mock
    private Paper entityMock;

    @Mock
    private PaperRecord recordMock;

    @Override
    protected InsertSetStepSetter<PaperRecord, Paper> getSetter() {
        return setter;
    }

    @Override
    protected Paper getEntity() {
        return entityMock;
    }

    @Override
    protected void specificTearDown() {
        verifyNoMoreInteractions(entityMock, recordMock);
    }

    @Override
    protected void entityFixture() {
        PaperRecordMapperTest.entityFixtureWithoutIdFields(entityMock);
    }

    @Override
    protected void stepSetFixtureExceptAudit() {
        doReturn(getMoreStep())
            .when(getStep())
            .set(PAPER.NUMBER, NUMBER);
        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(PAPER.PM_ID, PM_ID);

        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(PAPER.DOI, DOI);
        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(PAPER.AUTHORS, AUTHORS);
        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(PAPER.FIRST_AUTHOR, FIRST_AUTHOR);
        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(PAPER.FIRST_AUTHOR_OVERRIDDEN, FIRST_AUTHOR_OVERRIDDEN);
        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(PAPER.TITLE, TITLE);
        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(PAPER.LOCATION, LOCATION);
        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(PAPER.PUBLICATION_YEAR, PUBLICATION_YEAR);

        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(PAPER.GOALS, GOALS);
        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(PAPER.POPULATION, POPULATION);
        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(PAPER.METHODS, METHODS);

        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(PAPER.POPULATION_PLACE, POPULATION_PLACE);
        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(PAPER.POPULATION_PARTICIPANTS, POPULATION_PARTICIPANTS);
        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(PAPER.POPULATION_DURATION, POPULATION_DURATION);
        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(PAPER.EXPOSURE_POLLUTANT, EXPOSURE_POLLUTANT);
        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(PAPER.EXPOSURE_ASSESSMENT, EXPOSURE_ASSESSMENT);
        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(PAPER.METHOD_STUDY_DESIGN, METHOD_STUDY_DESIGN);
        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(PAPER.METHOD_OUTCOME, METHOD_OUTCOME);
        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(PAPER.METHOD_STATISTICS, METHOD_STATISTICS);
        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(PAPER.METHOD_CONFOUNDERS, METHOD_CONFOUNDERS);

        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(PAPER.RESULT, RESULT);
        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(PAPER.COMMENT, COMMENT);
        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(PAPER.INTERN, INTERN);

        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(PAPER.RESULT_EXPOSURE_RANGE, RESULT_EXPOSURE_RANGE);
        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(PAPER.RESULT_EFFECT_ESTIMATE, RESULT_EFFECT_ESTIMATE);
        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(PAPER.RESULT_MEASURED_OUTCOME, RESULT_MEASURED_OUTCOME);
        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(PAPER.CONCLUSION, CONCLUSION);

        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(PAPER.ORIGINAL_ABSTRACT, ORIGINAL_ABSTRACT);

        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(PAPER.MAIN_CODE_OF_CODECLASS1, MAIN_CODE_OF_CODECLASS1);
    }

    @Override
    protected void setStepFixtureAudit() {
        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(PAPER.CREATED_BY, PaperRecordMapperTest.CREATED_BY);
        doReturn(getMoreStep())
            .when(getMoreStep())
            .set(PAPER.LAST_MODIFIED_BY, PaperRecordMapperTest.LAST_MOD_BY);
    }

    @Override
    protected void verifyCallToFieldsExceptKeyAndAudit() {
        verify(entityMock).getNumber();
        verify(entityMock).getPmId();
        verify(entityMock).getDoi();
        verify(entityMock).getAuthors();
        verify(entityMock).getFirstAuthor();
        verify(entityMock).isFirstAuthorOverridden();
        verify(entityMock).getTitle();
        verify(entityMock).getLocation();
        verify(entityMock).getPublicationYear();

        verify(entityMock).getGoals();
        verify(entityMock).getPopulation();
        verify(entityMock).getMethods();

        verify(entityMock).getPopulationPlace();
        verify(entityMock).getPopulationParticipants();
        verify(entityMock).getPopulationDuration();
        verify(entityMock).getExposurePollutant();
        verify(entityMock).getExposureAssessment();
        verify(entityMock).getMethodStudyDesign();
        verify(entityMock).getMethodOutcome();
        verify(entityMock).getMethodStatistics();
        verify(entityMock).getMethodConfounders();

        verify(entityMock).getResult();
        verify(entityMock).getComment();
        verify(entityMock).getIntern();

        verify(entityMock).getResultExposureRange();
        verify(entityMock).getResultEffectEstimate();
        verify(entityMock).getResultMeasuredOutcome();
        verify(entityMock).getConclusion();

        verify(entityMock).getOriginalAbstract();

        verify(entityMock).getMainCodeOfCodeclass1();
    }

    @Override
    protected void verifySettingFieldsExceptKeyAndAudit() {
        verify(getStep()).set(PAPER.NUMBER, NUMBER);
        verify(getMoreStep()).set(PAPER.PM_ID, PM_ID);

        verify(getMoreStep()).set(PAPER.DOI, DOI);
        verify(getMoreStep()).set(PAPER.AUTHORS, AUTHORS);
        verify(getMoreStep()).set(PAPER.FIRST_AUTHOR, FIRST_AUTHOR);
        verify(getMoreStep()).set(PAPER.FIRST_AUTHOR_OVERRIDDEN, FIRST_AUTHOR_OVERRIDDEN);
        verify(getMoreStep()).set(PAPER.TITLE, TITLE);
        verify(getMoreStep()).set(PAPER.LOCATION, LOCATION);
        verify(getMoreStep()).set(PAPER.PUBLICATION_YEAR, PUBLICATION_YEAR);

        verify(getMoreStep()).set(PAPER.GOALS, GOALS);
        verify(getMoreStep()).set(PAPER.POPULATION, POPULATION);
        verify(getMoreStep()).set(PAPER.METHODS, METHODS);

        verify(getMoreStep()).set(PAPER.POPULATION_PLACE, POPULATION_PLACE);
        verify(getMoreStep()).set(PAPER.POPULATION_PARTICIPANTS, POPULATION_PARTICIPANTS);
        verify(getMoreStep()).set(PAPER.POPULATION_DURATION, POPULATION_DURATION);
        verify(getMoreStep()).set(PAPER.EXPOSURE_POLLUTANT, EXPOSURE_POLLUTANT);
        verify(getMoreStep()).set(PAPER.EXPOSURE_ASSESSMENT, EXPOSURE_ASSESSMENT);
        verify(getMoreStep()).set(PAPER.METHOD_STUDY_DESIGN, METHOD_STUDY_DESIGN);
        verify(getMoreStep()).set(PAPER.METHOD_OUTCOME, METHOD_OUTCOME);
        verify(getMoreStep()).set(PAPER.METHOD_STATISTICS, METHOD_STATISTICS);
        verify(getMoreStep()).set(PAPER.METHOD_CONFOUNDERS, METHOD_CONFOUNDERS);

        verify(getMoreStep()).set(PAPER.RESULT, RESULT);
        verify(getMoreStep()).set(PAPER.COMMENT, COMMENT);
        verify(getMoreStep()).set(PAPER.INTERN, INTERN);

        verify(getMoreStep()).set(PAPER.RESULT_EXPOSURE_RANGE, RESULT_EXPOSURE_RANGE);
        verify(getMoreStep()).set(PAPER.RESULT_EFFECT_ESTIMATE, RESULT_EFFECT_ESTIMATE);
        verify(getMoreStep()).set(PAPER.RESULT_MEASURED_OUTCOME, RESULT_MEASURED_OUTCOME);
        verify(getMoreStep()).set(PAPER.CONCLUSION, CONCLUSION);

        verify(getMoreStep()).set(PAPER.ORIGINAL_ABSTRACT, ORIGINAL_ABSTRACT);

        verify(getMoreStep()).set(PAPER.MAIN_CODE_OF_CODECLASS1, MAIN_CODE_OF_CODECLASS1);
    }

    @Override
    protected void verifySettingAuditFields() {
        verify(getMoreStep()).set(PAPER.CREATED_BY, PaperRecordMapperTest.CREATED_BY);
        verify(getMoreStep()).set(PAPER.LAST_MODIFIED_BY, PaperRecordMapperTest.LAST_MOD_BY);
    }

    @Test
    void consideringSettingKeyOf_withNullId_doesNotSetId() {
        when(getEntity().getId()).thenReturn(null);
        getSetter().considerSettingKeyOf(getMoreStep(), getEntity());
        verify(getEntity()).getId();
    }

    @Test
    void consideringSettingKeyOf_withNonNullId_doesSetId() {
        when(getEntity().getId()).thenReturn(ID);

        getSetter().considerSettingKeyOf(getMoreStep(), getEntity());

        verify(getEntity()).getId();
        verify(getMoreStep()).set(PAPER.ID, ID);
    }

    @Test
    void resettingIdToEntity_withNullRecord_doesNothing() {
        getSetter().resetIdToEntity(entityMock, null);
        verify(entityMock, never()).setId(anyLong());
    }

    @Test
    void resettingIdToEntity_withNonNullRecord_setsId() {
        when(recordMock.getId()).thenReturn(3L);
        getSetter().resetIdToEntity(entityMock, recordMock);
        verify(recordMock).getId();
        verify(entityMock).setId(anyLong());
    }

}
