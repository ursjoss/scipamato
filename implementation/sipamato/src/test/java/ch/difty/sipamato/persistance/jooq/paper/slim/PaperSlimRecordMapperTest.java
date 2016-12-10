package ch.difty.sipamato.persistance.jooq.paper.slim;

import static ch.difty.sipamato.persistance.jooq.paper.PaperRecordMapperTest.FIRST_AUTHOR;
import static ch.difty.sipamato.persistance.jooq.paper.PaperRecordMapperTest.ID;
import static ch.difty.sipamato.persistance.jooq.paper.PaperRecordMapperTest.PUBLICATION_YEAR;
import static ch.difty.sipamato.persistance.jooq.paper.PaperRecordMapperTest.TITLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.jooq.RecordMapper;

import ch.difty.sipamato.db.tables.records.PaperRecord;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.persistance.jooq.RecordMapperTest;

public class PaperSlimRecordMapperTest extends RecordMapperTest<PaperRecord, PaperSlim> {

    public static void entityFixtureWithoutIdFields(PaperSlim entity) {
        when(entity.getFirstAuthor()).thenReturn(FIRST_AUTHOR);
        when(entity.getTitle()).thenReturn(TITLE);
        when(entity.getPublicationYear()).thenReturn(PUBLICATION_YEAR);
    }

    @Override
    protected RecordMapper<PaperRecord, PaperSlim> getMapper() {
        return new PaperSlimRecordMapper();
    }

    @Override
    protected PaperRecord makeRecord() {
        PaperRecord record = new PaperRecord();
        record.setId(ID);
        record.setFirstAuthor(FIRST_AUTHOR);
        record.setTitle(TITLE);
        record.setPublicationYear(PUBLICATION_YEAR);

        record.setVersion(VERSION);
        record.setCreated(CREATED);
        record.setCreatedBy(CREATED_BY);
        record.setLastModified(LAST_MOD);
        record.setLastModifiedBy(LAST_MOD_BY);

        return record;
    }

    @Override
    protected void assertEntity(PaperSlim e) {
        assertThat(e.getId()).isEqualTo(ID.intValue());
        assertThat(e.getFirstAuthor()).isEqualTo(FIRST_AUTHOR);
        assertThat(e.getTitle()).isEqualTo(TITLE);
        assertThat(e.getPublicationYear()).isEqualTo(PUBLICATION_YEAR);
    }

}
