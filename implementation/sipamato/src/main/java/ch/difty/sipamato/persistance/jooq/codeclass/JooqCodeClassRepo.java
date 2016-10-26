package ch.difty.sipamato.persistance.jooq.codeclass;

import static ch.difty.sipamato.db.tables.CodeClass.CODE_CLASS;
import static ch.difty.sipamato.db.tables.CodeClassTr.CODE_CLASS_TR;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ch.difty.sipamato.entity.CodeClass;
import ch.difty.sipamato.lib.TranslationUtils;

@Repository
public class JooqCodeClassRepo implements CodeClassRepository {

    private final DSLContext dslContext;

    @Autowired
    public JooqCodeClassRepo(final DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    /** {@inheritDoc} */
    @Override
    public List<CodeClass> find(final String languageCode) {
        final String lang = TranslationUtils.trimLanguageCode(languageCode);
        // @formatter:off
        final List<CodeClass> codeClasses = dslContext
            .select(  CODE_CLASS.ID.as("CC_ID")
                    , DSL.coalesce(CODE_CLASS_TR.NAME, TranslationUtils.NOT_TRANSL).as("CC_NAME")
                    , DSL.coalesce(CODE_CLASS_TR.DESCRIPTION, TranslationUtils.NOT_TRANSL).as("CC_DESCRIPTION"))
            .from(CODE_CLASS)
            .leftOuterJoin(CODE_CLASS_TR).on(CODE_CLASS.ID.equal(CODE_CLASS_TR.CODE_CLASS_ID).and(CODE_CLASS_TR.LANG_CODE.equal(lang)))
            .fetchInto(CodeClass.class);
        // @formatter:on
        return codeClasses;
    }

}
