package ch.difty.scipamato.public_.persistence.codeclass;

import static ch.difty.scipamato.public_.db.tables.CodeClass.*;

import java.util.List;

import org.jooq.DSLContext;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import ch.difty.scipamato.common.TranslationUtils;
import ch.difty.scipamato.public_.entity.CodeClass;

@Repository
@CacheConfig(cacheNames = "codeClasses")
public class JooqCodeClassRepo implements CodeClassRepository {

    private final DSLContext dslContext;

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
            .select(  CODE_CLASS.CODE_CLASS_ID
                    , CODE_CLASS.LANG_CODE
                    , CODE_CLASS.NAME
                    , CODE_CLASS.DESCRIPTION
                    , CODE_CLASS.CREATED
                    , CODE_CLASS.LAST_MODIFIED
                    , CODE_CLASS.VERSION)
            .from(CODE_CLASS)
            .where(CODE_CLASS.LANG_CODE.eq(lang))
            .fetchInto(CodeClass.class);
        // @formatter:on
    }

}
