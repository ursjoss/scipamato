package ch.difty.scipamato.core.persistence.newsletter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.time.LocalDate;

import org.jooq.RecordMapper;
import org.junit.Test;

import ch.difty.scipamato.common.entity.newsletter.PublicationStatus;
import ch.difty.scipamato.core.db.tables.records.NewsletterRecord;
import ch.difty.scipamato.core.entity.newsletter.Newsletter;
import ch.difty.scipamato.core.persistence.RecordMapperTest;

public class NewsletterRecordMapperTest extends RecordMapperTest<NewsletterRecord, Newsletter> {

    public static final int               ID                 = 1;
    public static final String            ISSUE              = "issue";
    public static final String            ISSUE_DATE         = "2018-04-24";
    public static final PublicationStatus PUBLICATION_STATUS = PublicationStatus.WIP;

    public static void entityFixtureWithoutIdFields(Newsletter entityMock) {
        when(entityMock.getIssue()).thenReturn(ISSUE);
        when(entityMock.getIssueDate()).thenReturn(LocalDate.parse(ISSUE_DATE));
        when(entityMock.getPublicationStatus()).thenReturn(PUBLICATION_STATUS);
    }

    @Override
    protected RecordMapper<NewsletterRecord, Newsletter> getMapper() {
        return new NewsletterRecordMapper();
    }

    @Override
    protected NewsletterRecord makeRecord() {
        final NewsletterRecord record = new NewsletterRecord();
        record.setId(ID);
        record.setIssue(ISSUE);
        record.setIssueDate(Date.valueOf(ISSUE_DATE));
        record.setPublicationStatus(PUBLICATION_STATUS.getId());
        return record;
    }

    @Override
    protected void setAuditFieldsIn(final NewsletterRecord r) {
        r.setCreated(CREATED);
        r.setCreatedBy(CREATED_BY);
        r.setLastModified(LAST_MOD);
        r.setLastModifiedBy(LAST_MOD_BY);
        r.setVersion(VERSION);
    }

    @Override
    protected void assertEntity(final Newsletter entity) {
        assertThat(entity.getId()).isEqualTo(ID);
        assertThat(entity.getIssue()).isEqualTo(ISSUE);
        assertThat(entity.getIssueDate()).isEqualTo(LocalDate.parse(ISSUE_DATE));
        assertThat(entity.getPublicationStatus()).isEqualTo(PUBLICATION_STATUS);
        assertThat(entity.getTopics()).isEmpty();
    }

    @Test
    public void mapping_withIssueDateInRecordNull_mapsRecordToEntity_withIssueDateNull() {
        NewsletterRecord record = makeRecord();
        setAuditFieldsIn(record);
        record.setIssueDate(null);
        Newsletter entity = getMapper().map(record);
        assertThat(entity.getIssueDate()).isNull();
    }
}