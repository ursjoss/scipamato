package ch.difty.scipamato.public_.persistence.code;

import static ch.difty.scipamato.public_.db.tables.Code.CODE;

import java.util.List;

import org.jooq.DSLContext;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.TranslationUtils;
import ch.difty.scipamato.common.entity.CodeClassId;
import ch.difty.scipamato.public_.entity.Code;

@Repository
@CacheConfig(cacheNames = "codes")
public class JooqCodeRepo implements CodeRepository {

    private final DSLContext dslContext;

    public JooqCodeRepo(final DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    /** {@inheritDoc} */
    @Override
    @Cacheable
    public List<Code> findCodesOfClass(final CodeClassId codeClassId, final String languageCode) {
        AssertAs.notNull(codeClassId, "codeClassId");
        final String lang = TranslationUtils.trimLanguageCode(languageCode);
        return dslContext.select(CODE.CODE_CLASS_ID, CODE.CODE_, CODE.LANG_CODE, CODE.NAME, CODE.COMMENT, CODE.SORT)
            .from(CODE)
            .where(CODE.LANG_CODE.eq(lang)
                .and(CODE.CODE_CLASS_ID.equal(codeClassId.getId())))
            .orderBy(CODE.CODE_CLASS_ID, CODE.SORT)
            .fetchInto(Code.class);
    }

}
