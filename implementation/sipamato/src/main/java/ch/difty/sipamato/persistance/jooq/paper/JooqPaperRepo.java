package ch.difty.sipamato.persistance.jooq.paper;

import static ch.difty.sipamato.db.h2.tables.Paper.PAPER;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ch.difty.sipamato.db.h2.tables.records.PaperRecord;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.PaperFilter;
import ch.difty.sipamato.persistance.jooq.InsertSetStepSetter;
import ch.difty.sipamato.persistance.jooq.JooqRepo;
import ch.difty.sipamato.persistance.jooq.UpdateSetStepSetter;

@Repository
public class JooqPaperRepo extends JooqRepo<PaperRecord, Paper, Long, ch.difty.sipamato.db.h2.tables.Paper, PaperRecordMapper, PaperFilter> implements PaperRepository {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(JooqPaperRepo.class);

    @Autowired
    public JooqPaperRepo(DSLContext dsl, PaperRecordMapper mapper, InsertSetStepSetter<PaperRecord, Paper> insertSetStepSetter, UpdateSetStepSetter<PaperRecord, Paper> updateSetStepSetter,
            Configuration jooqConfig) {
        super(dsl, mapper, insertSetStepSetter, updateSetStepSetter, jooqConfig);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected Class<? extends Paper> getEntityClass() {
        return Paper.class;
    }

    @Override
    protected Class<? extends PaperRecord> getRecordClass() {
        return PaperRecord.class;
    }

    @Override
    protected ch.difty.sipamato.db.h2.tables.Paper getTable() {
        return PAPER;
    }

    @Override
    protected TableField<PaperRecord, Long> getTableId() {
        return PAPER.ID;
    }

    @Override
    protected Long getIdFrom(PaperRecord record) {
        return record.getId();
    }

    @Override
    protected Long getIdFrom(Paper entity) {
        return entity.getId();
    }

    @Override
    protected Condition createWhereConditions(PaperFilter filter) {
        final List<Condition> conditions = new ArrayList<>();

        if (filter.getAuthorMask() != null) {
            String likeExpression = "%" + filter.getAuthorMask() + "%";
            conditions.add(PAPER.FIRST_AUTHOR.likeIgnoreCase(likeExpression).or(PAPER.AUTHORS.likeIgnoreCase(likeExpression)));
        }

        if (filter.getMethodsMask() != null) {
            String likeExpression = "%" + filter.getMethodsMask() + "%";
            conditions.add(PAPER.EXPOSURE_POLLUTANT.likeIgnoreCase(likeExpression)
                    .or(PAPER.EXPOSURE_ASSESSMENT.likeIgnoreCase(likeExpression))
                    .or(PAPER.METHODS.likeIgnoreCase(likeExpression))
                    .or(PAPER.METHOD_STUDY_DESIGN.likeIgnoreCase(likeExpression))
                    .or(PAPER.METHOD_OUTCOME.likeIgnoreCase(likeExpression))
                    .or(PAPER.METHOD_STATISTICS.likeIgnoreCase(likeExpression))
                    .or(PAPER.METHOD_CONFOUNDERS.likeIgnoreCase(likeExpression)));
        }

        if (filter.getSearchMask() != null) {
            String likeExpression = "%" + filter.getSearchMask() + "%";
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
                    .or(PAPER.COMMENT.likeIgnoreCase(likeExpression))
                    .or(PAPER.INTERN.likeIgnoreCase(likeExpression)));
        }

        if (filter.getPublicationYearFrom() != null) {
            conditions.add(PAPER.PUBLICATION_YEAR.ge(filter.getPublicationYearFrom()));
        }

        if (filter.getPublicationYearUntil() != null) {
            conditions.add(PAPER.PUBLICATION_YEAR.le(filter.getPublicationYearUntil()));
        }
        return DSL.and(conditions);
    }

}
