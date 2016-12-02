package ch.difty.sipamato.persistance.jooq.search;

import static ch.difty.sipamato.db.tables.SearchOrder.SEARCH_ORDER;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ch.difty.sipamato.db.tables.records.SearchOrderRecord;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.persistance.jooq.GenericFilterConditionMapper;
import ch.difty.sipamato.persistance.jooq.InsertSetStepSetter;
import ch.difty.sipamato.persistance.jooq.JooqEntityRepo;
import ch.difty.sipamato.persistance.jooq.JooqSortMapper;
import ch.difty.sipamato.persistance.jooq.UpdateSetStepSetter;
import ch.difty.sipamato.service.Localization;

@Repository
public class JooqSearchOrderRepo extends JooqEntityRepo<SearchOrderRecord, SearchOrder, Long, ch.difty.sipamato.db.tables.SearchOrder, SearchOrderRecordMapper, SearchOrderFilter>
        implements SearchOrderRepository {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(JooqSearchOrderRepo.class);

    @Autowired
    public JooqSearchOrderRepo(DSLContext dsl, SearchOrderRecordMapper mapper, JooqSortMapper<SearchOrderRecord, SearchOrder, ch.difty.sipamato.db.tables.SearchOrder> sortMapper,
            GenericFilterConditionMapper<SearchOrderFilter> filterConditionMapper, Localization localization, InsertSetStepSetter<SearchOrderRecord, SearchOrder> insertSetStepSetter,
            UpdateSetStepSetter<SearchOrderRecord, SearchOrder> updateSetStepSetter, Configuration jooqConfig) {
        super(dsl, mapper, sortMapper, filterConditionMapper, localization, insertSetStepSetter, updateSetStepSetter, jooqConfig);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected Class<? extends SearchOrder> getEntityClass() {
        return SearchOrder.class;
    }

    @Override
    protected Class<? extends SearchOrderRecord> getRecordClass() {
        return SearchOrderRecord.class;
    }

    @Override
    protected ch.difty.sipamato.db.tables.SearchOrder getTable() {
        return SEARCH_ORDER;
    }

    @Override
    protected TableField<SearchOrderRecord, Long> getTableId() {
        return SEARCH_ORDER.ID;
    }

    @Override
    protected Long getIdFrom(SearchOrderRecord record) {
        return record.getId();
    }

    @Override
    protected Long getIdFrom(SearchOrder entity) {
        return entity.getId();
    }

    //    @Override
    //    protected void enrichAssociatedEntitiesOf(SearchOrder entity) {
    //        if (entity != null) {
    //            final String localizationCode = getLocalization().getLocalization();
//            // @formatter:off
//            final List<Code> codes = getDsl()
//                .select(CODE.CODE_.as("C_ID")
//                        , DSL.coalesce(CODE_TR.NAME, TranslationUtils.NOT_TRANSL).as("C_NAME")
//                        , CODE_TR.COMMENT.as("C_COMMENT")
//                        , CODE.INTERNAL.as("C_INTERNAL")
//                        , CODE_CLASS.ID.as("CC_ID")
//                        , DSL.coalesce(CODE_CLASS_TR.NAME, TranslationUtils.NOT_TRANSL).as("CC_NAME")
//                        , DSL.coalesce(CODE_CLASS_TR.DESCRIPTION, TranslationUtils.NOT_TRANSL).as("CC_DESCRIPTION")
//                        , CODE.SORT)
//                .from(PAPER_CODE)
//                .join(PAPER).on(PAPER_CODE.PAPER_ID.equal(PAPER.ID))
//                .join(CODE).on(PAPER_CODE.CODE.equal(CODE.CODE_))
//                .join(CODE_CLASS).on(CODE.CODE_CLASS_ID.equal(CODE_CLASS.ID))
//                .leftOuterJoin(CODE_TR).on(CODE.CODE_.equal(CODE_TR.CODE).and(CODE_TR.LANG_CODE.equal(localizationCode)))
//                .leftOuterJoin(CODE_CLASS_TR).on(CODE_CLASS.ID.equal(CODE_CLASS_TR.CODE_CLASS_ID).and(CODE_CLASS_TR.LANG_CODE.equal(localizationCode)))
//                .where(PAPER_CODE.PAPER_ID.equal(entity.getId()))
//                .fetchInto(Code.class);
//            // @formatter:on
    //            if (CollectionUtils.isNotEmpty(codes)) {
    //                entity.addCodes(codes);
    //            }
    //        }
    //    }
    //
    //    @Override
    //    protected void saveAssociatedEntitiesOf(SearchOrder paper) {
    //        storeNewCodesOf(paper);
    //    }
    //
    //    @Override
    //    protected void updateAssociatedEntities(SearchOrder paper) {
    //        storeNewCodesOf(paper);
    //        deleteObsoleteCodesFrom(paper);
    //    }
    //
    //    private void storeNewCodesOf(SearchOrder paper) {
    //        InsertValuesStep2<PaperCodeRecord, Long, String> step = getDsl().insertInto(PAPER_CODE, PAPER_CODE.PAPER_ID, PAPER_CODE.CODE);
    //        final Long paperId = paper.getId();
    //        for (final Code c : paper.getCodes()) {
    //            step = step.values(paperId, c.getCode());
    //        }
    //        step.onDuplicateKeyIgnore().execute();
    //    }
    //
    //    private void deleteObsoleteCodesFrom(SearchOrder paper) {
    //        final List<String> codes = paper.getCodes().stream().map(Code::getCode).collect(Collectors.toList());
    //        getDsl().deleteFrom(PAPER_CODE).where(PAPER_CODE.PAPER_ID.equal(paper.getId()).and(PAPER_CODE.CODE.notIn(codes))).execute();
    //    }

}
