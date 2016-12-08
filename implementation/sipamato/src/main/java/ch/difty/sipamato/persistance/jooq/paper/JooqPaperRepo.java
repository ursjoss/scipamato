package ch.difty.sipamato.persistance.jooq.paper;

import static ch.difty.sipamato.db.tables.Code.CODE;
import static ch.difty.sipamato.db.tables.CodeClass.CODE_CLASS;
import static ch.difty.sipamato.db.tables.CodeClassTr.CODE_CLASS_TR;
import static ch.difty.sipamato.db.tables.CodeTr.CODE_TR;
import static ch.difty.sipamato.db.tables.Paper.PAPER;
import static ch.difty.sipamato.db.tables.PaperCode.PAPER_CODE;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep2;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ch.difty.sipamato.db.tables.records.PaperCodeRecord;
import ch.difty.sipamato.db.tables.records.PaperRecord;
import ch.difty.sipamato.entity.Code;
import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.lib.TranslationUtils;
import ch.difty.sipamato.persistance.jooq.GenericFilterConditionMapper;
import ch.difty.sipamato.persistance.jooq.InsertSetStepSetter;
import ch.difty.sipamato.persistance.jooq.JooqEntityRepo;
import ch.difty.sipamato.persistance.jooq.JooqSortMapper;
import ch.difty.sipamato.persistance.jooq.UpdateSetStepSetter;
import ch.difty.sipamato.service.Localization;

@Repository
public class JooqPaperRepo extends JooqEntityRepo<PaperRecord, Paper, Long, ch.difty.sipamato.db.tables.Paper, PaperRecordMapper, PaperFilter> implements PaperRepository {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(JooqPaperRepo.class);

    @Autowired
    public JooqPaperRepo(DSLContext dsl, PaperRecordMapper mapper, JooqSortMapper<PaperRecord, Paper, ch.difty.sipamato.db.tables.Paper> sortMapper,
            GenericFilterConditionMapper<PaperFilter> filterConditionMapper, Localization localization, InsertSetStepSetter<PaperRecord, Paper> insertSetStepSetter,
            UpdateSetStepSetter<PaperRecord, Paper> updateSetStepSetter, Configuration jooqConfig) {
        super(dsl, mapper, sortMapper, filterConditionMapper, localization, insertSetStepSetter, updateSetStepSetter, jooqConfig);
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
    protected void enrichAssociatedEntitiesOf(Paper entity) {
        if (entity != null) {
            final String localizationCode = getLocalization().getLocalization();
            // @formatter:off
            final List<Code> codes = getDsl()
                .select(CODE.CODE_.as("C_ID")
                        , DSL.coalesce(CODE_TR.NAME, TranslationUtils.NOT_TRANSL).as("C_NAME")
                        , CODE_TR.COMMENT.as("C_COMMENT")
                        , CODE.INTERNAL.as("C_INTERNAL")
                        , CODE_CLASS.ID.as("CC_ID")
                        , DSL.coalesce(CODE_CLASS_TR.NAME, TranslationUtils.NOT_TRANSL).as("CC_NAME")
                        , DSL.coalesce(CODE_CLASS_TR.DESCRIPTION, TranslationUtils.NOT_TRANSL).as("CC_DESCRIPTION")
                        , CODE.SORT)
                .from(PAPER_CODE)
                .join(PAPER).on(PAPER_CODE.PAPER_ID.equal(PAPER.ID))
                .join(CODE).on(PAPER_CODE.CODE.equal(CODE.CODE_))
                .join(CODE_CLASS).on(CODE.CODE_CLASS_ID.equal(CODE_CLASS.ID))
                .leftOuterJoin(CODE_TR).on(CODE.CODE_.equal(CODE_TR.CODE).and(CODE_TR.LANG_CODE.equal(localizationCode)))
                .leftOuterJoin(CODE_CLASS_TR).on(CODE_CLASS.ID.equal(CODE_CLASS_TR.CODE_CLASS_ID).and(CODE_CLASS_TR.LANG_CODE.equal(localizationCode)))
                .where(PAPER_CODE.PAPER_ID.equal(entity.getId()))
                .fetchInto(Code.class);
            // @formatter:on
            if (CollectionUtils.isNotEmpty(codes)) {
                entity.addCodes(codes);
            }
        }
    }

    @Override
    protected void saveAssociatedEntitiesOf(Paper paper) {
        storeNewCodesOf(paper);
    }

    @Override
    protected void updateAssociatedEntities(Paper paper) {
        storeNewCodesOf(paper);
        deleteObsoleteCodesFrom(paper);
    }

    private void storeNewCodesOf(Paper paper) {
        InsertValuesStep2<PaperCodeRecord, Long, String> step = getDsl().insertInto(PAPER_CODE, PAPER_CODE.PAPER_ID, PAPER_CODE.CODE);
        final Long paperId = paper.getId();
        for (final Code c : paper.getCodes()) {
            step = step.values(paperId, c.getCode());
        }
        step.onDuplicateKeyIgnore().execute();
    }

    private void deleteObsoleteCodesFrom(Paper paper) {
        final List<String> codes = paper.getCodes().stream().map(Code::getCode).collect(Collectors.toList());
        getDsl().deleteFrom(PAPER_CODE).where(PAPER_CODE.PAPER_ID.equal(paper.getId()).and(PAPER_CODE.CODE.notIn(codes))).execute();
    }

}
