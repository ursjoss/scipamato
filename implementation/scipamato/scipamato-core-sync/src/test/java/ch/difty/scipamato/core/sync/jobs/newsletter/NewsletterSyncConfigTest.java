package ch.difty.scipamato.core.sync.jobs.newsletter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.jooq.DeleteConditionStep;
import org.jooq.TableField;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ch.difty.scipamato.core.db.public_.tables.Newsletter;
import ch.difty.scipamato.core.sync.jobs.SyncConfigTest;
import ch.difty.scipamato.publ.db.public_.tables.records.NewsletterRecord;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class NewsletterSyncConfigTest extends SyncConfigTest<NewsletterRecord> {

    @Autowired
    private NewsletterSyncConfig config;

    @Override
    protected Job getJob() {
        return config.syncNewsletterJob();
    }

    @Override
    protected String selectSql() {
        return config.selectSql();
    }

    @Override
    protected TableField<NewsletterRecord, Timestamp> lastSynchedField() {
        return config.lastSynchedField();
    }

    @Override
    protected DeleteConditionStep<NewsletterRecord> getPseudoFkDcs() {
        return config.getPseudoFkDcs();
    }

    @Override
    protected String expectedJobName() {
        return "syncNewsletterJob";
    }

    @Override
    protected String expectedSelectSql() {
        return
            "select \"public\".\"newsletter\".\"id\", \"public\".\"newsletter\".\"issue\", \"public\".\"newsletter\".\"issue_date\", "
            + "\"public\".\"newsletter\".\"version\", \"public\".\"newsletter\".\"created\", \"public\".\"newsletter\".\"last_modified\" "
            + "from \"public\".\"newsletter\" where \"public\".\"newsletter\".\"publication_status\" = 1";
    }

    @Override
    protected TableField<NewsletterRecord, Timestamp> expectedLastSyncField() {
        return ch.difty.scipamato.publ.db.public_.tables.Newsletter.NEWSLETTER.LAST_SYNCHED;
    }

    @Test
    public void makingEntity() throws SQLException {
        ResultSet rs = Mockito.mock(ResultSet.class);
        when(rs.getInt(Newsletter.NEWSLETTER.ID.getName())).thenReturn(1);
        when(rs.getString(Newsletter.NEWSLETTER.ISSUE.getName())).thenReturn("issue");
        when(rs.getDate(Newsletter.NEWSLETTER.ISSUE_DATE.getName())).thenReturn(Date.valueOf("2018-06-14"));
        when(rs.getInt(Newsletter.NEWSLETTER.VERSION.getName())).thenReturn(4);
        when(rs.getTimestamp(Newsletter.NEWSLETTER.CREATED.getName())).thenReturn(CREATED);
        when(rs.getTimestamp(Newsletter.NEWSLETTER.LAST_MODIFIED.getName())).thenReturn(MODIFIED);

        PublicNewsletter pn = config.makeEntity(rs);

        assertThat(pn.getId()).isEqualTo(1L);
        assertThat(pn.getIssue()).isEqualTo("issue");
        assertThat(pn
            .getIssueDate()
            .toString()).isEqualTo("2018-06-14");
        assertThat(pn.getVersion()).isEqualTo(4);
        assertThat(pn.getCreated()).isEqualTo(CREATED);
        assertThat(pn.getLastModified()).isEqualTo(MODIFIED);
        assertThat(pn.getLastSynched()).isCloseTo("2016-12-09T06:02:13.000", 1000);

        verify(rs).getInt(Newsletter.NEWSLETTER.ID.getName());
        verify(rs).getString(Newsletter.NEWSLETTER.ISSUE.getName());
        verify(rs).getDate(Newsletter.NEWSLETTER.ISSUE_DATE.getName());
        verify(rs).getInt(Newsletter.NEWSLETTER.VERSION.getName());
        verify(rs).getTimestamp(Newsletter.NEWSLETTER.CREATED.getName());
        verify(rs).getTimestamp(Newsletter.NEWSLETTER.LAST_MODIFIED.getName());
        verify(rs, times(2)).wasNull();

        verifyNoMoreInteractions(rs);
    }

}
