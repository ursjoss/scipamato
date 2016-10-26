package ch.difty.sipamato.persistance.jooq.paper;

import static ch.difty.sipamato.db.tables.Code.CODE;
import static ch.difty.sipamato.db.tables.CodeClass.CODE_CLASS;
import static ch.difty.sipamato.db.tables.CodeClassTr.CODE_CLASS_TR;
import static ch.difty.sipamato.db.tables.CodeTr.CODE_TR;
import static ch.difty.sipamato.db.tables.Paper.PAPER;
import static ch.difty.sipamato.db.tables.PaperCode.PAPER_CODE;

import java.util.List;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ch.difty.sipamato.db.tables.records.PaperRecord;
import ch.difty.sipamato.entity.Code;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.PaperFilter;
import ch.difty.sipamato.lib.AssertAs;
import ch.difty.sipamato.lib.TranslationUtils;
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

    @Override
    public Paper findCompleteById(Long id, String languageCode) {
        AssertAs.notNull(id, "id");
        String lang = TranslationUtils.trimLanguageCode(languageCode);
        final Paper p = findById(id);
        if (p != null) {
            // @formatter:off
            final List<Code> codes = getDsl()
                .select(CODE.CODE_.as("C_ID")
                        , DSL.coalesce(CODE_TR.NAME, TranslationUtils.NOT_TRANSL).as("C_NAME")
                        , CODE_CLASS.ID.as("CC_ID")
                        , DSL.coalesce(CODE_CLASS_TR.NAME, TranslationUtils.NOT_TRANSL).as("CC_NAME")
                        , DSL.coalesce(CODE_CLASS_TR.DESCRIPTION, TranslationUtils.NOT_TRANSL).as("CC_DESCRIPTION"))
                .from(PAPER_CODE)
                .join(PAPER).on(PAPER_CODE.PAPER_ID.equal(PAPER.ID))
                .join(CODE).on(PAPER_CODE.CODE.equal(CODE.CODE_))
                .join(CODE_CLASS).on(CODE.CODE_CLASS_ID.equal(CODE_CLASS.ID))
                .leftOuterJoin(CODE_TR).on(CODE.CODE_.equal(CODE_TR.CODE).and(CODE_TR.LANG_CODE.equal(lang)))
                .leftOuterJoin(CODE_CLASS_TR).on(CODE_CLASS.ID.equal(CODE_CLASS_TR.CODE_CLASS_ID).and(CODE_CLASS_TR.LANG_CODE.equal(lang)))
                .where(PAPER_CODE.PAPER_ID.equal(p.getId()))
                .fetchInto(Code.class);
            // @formatter:on

            p.addCodes(codes);
        }
        return p;
    }

}
