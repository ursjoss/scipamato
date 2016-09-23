package ch.difty.sipamato.persistance.jooq.stepsetter;

import static ch.difty.sipamato.db.h2.tables.Paper.PAPER;

import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateSetMoreStep;
import org.springframework.stereotype.Component;

import ch.difty.sipamato.db.h2.tables.records.PaperRecord;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.lib.Asserts;
import ch.difty.sipamato.persistance.jooq.repo.UpdateSetStepSetter;

@Component
public class PaperUpdateSetStepSetter implements UpdateSetStepSetter<PaperRecord, Paper> {

    /** {@inheritDoc} */
    @Override
    public UpdateSetMoreStep<PaperRecord> setFieldsFor(UpdateSetFirstStep<PaperRecord> step, Paper e) {
        Asserts.notNull(step, "step");
        Asserts.notNull(e, "entity");
        Long id = e.getId();
        Asserts.notNull(id, "entity.id");
        // @formatter:off
        return step
            .set(PAPER.ID, id.longValue())
            .set(PAPER.PM_ID, e.getPmid())
            .set(PAPER.DOI, e.getDoi())
            .set(PAPER.AUTHORS, e.getAuthors())
            .set(PAPER.FIRST_AUTHOR, e.getFirstAuthor())
            .set(PAPER.FIRST_AUTHOR_OVERRIDDEN, e.isFirstAuthorOverridden())
            .set(PAPER.TITLE, e.getTitle())
            .set(PAPER.LOCATION, e.getLocation())
            .set(PAPER.PUBLICATION_YEAR, e.getPublicationYear())

            .set(PAPER.GOALS, e.getGoals())
            .set(PAPER.POPULATION, e.getPopulation())
            .set(PAPER.EXPOSURE, e.getExposure())
            .set(PAPER.METHODS, e.getMethods())

            .set(PAPER.POPULATION_PLACE, e.getPopulationPlace())
            .set(PAPER.POPULATION_PARTICIPANTS, e.getPopulationParticipants())
            .set(PAPER.POPULATION_DURATION, e.getPopulationDuration())
            .set(PAPER.EXPOSURE_POLLUTANT, e.getExposurePollutant())
            .set(PAPER.EXPOSURE_ASSESSMENT, e.getExposureAssessment())
            .set(PAPER.METHOD_OUTCOME, e.getMethodOutcome())
            .set(PAPER.METHOD_STATISTICS, e.getMethodStatistics())
            .set(PAPER.METHOD_CONFOUNDERS, e.getMethodConfounders())

            .set(PAPER.RESULT, e.getResult())
            .set(PAPER.COMMENT, e.getComment())
            .set(PAPER.INTERN, e.getIntern())

            .set(PAPER.RESULT_EXPOSURE_RANGE, e.getResultExposureRange())
            .set(PAPER.RESULT_EFFECT_ESTIMATE, e.getResultEffectEstimate());
         // @formatter:on
    }

}
