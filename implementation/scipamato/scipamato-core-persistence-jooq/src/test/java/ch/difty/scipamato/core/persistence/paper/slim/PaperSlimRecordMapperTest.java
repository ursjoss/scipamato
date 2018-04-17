package ch.difty.scipamato.core.persistence.paper.slim;

import static ch.difty.scipamato.core.persistence.paper.PaperRecordMapperTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.jooq.RecordMapper;

import ch.difty.scipamato.core.db.tables.records.PaperRecord;
import ch.difty.scipamato.core.entity.projection.PaperSlim;
import ch.difty.scipamato.core.persistence.RecordMapperTest;

public class PaperSlimRecordMapperTest extends RecordMapperTest<PaperRecord, PaperSlim> {

    public static void entityFixtureWithoutIdFields(PaperSlim entity) {
        when(entity.getNumber()).thenReturn(NUMBER);
        when(entity.getFirstAuthor()).thenReturn(FIRST_AUTHOR);
        when(entity.getTitle()).thenReturn(TITLE);
        when(entity.getPublicationYear()).thenReturn(PUBLICATION_YEAR);

        auditFixtureFor(entity);
    }

    @Override
    protected RecordMapper<PaperRecord, PaperSlim> getMapper() {
        return new PaperSlimRecordMapper();
    }

    @Override
    protected PaperRecord makeRecord() {
        PaperRecord record = new PaperRecord();
        record.setId(ID);
        record.setNumber(NUMBER);
        record.setFirstAuthor(FIRST_AUTHOR);
        record.setTitle(TITLE);
        record.setPublicationYear(PUBLICATION_YEAR);
        return record;
    }

    @Override
    protected void setAuditFieldsIn(PaperRecord record) {
        record.setCreated(CREATED);
        record.setCreatedBy(CREATED_BY);
        record.setLastModified(LAST_MOD);
        record.setLastModifiedBy(LAST_MOD_BY);
        record.setVersion(VERSION);
    }

    @Override
    protected void assertEntity(PaperSlim e) {
        assertThat(e.getId()).isEqualTo(ID.longValue());
        assertThat(e.getNumber()).isEqualTo(NUMBER.longValue());
        assertThat(e.getFirstAuthor()).isEqualTo(FIRST_AUTHOR);
        assertThat(e.getTitle()).isEqualTo(TITLE);
        assertThat(e.getPublicationYear()).isEqualTo(PUBLICATION_YEAR);
    }

}
