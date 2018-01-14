package ch.difty.scipamato.core.persistence.codeclass;

import static ch.difty.scipamato.core.db.tables.CodeClass.CODE_CLASS;
import static ch.difty.scipamato.core.db.tables.CodeClassTr.CODE_CLASS_TR;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import ch.difty.scipamato.common.TranslationUtils;
import ch.difty.scipamato.core.entity.CodeClass;

@Repository
@CacheConfig(cacheNames = "codeClasses")
public class JooqCodeClassRepo implements CodeClassRepository {

    private final DSLContext dslContext;

    public JooqCodeClassRepo(@Qualifier("dslContext") final DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    @Cacheable
    public List<CodeClass> find(final String languageCode) {
        final String lang = TranslationUtils.trimLanguageCode(languageCode);
        // skipping the audit fields
        return dslContext
            .select(CODE_CLASS.ID.as("CC_ID"), DSL.coalesce(CODE_CLASS_TR.NAME, TranslationUtils.NOT_TRANSL)
                .as("CC_NAME"),
                DSL.coalesce(CODE_CLASS_TR.DESCRIPTION, TranslationUtils.NOT_TRANSL)
                    .as("CC_DESCRIPTION"))
            .from(CODE_CLASS)
            .leftOuterJoin(CODE_CLASS_TR)
            .on(CODE_CLASS.ID.equal(CODE_CLASS_TR.CODE_CLASS_ID)
                .and(CODE_CLASS_TR.LANG_CODE.equal(lang)))
            .fetchInto(CodeClass.class);
    }

}
