package ch.difty.sipamato.persistance.jooq.paper;

import static ch.difty.sipamato.db.tables.Paper.PAPER;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ch.difty.sipamato.db.tables.records.PaperRecord;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.PaperFilter;
import ch.difty.sipamato.persistance.jooq.GenericFilterConditionMapper;
import ch.difty.sipamato.persistance.jooq.InsertSetStepSetter;
import ch.difty.sipamato.persistance.jooq.JooqEntityRepo;
import ch.difty.sipamato.persistance.jooq.JooqSortMapper;
import ch.difty.sipamato.persistance.jooq.UpdateSetStepSetter;

@Repository
public class JooqPaperRepo extends JooqEntityRepo<PaperRecord, Paper, Long, ch.difty.sipamato.db.tables.Paper, PaperRecordMapper, PaperFilter> implements PaperRepository {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(JooqPaperRepo.class);

    @Autowired
    public JooqPaperRepo(DSLContext dsl, PaperRecordMapper mapper, JooqSortMapper<PaperRecord, Paper, ch.difty.sipamato.db.tables.Paper> sortMapper,
            GenericFilterConditionMapper<PaperFilter> filterConditionMapper, InsertSetStepSetter<PaperRecord, Paper> insertSetStepSetter, UpdateSetStepSetter<PaperRecord, Paper> updateSetStepSetter,
            Configuration jooqConfig) {
        super(dsl, mapper, sortMapper, filterConditionMapper, insertSetStepSetter, updateSetStepSetter, jooqConfig);
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
    protected ch.difty.sipamato.db.tables.Paper getTable() {
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

}
