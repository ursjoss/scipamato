package ch.difty.sipamato.persistance.jooq.paper.slim;

import static ch.difty.sipamato.db.tables.Paper.PAPER;

import org.jooq.DSLContext;
import org.jooq.TableField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ch.difty.sipamato.db.tables.records.PaperRecord;
import ch.difty.sipamato.entity.PaperFilter;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.persistance.jooq.GenericFilterConditionMapper;
import ch.difty.sipamato.persistance.jooq.JooqReadOnlyRepo;
import ch.difty.sipamato.persistance.jooq.JooqSortMapper;

@Repository
public class JooqPaperSlimRepo extends JooqReadOnlyRepo<PaperRecord, PaperSlim, Long, ch.difty.sipamato.db.tables.Paper, PaperSlimRecordMapper, PaperFilter> implements PaperSlimRepository {

    private static final long serialVersionUID = 1L;

    @Autowired
    public JooqPaperSlimRepo(DSLContext dsl, PaperSlimRecordMapper mapper, JooqSortMapper<PaperRecord, PaperSlim, ch.difty.sipamato.db.tables.Paper> sortMapper,
            GenericFilterConditionMapper<PaperFilter> filterConditionMapper) {
        super(dsl, mapper, sortMapper, filterConditionMapper);
    }

    @Override
    protected Class<? extends PaperSlim> getEntityClass() {
        return PaperSlim.class;
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

}
