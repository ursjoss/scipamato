package ch.difty.scipamato.persistance.jooq.codeclass;

import static ch.difty.scipamato.db.tables.CodeClass.*;
import static ch.difty.scipamato.db.tables.CodeClassTr.*;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import ch.difty.scipamato.entity.CodeClass;
import ch.difty.scipamato.lib.TranslationUtils;

@Repository
@CacheConfig(cacheNames = "codeClasses")
public class JooqCodeClassRepo implements CodeClassRepository {

    private final DSLContext dslContext;

    @Autowired
    public JooqCodeClassRepo(final DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    /** {@inheritDoc} */
    @Override
    @Cacheable
    public List<CodeClass> find(final String languageCode) {
        final String lang = TranslationUtils.trimLanguageCode(languageCode);
        // @formatter:off
        return dslContext
            .select(  CODE_CLASS.ID.as("CC_ID")
                    , DSL.coalesce(CODE_CLASS_TR.NAME, TranslationUtils.NOT_TRANSL).as("CC_NAME")
                    , DSL.coalesce(CODE_CLASS_TR.DESCRIPTION, TranslationUtils.NOT_TRANSL).as("CC_DESCRIPTION"))
            .from(CODE_CLASS)
            .leftOuterJoin(CODE_CLASS_TR).on(CODE_CLASS.ID.equal(CODE_CLASS_TR.CODE_CLASS_ID).and(CODE_CLASS_TR.LANG_CODE.equal(lang)))
            .fetchInto(CodeClass.class);
        // @formatter:on
    }

}
