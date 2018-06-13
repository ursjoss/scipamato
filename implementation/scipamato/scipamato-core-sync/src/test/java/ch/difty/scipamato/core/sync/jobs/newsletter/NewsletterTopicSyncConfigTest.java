package ch.difty.scipamato.core.sync.jobs.newsletter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.jooq.DeleteConditionStep;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ch.difty.scipamato.core.db.public_.tables.NewsletterTopic;
import ch.difty.scipamato.core.db.public_.tables.NewsletterTopicTr;
import ch.difty.scipamato.core.sync.jobs.SyncConfigTest;
import ch.difty.scipamato.publ.db.public_.tables.records.NewsletterTopicRecord;

@SpringBootTest
@RunWith(SpringRunner.class)
public class NewsletterTopicSyncConfigTest extends SyncConfigTest<NewsletterTopicRecord> {

    @Autowired
    private NewsletterTopicSyncConfig config;

    @Override
    protected Job getJob() {
        return config.syncNewsletterTopicJob();
    }

    @Override
    protected String selectSql() {
        return config.selectSql();
    }

    @Override
    protected DeleteConditionStep<NewsletterTopicRecord> purgeDeleteConditionStep() {
        return config.getPurgeDcs(Timestamp.valueOf(LocalDateTime.now()));
    }

    @Override
    protected String expectedJobName() {
        return "syncNewsletterTopicJob";
    }

    @Override
    protected String expectedSelectSql() {
        return
            "select \"public\".\"newsletter_topic\".\"id\", \"public\".\"newsletter_topic_tr\".\"lang_code\", \"public\".\"newsletter_topic_tr\".\"title\", "
            + "\"public\".\"newsletter_topic_tr\".\"version\", \"public\".\"newsletter_topic_tr\".\"created\", \"public\".\"newsletter_topic_tr\".\"last_modified\" "
            + "from \"public\".\"newsletter_topic\" join \"public\".\"newsletter_topic_tr\" on \"public\".\"newsletter_topic\".\"id\" = \"public\".\"newsletter_topic_tr\".\"newsletter_topic_id\"";
    }

    @Override
    protected String expectedPurgeSql() {
        return "delete from \"public\".\"newsletter_topic\" where \"public\".\"newsletter_topic\".\"last_synched\" < cast(? as timestamp)";
    }

    @Test
    public void makingEntity() throws SQLException {
        ResultSet rs = Mockito.mock(ResultSet.class);
        when(rs.getInt(NewsletterTopic.NEWSLETTER_TOPIC.ID.getName())).thenReturn(1);
        when(rs.getString(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.LANG_CODE.getName())).thenReturn("lc");
        when(rs.getString(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.TITLE.getName())).thenReturn("t");
        when(rs.getInt(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.VERSION.getName())).thenReturn(2);
        when(rs.getTimestamp(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.CREATED.getName())).thenReturn(CREATED);
        when(rs.getTimestamp(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.LAST_MODIFIED.getName())).thenReturn(MODIFIED);

        PublicNewsletterTopic pnt = config.makeEntity(rs);

        assertThat(pnt.getId()).isEqualTo(1);
        assertThat(pnt.getLangCode()).isEqualTo("lc");
        assertThat(pnt.getTitle()).isEqualTo("t");
        assertThat(pnt.getVersion()).isEqualTo(2);
        assertThat(pnt.getCreated()).isEqualTo(CREATED);
        assertThat(pnt.getLastModified()).isEqualTo(MODIFIED);
        assertThat(pnt.getLastSynched()).isCloseTo("2016-12-09T06:02:13.000", 1000);

        verify(rs).getInt(NewsletterTopic.NEWSLETTER_TOPIC.ID.getName());
        verify(rs).getString(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.LANG_CODE.getName());
        verify(rs).getString(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.TITLE.getName());
        verify(rs).getInt(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.VERSION.getName());
        verify(rs).getTimestamp(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.CREATED.getName());
        verify(rs).getTimestamp(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.LAST_MODIFIED.getName());
        verify(rs, times(2)).wasNull();

        verifyNoMoreInteractions(rs);
    }

}