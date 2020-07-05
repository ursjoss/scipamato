package ch.difty.scipamato.core.sync.jobs.keyword;

import static ch.difty.scipamato.publ.db.tables.Keyword.KEYWORD;
import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterIntegrationTest;
import ch.difty.scipamato.publ.db.tables.records.KeywordRecord;

@SuppressWarnings("SameParameterValue")
class KeywordItemWriterIntegrationTest extends AbstractItemWriterIntegrationTest<PublicKeyword, KeywordItemWriter> {

    private static final int ID_NEW      = 10;
    private static final int ID_EXISTING = 8;

    private static final String NAME_NEW      = "foo";
    private static final String LANG_CODE     = "en";
    private static final String NAME_EXISTING = "Allergies";

    private PublicKeyword newKeyword, existingKeyword;

    @Override
    protected KeywordItemWriter newWriter() {
        return new KeywordItemWriter(dsl);
    }

    @Override
    public void setUpEntities() {
        newKeyword = newKeyword(ID_NEW, NAME_NEW);

        existingKeyword = getExistingCodeFromDb(ID_EXISTING, LANG_CODE);
        existingKeyword.getSearchOverride().shouldBeNull();
        existingKeyword.setSearchOverride("bar");
    }

    private PublicKeyword newKeyword(final int id, final String name) {
        return PublicKeyword
            .builder()
            .id(id)
            .keywordId(id)
            .langCode(LANG_CODE)
            .name(name)
            .lastSynched(Timestamp.valueOf(LocalDateTime.now()))
            .build();
    }

    private PublicKeyword getExistingCodeFromDb(final int id, String langCode) {
        final KeywordRecord kr = dsl
            .selectFrom(KEYWORD)
            .where(KEYWORD.ID.eq(id))
            .fetchOne();
        return PublicKeyword
            .builder()
            .id(kr.getId())
            .keywordId(kr.getKeywordId())
            .langCode(kr.getLangCode())
            .name(kr.getName())
            .version(kr.getVersion())
            .created(kr.getCreated())
            .lastModified(kr.getLastModified())
            .lastSynched(kr.getLastSynched())
            .searchOverride(kr.getSearchOverride())
            .build();
    }

    @AfterEach
    void tearDown() {
        dsl
            .deleteFrom(KEYWORD)
            .where(KEYWORD.ID.eq(ID_NEW))
            .execute();
        dsl
            .update(KEYWORD)
            .set(KEYWORD.SEARCH_OVERRIDE, DSL.val((String) null))
            .where(KEYWORD.ID.eq(ID_EXISTING))
            .and(KEYWORD.LANG_CODE.eq(LANG_CODE))
            .execute();
    }

    @Test
    void insertingNewKeyword_succeeds() {
        int keywordId = newKeyword.getKeywordId();
        assertCodeDoesNotExistWith(keywordId, LANG_CODE);
        getWriter().executeUpdate(newKeyword) shouldBeEqualTo 1;
        assertCodeExistsWith(keywordId, LANG_CODE);
    }

    private void assertCodeExistsWith(int keywordId, String langCode) {
        assertRecordCountForId(keywordId, langCode, 1);
    }

    private void assertCodeDoesNotExistWith(int keywordId, String langCode) {
        assertRecordCountForId(keywordId, langCode, 0);
    }

    private void assertRecordCountForId(int keywordId, String langCode, int size) {
        assertThat(dsl
            .select(KEYWORD.KEYWORD_ID)
            .from(KEYWORD)
            .where(KEYWORD.KEYWORD_ID.eq(keywordId))
            .and(KEYWORD.LANG_CODE.eq(langCode))
            .fetch()).hasSize(size);
    }

    @Test
    void updatingExistingCode_succeeds() {
        getWriter().executeUpdate(existingKeyword) shouldBeEqualTo 1;
    }

}
