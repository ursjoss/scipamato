package ch.difty.scipamato.core.persistence.code;

import static ch.difty.scipamato.core.db.tables.Code.CODE;
import static ch.difty.scipamato.core.db.tables.CodeClass.CODE_CLASS;
import static ch.difty.scipamato.core.db.tables.CodeClassTr.CODE_CLASS_TR;
import static ch.difty.scipamato.core.db.tables.CodeTr.CODE_TR;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.TranslationUtils;
import ch.difty.scipamato.common.entity.CodeClassId;
import ch.difty.scipamato.core.entity.Code;

@Repository
@CacheConfig(cacheNames = "codes")
public class JooqCodeRepo implements CodeRepository {

    private final DSLContext dslContext;

    public JooqCodeRepo(@Qualifier("dslContext") final DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    /** {@inheritDoc} */
    @Override
    @Cacheable
    public List<Code> findCodesOfClass(final CodeClassId codeClassId, final String languageCode) {
        AssertAs.notNull(codeClassId, "codeClassId");
        final String lang = TranslationUtils.trimLanguageCode(languageCode);
        return dslContext.select(CODE.CODE_.as("C_ID"), DSL.coalesce(CODE_TR.NAME, TranslationUtils.NOT_TRANSL)
            .as("C_NAME"), CODE_TR.COMMENT.as("C_COMMENT"), CODE.INTERNAL.as("C_INTERNAL"), CODE_CLASS.ID.as("CC_ID"),
            DSL.coalesce(CODE_CLASS_TR.NAME, TranslationUtils.NOT_TRANSL)
                .as("CC_NAME"),
            DSL.coalesce(CODE_CLASS_TR.DESCRIPTION, TranslationUtils.NOT_TRANSL)
                .as("CC_DESCRIPTION"),
            CODE.SORT.as("C_SORT"))
            .from(CODE)
            .join(CODE_CLASS)
            .on(CODE.CODE_CLASS_ID.equal(CODE_CLASS.ID))
            .leftOuterJoin(CODE_TR)
            .on(CODE.CODE_.equal(CODE_TR.CODE)
                .and(CODE_TR.LANG_CODE.equal(lang)))
            .leftOuterJoin(CODE_CLASS_TR)
            .on(CODE_CLASS.ID.equal(CODE_CLASS_TR.CODE_CLASS_ID)
                .and(CODE_CLASS_TR.LANG_CODE.equal(lang)))
            .where(CODE.CODE_CLASS_ID.equal(codeClassId.getId()))
            .orderBy(CODE_CLASS.ID, CODE.SORT)
            .fetchInto(Code.class);
    }

}
