package ch.difty.scipamato.core.sync.jobs.code;

import static ch.difty.scipamato.publ.db.tables.Code.CODE;
import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterIntegrationTest;
import ch.difty.scipamato.publ.db.tables.records.CodeRecord;

@SuppressWarnings("SameParameterValue")
class CodeItemWriterIntegrationTest extends AbstractItemWriterIntegrationTest<PublicCode, CodeItemWriter> {

    private static final String CODE_EXISTING       = "1F";
    private static final int    CODE_CLASS_EXISTING = 1;
    private static final String CODE_NEW            = "9X";
    private static final String LANG_CODE           = "en";

    private PublicCode newCode, existingCode;

    @Override
    protected CodeItemWriter newWriter() {
        return new CodeItemWriter(dsl);
    }

    @Override
    public void setUpEntities() {
        newCode = newCode(CODE_NEW);

        existingCode = getExistingCodeFromDb(CODE_EXISTING, LANG_CODE);
        assertThat(existingCode.getCodeClassId()).isEqualTo(CODE_CLASS_EXISTING);
        existingCode.setCodeClassId(2);
    }

    private PublicCode newCode(String code) {
        return PublicCode
            .builder()
            .code(code)
            .langCode(LANG_CODE)
            .codeClassId(8)
            .name(code)
            .sort(100)
            .lastSynched(Timestamp.valueOf(LocalDateTime.now()))
            .build();
    }

    private PublicCode getExistingCodeFromDb(String code, String langCode) {
        final CodeRecord cr = dsl
            .selectFrom(CODE)
            .where(CODE.CODE_.eq(code))
            .and(CODE.LANG_CODE.eq(langCode))
            .fetchOne();
        return PublicCode
            .builder()
            .code(cr.getCode())
            .langCode(cr.getLangCode())
            .codeClassId(cr.getCodeClassId())
            .name(cr.getName())
            .comment(cr.getComment())
            .sort(cr.getSort())
            .version(cr.getVersion())
            .created(cr.getCreated())
            .lastModified(cr.getLastModified())
            .lastSynched(cr.getLastSynched())
            .build();
    }

    @AfterEach
    void tearDown() {
        dsl
            .deleteFrom(CODE)
            .where(CODE.CODE_.eq(CODE_NEW))
            .execute();
        dsl
            .update(CODE)
            .set(CODE.CODE_CLASS_ID, CODE_CLASS_EXISTING)
            .where(CODE.CODE_.eq(CODE_EXISTING))
            .and(CODE.LANG_CODE.eq(LANG_CODE))
            .execute();
    }

    @Test
    void insertingNewCode_succeeds() {
        String code = newCode.getCode();
        assertCodeDoesNotExistWith(code, LANG_CODE);
        assertThat(getWriter().executeUpdate(newCode)).isEqualTo(1);
        assertCodeExistsWith(code, LANG_CODE);
    }

    private void assertCodeExistsWith(String code, String langCode) {
        assertRecordCountForId(code, langCode, 1);
    }

    private void assertCodeDoesNotExistWith(String code, String langCode) {
        assertRecordCountForId(code, langCode, 0);
    }

    private void assertRecordCountForId(String code, String langCode, int size) {
        assertThat(dsl
            .select(CODE.CODE_)
            .from(CODE)
            .where(CODE.CODE_.eq(code))
            .and(CODE.LANG_CODE.eq(langCode))
            .fetch()).hasSize(size);
    }

    @Test
    void updatingExistingCode_succeeds() {
        assertThat(getWriter().executeUpdate(existingCode)).isEqualTo(1);
    }

}
