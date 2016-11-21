package ch.difty.sipamato.persistance.jooq.paper.slim;

import static ch.difty.sipamato.db.tables.Paper.PAPER;

import java.util.List;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ch.difty.sipamato.db.Tables;
import ch.difty.sipamato.db.tables.records.PaperRecord;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.PaperFilter;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.lib.AssertAs;
import ch.difty.sipamato.persistance.jooq.GenericFilterConditionMapper;
import ch.difty.sipamato.persistance.jooq.JooqReadOnlyRepo;
import ch.difty.sipamato.persistance.jooq.JooqSortMapper;
import ch.difty.sipamato.service.Localization;

@Repository
public class JooqPaperSlimRepo extends JooqReadOnlyRepo<PaperRecord, PaperSlim, Long, ch.difty.sipamato.db.tables.Paper, PaperSlimRecordMapper, PaperFilter> implements PaperSlimRepository {

    private static final long serialVersionUID = 1L;

    @Autowired
    public JooqPaperSlimRepo(DSLContext dsl, PaperSlimRecordMapper mapper, JooqSortMapper<PaperRecord, PaperSlim, ch.difty.sipamato.db.tables.Paper> sortMapper,
            GenericFilterConditionMapper<PaperFilter> filterConditionMapper, Localization localization) {
        super(dsl, mapper, sortMapper, filterConditionMapper, localization);
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

    @Override
    public List<PaperSlim> findByExample(Paper example) {
        AssertAs.notNull(example, "example");
        // Query By example simply uses eq (without ignore case) and is thus not flexible enough.
        //        final List<PaperRecord> queryResults = getDsl().selectFrom(Tables.PAPER).where(DSL.condition(record)).fetchInto(getRecordClass());
        Condition c = extractConditions(example);
        final List<PaperRecord> queryResults = getDsl().selectFrom(Tables.PAPER).where(c).fetchInto(getRecordClass());
        final List<PaperSlim> entities = queryResults.stream().map(getMapper()::map).collect(Collectors.toList());
        enrichAssociatedEntitiesOfAll(entities);

        return entities;
    }

    private Condition extractConditions(Paper example) {
        // TODO also consider codes. Make possible to have Year null
        PaperRecord record = new PaperRecord();
        record.from(example);

        // TODO refactor into separate class hierarchy
        // TODO implement search patterns as follows:
        /*
            Strings:

            foo     likeIgnoreCase '%foo%'  "*foo*"
            "foo"   equalsIgnoreCase 'foo'
            foo*  likeIgnoreCase 'foo%'
            *foo  likeIgnoreCase '%foo'


            Numbers:

            2016        = 2016
            >2016       > 2016
            >=2016      >= 2016
            <2016       < 2016
            <=2016      <= 2016
            2016-2018   between 2016 and 2018
         */
        Condition c = DSL.trueCondition();
        int size = record.size();
        for (int i = 0; i < size; i++) {
            Object value = record.get(i);

            if (value != null) {
                Field f1 = record.field(i);
                Field f2 = DSL.val(value, f1.getDataType());
                Class<?> type = f1.getType();
                if (String.class == type) {
                    c = c.and(f1.lower().contains(f2.lower()));
                } else if (Boolean.class == type) {
                    if (((Boolean) value).booleanValue()) {
                        c = c.and(f1.equal(f2));
                    }
                } else {
                    c = c.and(f1.equal(f2));
                }
            }
        }
        return c;
    }

}
