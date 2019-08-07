package ch.difty.scipamato.core.sync.jobs.codeclass;

import static ch.difty.scipamato.publ.db.tables.CodeClass.CODE_CLASS;
import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.sync.jobs.AbstractItemWriterIntegrationTest;
import ch.difty.scipamato.publ.db.tables.records.CodeClassRecord;

@SuppressWarnings("SameParameterValue")
class CodeClassItemWriterIntegrationTest
    extends AbstractItemWriterIntegrationTest<PublicCodeClass, CodeClassItemWriter> {

    private static final int ID_EXISTING = 1;
    private static final int ID_NEW      = -1;

    private static final String LANG_CODE = "en";

    private PublicCodeClass newCodeClass, existingCodeClass;

    @Override
    protected CodeClassItemWriter newWriter() {
        return new CodeClassItemWriter(dsl);
    }

    @Override
    public void setUpEntities() {
        newCodeClass = newCodeClass(ID_NEW);

        existingCodeClass = getExistingCodeFromDb(ID_EXISTING, LANG_CODE);
        assertThat(existingCodeClass.getDescription()).isEmpty();
        existingCodeClass.setDescription("foo");
    }

    private PublicCodeClass newCodeClass(int id) {
        return PublicCodeClass
            .builder()
            .codeClassId(id)
            .langCode(LANG_CODE)
            .name("CC" + id)
            .description("CC" + id)
            .lastSynched(Timestamp.valueOf(LocalDateTime.now()))
            .build();
    }

    private PublicCodeClass getExistingCodeFromDb(int id, String langCode) {
        final CodeClassRecord ccr = dsl
            .selectFrom(CODE_CLASS)
            .where(CODE_CLASS.CODE_CLASS_ID.eq(id))
            .and(CODE_CLASS.LANG_CODE.eq(langCode))
            .fetchOne();
        return PublicCodeClass
            .builder()
            .codeClassId(ccr.getCodeClassId())
            .langCode(ccr.getLangCode())
            .name(ccr.getName())
            .description(ccr.getDescription())
            .version(ccr.getVersion())
            .created(ccr.getCreated())
            .lastModified(ccr.getLastModified())
            .lastSynched(ccr.getLastSynched())
            .build();
    }

    @AfterEach
    void tearDown() {
        dsl
            .deleteFrom(CODE_CLASS)
            .where(CODE_CLASS.CODE_CLASS_ID.eq(ID_NEW))
            .execute();
        dsl
            .update(CODE_CLASS)
            .set(CODE_CLASS.DESCRIPTION, "")
            .where(CODE_CLASS.CODE_CLASS_ID.eq(ID_EXISTING))
            .and(CODE_CLASS.LANG_CODE.eq(LANG_CODE))
            .execute();
    }

    @Test
    void insertingNewCodeClass_succeeds() {
        int id = newCodeClass.getCodeClassId();
        assertCodeClassDoesNotExistWith(id, LANG_CODE);
        assertThat(getWriter().executeUpdate(newCodeClass)).isEqualTo(1);
        assertCodeClassExistsWith(id, LANG_CODE);
    }

    private void assertCodeClassExistsWith(int id, String langCode) {
        assertRecordCountForId(id, langCode, 1);
    }

    private void assertCodeClassDoesNotExistWith(int id, String langCode) {
        assertRecordCountForId(id, langCode, 0);
    }

    private void assertRecordCountForId(int id, String langCode, int size) {
        assertThat(dsl
            .select(CODE_CLASS.CODE_CLASS_ID)
            .from(CODE_CLASS)
            .where(CODE_CLASS.CODE_CLASS_ID.eq(id))
            .and(CODE_CLASS.LANG_CODE.eq(langCode))
            .fetch()).hasSize(size);
    }

    @Test
    void updatingExistingCodeClass_succeeds() {
        assertThat(getWriter().executeUpdate(existingCodeClass)).isEqualTo(1);
    }

}
