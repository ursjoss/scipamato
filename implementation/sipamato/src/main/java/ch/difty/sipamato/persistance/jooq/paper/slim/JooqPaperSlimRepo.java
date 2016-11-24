package ch.difty.sipamato.persistance.jooq.paper.slim;

import static ch.difty.sipamato.db.tables.Paper.PAPER;

import java.util.List;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import ch.difty.sipamato.db.Tables;
import ch.difty.sipamato.db.tables.records.PaperRecord;
import ch.difty.sipamato.entity.ComplexPaperFilter;
import ch.difty.sipamato.entity.ComplexPaperFilter.BooleanSearchTerm;
import ch.difty.sipamato.entity.ComplexPaperFilter.IntegerSearchTerm;
import ch.difty.sipamato.entity.ComplexPaperFilter.StringSearchTerm;
import ch.difty.sipamato.entity.SimplePaperFilter;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.lib.AssertAs;
import ch.difty.sipamato.persistance.jooq.GenericFilterConditionMapper;
import ch.difty.sipamato.persistance.jooq.JooqReadOnlyRepo;
import ch.difty.sipamato.persistance.jooq.JooqSortMapper;
import ch.difty.sipamato.service.Localization;

@Repository
public class JooqPaperSlimRepo extends JooqReadOnlyRepo<PaperRecord, PaperSlim, Long, ch.difty.sipamato.db.tables.Paper, PaperSlimRecordMapper, SimplePaperFilter> implements PaperSlimRepository {

    private static final long serialVersionUID = 1L;

    @Autowired
    public JooqPaperSlimRepo(DSLContext dsl, PaperSlimRecordMapper mapper, JooqSortMapper<PaperRecord, PaperSlim, ch.difty.sipamato.db.tables.Paper> sortMapper,
            GenericFilterConditionMapper<SimplePaperFilter> filterConditionMapper, Localization localization) {
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
    public List<PaperSlim> findByFilter(final ComplexPaperFilter filter) {
        AssertAs.notNull(filter, "filter");
        // Query By example simply uses eq (without ignore case) and is thus not flexible enough.
        //        final List<PaperRecord> queryResults = getDsl().selectFrom(Tables.PAPER).where(DSL.condition(record)).fetchInto(getRecordClass());
        Condition c = extractConditions(filter);
        final List<PaperRecord> queryResults = getDsl().selectFrom(Tables.PAPER).where(c).fetchInto(getRecordClass());
        final List<PaperSlim> entities = queryResults.stream().map(getMapper()::map).collect(Collectors.toList());
        enrichAssociatedEntitiesOfAll(entities);

        return entities;
    }

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
    private Condition extractConditions(ComplexPaperFilter filter) {
        Condition c = DSL.trueCondition();
        for (BooleanSearchTerm st : filter.getBooleanSearchTerms()) {
            // TODO key to field name ???
            c = c.and(DSL.field(st.key).equal(DSL.val(st.rawValue)));
        }
        for (IntegerSearchTerm st : filter.getIntegerSearchTerms()) {
            c = c.and(DSL.field(st.key).equal(DSL.val(st.rawValue)));
        }
        for (StringSearchTerm st : filter.getStringSearchTerms()) {
            c = c.and(DSL.field(st.key).lower().contains(DSL.val(st.rawValue).lower()));
        }
        return c;
    }

    @Override
    public Page<PaperSlim> findByFilter(ComplexPaperFilter filter, Pageable pageable) {
        final List<PaperSlim> entities = findByFilter(filter);
        return new PageImpl<>(entities, pageable, (long) countByFilter(filter));
    }

    @Override
    public int countByFilter(ComplexPaperFilter filter) {
        final Condition conditions = extractConditions(filter);
        return getDsl().fetchCount(getDsl().selectOne().from(getTable()).where(conditions));
    }

}
