package ch.difty.scipamato.persistance.jooq.paper;

import static ch.difty.scipamato.db.tables.Paper.PAPER;

import java.util.List;

import org.jooq.Condition;

import ch.difty.scipamato.persistance.jooq.AbstractFilterConditionMapper;
import ch.difty.scipamato.persistance.jooq.FilterConditionMapper;

/**
 * Mapper turning the provider {@link PaperFilter} into a jOOQ {@link Condition}.
 *
 * @author u.joss
 */
@FilterConditionMapper
public class PaperFilterConditionMapper extends AbstractFilterConditionMapper<PaperFilter> {

    @Override
    public void map(final PaperFilter filter, final List<Condition> conditions) {
        if (filter.getNumber() != null) {
            conditions.add(PAPER.NUMBER.eq(filter.getNumber()));
        }

        if (filter.getAuthorMask() != null) {
            final String likeExpression = "%" + filter.getAuthorMask() + "%";
            conditions.add(PAPER.FIRST_AUTHOR.likeIgnoreCase(likeExpression).or(PAPER.AUTHORS.likeIgnoreCase(likeExpression)));
        }

        if (filter.getMethodsMask() != null) {
            final String likeExpression = "%" + filter.getMethodsMask() + "%";
            conditions.add(PAPER.EXPOSURE_POLLUTANT.likeIgnoreCase(likeExpression)
                    .or(PAPER.EXPOSURE_ASSESSMENT.likeIgnoreCase(likeExpression))
                    .or(PAPER.METHODS.likeIgnoreCase(likeExpression))
                    .or(PAPER.METHOD_STUDY_DESIGN.likeIgnoreCase(likeExpression))
                    .or(PAPER.METHOD_OUTCOME.likeIgnoreCase(likeExpression))
                    .or(PAPER.METHOD_STATISTICS.likeIgnoreCase(likeExpression))
                    .or(PAPER.METHOD_CONFOUNDERS.likeIgnoreCase(likeExpression)));
        }

        if (filter.getSearchMask() != null) {
            final String likeExpression = "%" + filter.getSearchMask() + "%";
            conditions.add(PAPER.DOI.likeIgnoreCase(likeExpression)
                    .or(PAPER.LOCATION.likeIgnoreCase(likeExpression))
                    .or(PAPER.TITLE.likeIgnoreCase(likeExpression))
                    .or(PAPER.GOALS.likeIgnoreCase(likeExpression))
                    .or(PAPER.POPULATION.likeIgnoreCase(likeExpression))
                    .or(PAPER.POPULATION_PLACE.likeIgnoreCase(likeExpression))
                    .or(PAPER.POPULATION_PARTICIPANTS.likeIgnoreCase(likeExpression))
                    .or(PAPER.POPULATION_DURATION.likeIgnoreCase(likeExpression))
                    .or(PAPER.RESULT.likeIgnoreCase(likeExpression))
                    .or(PAPER.RESULT_EXPOSURE_RANGE.likeIgnoreCase(likeExpression))
                    .or(PAPER.RESULT_EFFECT_ESTIMATE.likeIgnoreCase(likeExpression))
                    .or(PAPER.RESULT_MEASURED_OUTCOME.likeIgnoreCase(likeExpression))
                    .or(PAPER.COMMENT.likeIgnoreCase(likeExpression))
                    .or(PAPER.INTERN.likeIgnoreCase(likeExpression))
                    .or(PAPER.ORIGINAL_ABSTRACT.likeIgnoreCase(likeExpression)));
        }

        if (filter.getPublicationYearFrom() != null) {
            conditions.add(PAPER.PUBLICATION_YEAR.ge(filter.getPublicationYearFrom()));
        }

        if (filter.getPublicationYearUntil() != null) {
            conditions.add(PAPER.PUBLICATION_YEAR.le(filter.getPublicationYearUntil()));
        }
    }

}
