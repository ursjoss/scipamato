package ch.difty.scipamato.core.sync.jobs.language;

import static ch.difty.scipamato.publ.db.tables.Language.LANGUAGE;
import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterIntegrationTest;
import ch.difty.scipamato.publ.db.tables.records.LanguageRecord;

@SuppressWarnings("SameParameterValue")
class LanguageItemWriterIntegrationTest extends AbstractItemWriterIntegrationTest<PublicLanguage, LanguageItemWriter> {

    private static final String CODE_NEW      = "es";
    private static final String CODE_EXISTING = "de";

    private static final Timestamp LAST_SYNCHED_NEW = Timestamp.valueOf(LocalDateTime.now());

    private PublicLanguage newLanguage, existingLanguage;

    @Override
    protected LanguageItemWriter newWriter() {
        return new LanguageItemWriter(dsl);
    }

    @Override
    public void setUpEntities() {
        newLanguage = newLanguage(CODE_NEW, LAST_SYNCHED_NEW);

        existingLanguage = getExistingLanguageFromDb(CODE_EXISTING);
        assertThat(existingLanguage.getLastSynched()).isBefore(Timestamp.valueOf(LocalDateTime.now()));
        existingLanguage.setLastSynched(Timestamp.valueOf(LocalDateTime.now()));
    }

    private PublicLanguage newLanguage(final String code, final Timestamp lastSynched) {
        return PublicLanguage
            .builder()
            .code(code)
            .mainLanguage(false)
            .lastSynched(lastSynched)
            .build();
    }

    private PublicLanguage getExistingLanguageFromDb(final String code) {
        final LanguageRecord lr = dsl
            .selectFrom(LANGUAGE)
            .where(LANGUAGE.CODE.eq(code))
            .fetchOne();
        return PublicLanguage
            .builder()
            .code(lr.getCode())
            .mainLanguage(lr.getMainLanguage())
            .lastSynched(lr.getLastSynched())
            .build();
    }

    @AfterEach
    void tearDown() {
        dsl
            .deleteFrom(LANGUAGE)
            .where(LANGUAGE.CODE.eq(CODE_NEW))
            .execute();
        dsl
            .update(LANGUAGE)
            .set(LANGUAGE.LAST_SYNCHED, DSL.val(Timestamp.valueOf("2018-09-25 23:47:43.798089")))
            .set(LANGUAGE.MAIN_LANGUAGE, DSL.val(true))
            .where(LANGUAGE.CODE.eq(CODE_EXISTING))
            .execute();
    }

    @Test
    void insertingNewLanguage_succeeds() {
        String newCode = newLanguage.getCode();
        assertCodeDoesNotExistWith(newCode);
        assertThat(getWriter().executeUpdate(newLanguage)).isEqualTo(1);
        assertCodeExistsWith(newCode);
    }

    private void assertCodeExistsWith(String code) {
        assertRecordCountForId(code, 1);
    }

    private void assertCodeDoesNotExistWith(String code) {
        assertRecordCountForId(code, 0);
    }

    private void assertRecordCountForId(String code, int size) {
        assertThat(dsl
            .select(LANGUAGE.CODE)
            .from(LANGUAGE)
            .where(LANGUAGE.CODE.eq(code))
            .fetch()).hasSize(size);
    }

    @Test
    void updatingExistingCode_succeeds() {
        assertThat(getWriter().executeUpdate(existingLanguage)).isEqualTo(1);
    }

}
