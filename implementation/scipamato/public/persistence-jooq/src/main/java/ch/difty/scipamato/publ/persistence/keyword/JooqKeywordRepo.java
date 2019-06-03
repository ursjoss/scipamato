package ch.difty.scipamato.publ.persistence.keyword;

import static ch.difty.scipamato.publ.db.tables.Keyword.KEYWORD;

import java.util.List;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import ch.difty.scipamato.common.TranslationUtils;
import ch.difty.scipamato.publ.entity.Keyword;

@Repository
public class JooqKeywordRepo implements KeywordRepository {

    private final DSLContext dslContext;

    public JooqKeywordRepo(final DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public List<Keyword> findKeywords(final String languageCode) {
        final String lang = TranslationUtils.trimLanguageCode(languageCode);
        // skipping the audit fields
        return dslContext
            .select(KEYWORD.ID, KEYWORD.KEYWORD_ID, KEYWORD.LANG_CODE, KEYWORD.NAME, KEYWORD.SEARCH_OVERRIDE)
            .from(KEYWORD)
            .where(KEYWORD.LANG_CODE.eq(lang))
            .orderBy(KEYWORD.NAME)
            .fetchInto(Keyword.class);
    }

}
