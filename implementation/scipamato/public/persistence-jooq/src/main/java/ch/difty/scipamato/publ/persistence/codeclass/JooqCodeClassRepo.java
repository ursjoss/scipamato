package ch.difty.scipamato.publ.persistence.codeclass;

import static ch.difty.scipamato.publ.db.tables.CodeClass.CODE_CLASS;

import java.util.List;

import org.jooq.DSLContext;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.TranslationUtils;
import ch.difty.scipamato.publ.entity.CodeClass;

@Repository
@CacheConfig(cacheNames = "codeClasses")
public class JooqCodeClassRepo implements CodeClassRepository {

    private final DSLContext dslContext;

    public JooqCodeClassRepo(final DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    @Cacheable
    public List<CodeClass> find(final String languageCode) {
        AssertAs.INSTANCE.notNull(languageCode, "languageCode");
        final String lang = TranslationUtils.INSTANCE.trimLanguageCode(languageCode);
        // skipping the audit fields
        return dslContext
            .select(CODE_CLASS.CODE_CLASS_ID, CODE_CLASS.LANG_CODE, CODE_CLASS.NAME, CODE_CLASS.DESCRIPTION)
            .from(CODE_CLASS)
            .where(CODE_CLASS.LANG_CODE.eq(lang))
            .orderBy(CODE_CLASS.CODE_CLASS_ID)
            .fetchInto(CodeClass.class);
    }

}
