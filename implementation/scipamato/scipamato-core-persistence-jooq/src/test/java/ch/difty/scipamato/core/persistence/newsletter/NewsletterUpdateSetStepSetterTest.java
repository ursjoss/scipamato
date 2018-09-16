package ch.difty.scipamato.core.persistence.newsletter;

import static ch.difty.scipamato.core.db.tables.Newsletter.NEWSLETTER;
import static ch.difty.scipamato.core.persistence.newsletter.NewsletterRecordMapperTest.*;
import static org.mockito.Mockito.*;

import java.sql.Date;

import org.mockito.Mock;

import ch.difty.scipamato.core.db.tables.records.NewsletterRecord;
import ch.difty.scipamato.core.entity.newsletter.Newsletter;
import ch.difty.scipamato.core.persistence.UpdateSetStepSetter;
import ch.difty.scipamato.core.persistence.UpdateSetStepSetterTest;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class NewsletterUpdateSetStepSetterTest extends UpdateSetStepSetterTest<NewsletterRecord, Newsletter> {

    private final UpdateSetStepSetter<NewsletterRecord, Newsletter> setter = new NewsletterUpdateSetStepSetter();

    @Mock
    private Newsletter entityMock;

    @Override
    protected UpdateSetStepSetter<NewsletterRecord, Newsletter> getSetter() {
        return setter;
    }

    @Override
    protected Newsletter getEntity() {
        return entityMock;
    }

    @Override
    protected void specificTearDown() {
        verifyNoMoreInteractions(entityMock);
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
    protected void stepSetFixtureAudit() {
        when(getMoreStep().set(NEWSLETTER.CREATED, CREATED)).thenReturn(getMoreStep());
        when(getMoreStep().set(NEWSLETTER.CREATED_BY, CREATED_BY)).thenReturn(getMoreStep());
        when(getMoreStep().set(NEWSLETTER.LAST_MODIFIED, LAST_MOD)).thenReturn(getMoreStep());
        when(getMoreStep().set(NEWSLETTER.LAST_MODIFIED_BY, LAST_MOD_BY)).thenReturn(getMoreStep());
        when(getMoreStep().set(NEWSLETTER.VERSION, VERSION + 1)).thenReturn(getMoreStep());
    }

    @Override
    protected void verifyCallToAllFieldsExceptAudit() {
        verify(entityMock).getIssue();
        verify(entityMock).getIssueDate();
        verify(entityMock).getPublicationStatus();
    }

    @Override
    protected void verifyStepSettingExceptAudit() {
        verify(getStep()).set(NEWSLETTER.ISSUE, ISSUE);
        verify(getMoreStep()).set(NEWSLETTER.ISSUE_DATE, Date.valueOf(ISSUE_DATE));
        verify(getMoreStep()).set(NEWSLETTER.PUBLICATION_STATUS, PUBLICATION_STATUS.getId());
    }

    @Override
    protected void verifyStepSettingAudit() {
        verify(getMoreStep()).set(NEWSLETTER.CREATED, CREATED);
        verify(getMoreStep()).set(NEWSLETTER.CREATED_BY, CREATED_BY);
        verify(getMoreStep()).set(NEWSLETTER.LAST_MODIFIED, LAST_MOD);
        verify(getMoreStep()).set(NEWSLETTER.LAST_MODIFIED_BY, LAST_MOD_BY);
        verify(getMoreStep()).set(NEWSLETTER.VERSION, VERSION + 1);
    }

}
