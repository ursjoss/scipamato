package ch.difty.scipamato.core.persistence.newsletter;

import static ch.difty.scipamato.core.db.tables.Newsletter.NEWSLETTER;
import static ch.difty.scipamato.core.persistence.newsletter.NewsletterRecordMapperTest.*;
import static org.mockito.Mockito.*;

import java.sql.Date;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import ch.difty.scipamato.core.db.tables.records.NewsletterRecord;
import ch.difty.scipamato.core.entity.newsletter.Newsletter;
import ch.difty.scipamato.core.persistence.InsertSetStepSetter;
import ch.difty.scipamato.core.persistence.InsertSetStepSetterTest;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class NewsletterInsertSetStepSetterTest extends InsertSetStepSetterTest<NewsletterRecord, Newsletter> {

    private final InsertSetStepSetter<NewsletterRecord, Newsletter> setter = new NewsletterInsertSetStepSetter();

    @Mock
    private Newsletter entityMock;

    @Mock
    private NewsletterRecord recordMock;

    @Override
    protected InsertSetStepSetter<NewsletterRecord, Newsletter> getSetter() {
        return setter;
    }

    @Override
    protected Newsletter getEntity() {
        return entityMock;
    }

    @Override
    protected void specificTearDown() {
        verifyNoMoreInteractions(entityMock, recordMock);
    }

    @Override
    protected void entityFixture() {
        NewsletterRecordMapperTest.entityFixtureWithoutIdFields(entityMock);
    }

    @Override
    protected void stepSetFixtureExceptAudit() {
        when(getStep().set(NEWSLETTER.ISSUE, ISSUE)).thenReturn(getMoreStep());
        when(getMoreStep().set(NEWSLETTER.ISSUE_DATE, Date.valueOf(ISSUE_DATE))).thenReturn(getMoreStep());

        when(getMoreStep().set(NEWSLETTER.PUBLICATION_STATUS, PUBLICATION_STATUS.getId())).thenReturn(getMoreStep());
    }

    @Override
    protected void setStepFixtureAudit() {
        when(getMoreStep().set(NEWSLETTER.CREATED_BY, NewsletterRecordMapperTest.CREATED_BY)).thenReturn(getMoreStep());
        when(getMoreStep().set(NEWSLETTER.LAST_MODIFIED_BY, NewsletterRecordMapperTest.LAST_MOD_BY)).thenReturn(
            getMoreStep());
    }

    @Override
    protected void verifyCallToFieldsExceptKeyAndAudit() {
        verify(entityMock).getIssue();
        verify(entityMock).getIssueDate();
        verify(entityMock).getPublicationStatus();
    }

    @Override
    protected void verifySettingFieldsExceptKeyAndAudit() {
        verify(getStep()).set(NEWSLETTER.ISSUE, ISSUE);
        verify(getMoreStep()).set(NEWSLETTER.ISSUE_DATE, Date.valueOf(ISSUE_DATE));
        verify(getMoreStep()).set(NEWSLETTER.PUBLICATION_STATUS, PUBLICATION_STATUS.getId());
    }

    @Override
    protected void verifySettingAuditFields() {
        verify(getMoreStep()).set(NEWSLETTER.CREATED_BY, NewsletterRecordMapperTest.CREATED_BY);
        verify(getMoreStep()).set(NEWSLETTER.LAST_MODIFIED_BY, NewsletterRecordMapperTest.LAST_MOD_BY);
    }

    @Test
    public void consideringSettingKeyOf_withNullId_doesNotSetId() {
        when(getEntity().getId()).thenReturn(null);
        getSetter().considerSettingKeyOf(getMoreStep(), getEntity());
        verify(getEntity()).getId();
    }

    @Test
    public void consideringSettingKeyOf_withNonNullId_doesSetId() {
        when(getEntity().getId()).thenReturn(ID);

        getSetter().considerSettingKeyOf(getMoreStep(), getEntity());

        verify(getEntity()).getId();
        verify(getMoreStep()).set(NEWSLETTER.ID, ID);
    }

    @Test
    public void resettingIdToEntity_withNullRecord_doesNothing() {
        getSetter().resetIdToEntity(entityMock, null);
        verify(entityMock, never()).setId(anyInt());
    }

    @Test
    public void resettingIdToEntity_withNonNullRecord_setsId() {
        when(recordMock.getId()).thenReturn(3);
        getSetter().resetIdToEntity(entityMock, recordMock);
        verify(recordMock).getId();
        verify(entityMock).setId(anyInt());
    }

}