package ch.difty.scipamato.persistence;

import static ch.difty.scipamato.db.tables.Paper.*;

import java.util.List;

import org.jooq.Condition;

import ch.difty.scipamato.entity.PopulationCode;
import ch.difty.scipamato.entity.StudyDesignCode;
import ch.difty.scipamato.entity.filter.PublicPaperFilter;

@FilterConditionMapper
public class PublicPaperFilterConditionMapper extends AbstractFilterConditionMapper<PublicPaperFilter> {

    @Override
    protected void map(final PublicPaperFilter filter, final List<Condition> conditions) {
        if (filter.getNumber() != null) {
            conditions.add(PAPER.NUMBER.eq(filter.getNumber()));
        }

        if (filter.getAuthorMask() != null) {
            final String likeExpression = "%" + filter.getAuthorMask() + "%";
            conditions.add(PAPER.AUTHORS.likeIgnoreCase(likeExpression));
        }

        if (filter.getMethodsMask() != null) {
            final String likeExpression = "%" + filter.getMethodsMask() + "%";
            conditions.add(PAPER.METHODS.likeIgnoreCase(likeExpression));
        }

        if (filter.getPublicationYearFrom() != null) {
            conditions.add(PAPER.PUBLICATION_YEAR.ge(filter.getPublicationYearFrom()));
        }

        if (filter.getPublicationYearUntil() != null) {
            conditions.add(PAPER.PUBLICATION_YEAR.le(filter.getPublicationYearUntil()));
        }

        if (filter.getPopulationCodes() != null) {
            final Short[] ids = filter.getPopulationCodes().stream().map(PopulationCode::getId).toArray(Short[]::new);
            conditions.add(PAPER.CODES_POPULATION.contains(ids));
        }

        if (filter.getStudyDesignCodes() != null) {
            final Short[] ids = filter.getStudyDesignCodes().stream().map(StudyDesignCode::getId).toArray(Short[]::new);
            conditions.add(PAPER.CODES_STUDY_DESIGN.contains(ids));
        }
    }

}