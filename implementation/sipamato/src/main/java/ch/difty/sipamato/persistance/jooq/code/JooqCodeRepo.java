package ch.difty.sipamato.persistance.jooq.code;

import static ch.difty.sipamato.db.tables.Code.CODE;
import static ch.difty.sipamato.db.tables.CodeClass.CODE_CLASS;
import static ch.difty.sipamato.db.tables.CodeClassTr.CODE_CLASS_TR;
import static ch.difty.sipamato.db.tables.CodeTr.CODE_TR;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ch.difty.sipamato.entity.Code;
import ch.difty.sipamato.entity.CodeClassId;
import ch.difty.sipamato.lib.AssertAs;
import ch.difty.sipamato.lib.TranslationUtils;

@Repository
public class JooqCodeRepo implements CodeRepository {

    private final DSLContext dslContext;

    @Autowired
    public JooqCodeRepo(final DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    /** {@inheritDoc} */
    @Override
    public List<Code> findCodesOfClass(final CodeClassId codeClassId, final String languageCode) {
        AssertAs.notNull(codeClassId, "codeClassId");
        final String lang = TranslationUtils.trimLanguageCode(languageCode);
        // @formatter:off
        final List<Code> codes = dslContext
            .select(CODE.CODE_.as("C_ID")
                    , DSL.coalesce(CODE_TR.NAME, TranslationUtils.NOT_TRANSL).as("C_NAME")
                    , CODE_TR.COMMENT.as("C_COMMENT")
                    , CODE.INTERNAL.as("C_INTERNAL")
                    , CODE_CLASS.ID.as("CC_ID")
                    , DSL.coalesce(CODE_CLASS_TR.NAME, TranslationUtils.NOT_TRANSL).as("CC_NAME")
                    , DSL.coalesce(CODE_CLASS_TR.DESCRIPTION, TranslationUtils.NOT_TRANSL).as("CC_DESCRIPTION")
                    , CODE.SORT.as("C_SORT"))
            .from(CODE)
            .join(CODE_CLASS).on(CODE.CODE_CLASS_ID.equal(CODE_CLASS.ID))
            .leftOuterJoin(CODE_TR).on(CODE.CODE_.equal(CODE_TR.CODE).and(CODE_TR.LANG_CODE.equal(lang)))
            .leftOuterJoin(CODE_CLASS_TR).on(CODE_CLASS.ID.equal(CODE_CLASS_TR.CODE_CLASS_ID).and(CODE_CLASS_TR.LANG_CODE.equal(lang)))
            .where(CODE.CODE_CLASS_ID.equal(codeClassId.getId()))
            .orderBy(CODE_CLASS.ID, CODE.SORT)
            .fetchInto(Code.class);
        // @formatter:on

        return codes;
    }

}
