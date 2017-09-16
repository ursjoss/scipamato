package ch.difty.scipamato.persistance.jooq.paper;

import static ch.difty.scipamato.db.tables.Paper.*;

import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateSetMoreStep;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.db.tables.records.PaperRecord;
import ch.difty.scipamato.entity.Code;
import ch.difty.scipamato.entity.Paper;
import ch.difty.scipamato.lib.AssertAs;
import ch.difty.scipamato.lib.DateUtils;
import ch.difty.scipamato.persistance.jooq.UpdateSetStepSetter;

/**
 * The update step setter used for updating {@link Paper}s.<p>
 *
 * <b>Note:</b> the {@link Code}s are not updated here.
 *
 * @author u.joss
 */
@Component
public class PaperUpdateSetStepSetter implements UpdateSetStepSetter<PaperRecord, Paper> {

    /** {@inheritDoc} */
    @Override
    public UpdateSetMoreStep<PaperRecord> setFieldsFor(UpdateSetFirstStep<PaperRecord> step, Paper e) {
        AssertAs.notNull(step, "step");
        AssertAs.notNull(e, "entity");
        // @formatter:off
        return step
            .set(PAPER.NUMBER, e.getNumber())
            .set(PAPER.PM_ID, e.getPmId())
            .set(PAPER.DOI, e.getDoi())
            .set(PAPER.AUTHORS, e.getAuthors())
            .set(PAPER.FIRST_AUTHOR, e.getFirstAuthor())
            .set(PAPER.FIRST_AUTHOR_OVERRIDDEN, e.isFirstAuthorOverridden())
            .set(PAPER.TITLE, e.getTitle())
            .set(PAPER.LOCATION, e.getLocation())
            .set(PAPER.PUBLICATION_YEAR, e.getPublicationYear())

            .set(PAPER.GOALS, e.getGoals())
            .set(PAPER.POPULATION, e.getPopulation())
            .set(PAPER.METHODS, e.getMethods())

            .set(PAPER.POPULATION_PLACE, e.getPopulationPlace())
            .set(PAPER.POPULATION_PARTICIPANTS, e.getPopulationParticipants())
            .set(PAPER.POPULATION_DURATION, e.getPopulationDuration())
            .set(PAPER.EXPOSURE_POLLUTANT, e.getExposurePollutant())
            .set(PAPER.EXPOSURE_ASSESSMENT, e.getExposureAssessment())
            .set(PAPER.METHOD_STUDY_DESIGN, e.getMethodStudyDesign())
            .set(PAPER.METHOD_OUTCOME, e.getMethodOutcome())
            .set(PAPER.METHOD_STATISTICS, e.getMethodStatistics())
            .set(PAPER.METHOD_CONFOUNDERS, e.getMethodConfounders())

            .set(PAPER.RESULT, e.getResult())
            .set(PAPER.COMMENT, e.getComment())
            .set(PAPER.INTERN, e.getIntern())

            .set(PAPER.RESULT_EXPOSURE_RANGE, e.getResultExposureRange())
            .set(PAPER.RESULT_EFFECT_ESTIMATE, e.getResultEffectEstimate())
            .set(PAPER.RESULT_MEASURED_OUTCOME, e.getResultMeasuredOutcome())

            .set(PAPER.ORIGINAL_ABSTRACT, e.getOriginalAbstract())

            .set(PAPER.MAIN_CODE_OF_CODECLASS1, e.getMainCodeOfCodeclass1())

            .set(PAPER.CREATED, DateUtils.tsOf(e.getCreated()))
            .set(PAPER.CREATED_BY, e.getCreatedBy())
            .set(PAPER.LAST_MODIFIED, DateUtils.tsOf(e.getLastModified()))
            .set(PAPER.LAST_MODIFIED_BY, e.getLastModifiedBy())
            .set(PAPER.VERSION, e.getVersion() + 1);
         // @formatter:on
    }

}
