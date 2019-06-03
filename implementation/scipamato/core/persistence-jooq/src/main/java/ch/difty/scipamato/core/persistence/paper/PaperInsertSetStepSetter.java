package ch.difty.scipamato.core.persistence.paper;

import static ch.difty.scipamato.core.db.tables.Paper.PAPER;

import org.jooq.InsertSetMoreStep;
import org.jooq.InsertSetStep;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.core.db.tables.records.PaperRecord;
import ch.difty.scipamato.core.entity.Code;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.persistence.InsertSetStepSetter;

/**
 * The insert step setter used for inserting new {@link Paper}s.
 *
 *
 * <b>Note:</b> the {@link Code}s are not inserted here.
 *
 * @author u.joss
 */
@Component
public class PaperInsertSetStepSetter implements InsertSetStepSetter<PaperRecord, Paper> {

    @Override
    public InsertSetMoreStep<PaperRecord> setNonKeyFieldsFor(InsertSetStep<PaperRecord> step, Paper e) {
        AssertAs.notNull(step, "step");
        AssertAs.notNull(e, "entity");

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
            .set(PAPER.CONCLUSION, e.getConclusion())

            .set(PAPER.ORIGINAL_ABSTRACT, e.getOriginalAbstract())

            .set(PAPER.MAIN_CODE_OF_CODECLASS1, e.getMainCodeOfCodeclass1())

            .set(PAPER.CREATED_BY, e.getCreatedBy())
            .set(PAPER.LAST_MODIFIED_BY, e.getLastModifiedBy());
    }

    @Override
    public void considerSettingKeyOf(InsertSetMoreStep<PaperRecord> step, Paper entity) {
        AssertAs.notNull(step, "step");
        AssertAs.notNull(entity, "entity");
        Long id = entity.getId();
        if (id != null)
            step.set(PAPER.ID, id);
    }

    @Override
    public void resetIdToEntity(Paper entity, PaperRecord saved) {
        if (saved != null)
            entity.setId(saved.getId());
    }

}
