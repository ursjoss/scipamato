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

import ch.difty.scipamato.core.db.public_.tables.NewsletterTopicTr;
import ch.difty.scipamato.core.db.public_.tables.PaperNewsletter;
import ch.difty.scipamato.core.sync.jobs.SyncConfigTest;
import ch.difty.scipamato.publ.db.public_.tables.records.NewStudyTopicRecord;

@SpringBootTest
@RunWith(SpringRunner.class)
public class NewStudyTopicSyncConfigTest extends SyncConfigTest<NewStudyTopicRecord> {

    @Autowired
    private NewStudyTopicSyncConfig config;

    @Override
    protected Job getJob() {
        return config.syncNewStudyTopicJob();
    }

    @Override
    protected String selectSql() {
        return config.selectSql();
    }

    @Override
    protected DeleteConditionStep<NewStudyTopicRecord> purgeDeleteConditionStep() {
        return config.getPurgeDcs(Timestamp.valueOf(LocalDateTime.now()));
    }

    @Override
    protected String expectedJobName() {
        return "syncNewStudyTopicJob";
    }

    @Override
    protected String expectedSelectSql() {
        // @formatter:off
        return
            "select \"public\".\"paper_newsletter\".\"newsletter_id\", \"public\".\"newsletter_topic_tr\".\"newsletter_topic_id\", "
            + "\"public\".\"newsletter_topic_tr\".\"version\", \"public\".\"newsletter_topic_tr\".\"created\", "
            + "\"public\".\"newsletter_topic_tr\".\"last_modified\" "
            + "from \"public\".\"paper_newsletter\" join \"public\".\"newsletter_topic\" "
            + "on \"public\".\"paper_newsletter\".\"newsletter_topic_id\" = \"public\".\"newsletter_topic\".\"id\" "
            + "join \"public\".\"newsletter_topic_tr\" on \"public\".\"newsletter_topic\".\"id\" = "
            + "\"public\".\"newsletter_topic_tr\".\"newsletter_topic_id\"";
        // @formatter:on
    }

    @Override
    protected String expectedPurgeSql() {
        return "delete from \"public\".\"new_study_topic\" where \"public\".\"new_study_topic\".\"last_synched\" < cast(? as timestamp)";
    }

    @Test
    public void makingEntity() throws SQLException {
        ResultSet rs = Mockito.mock(ResultSet.class);
        when(rs.getInt(PaperNewsletter.PAPER_NEWSLETTER.NEWSLETTER_ID.getName())).thenReturn(1);
        when(rs.getInt(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.NEWSLETTER_TOPIC_ID.getName())).thenReturn(2);
        // when(rs.getInt(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.SORT.getName())).thenReturn(3);
        when(rs.getInt(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.VERSION.getName())).thenReturn(4);
        when(rs.getTimestamp(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.CREATED.getName())).thenReturn(CREATED);
        when(rs.getTimestamp(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.LAST_MODIFIED.getName())).thenReturn(MODIFIED);

        PublicNewStudyTopic pns = config.makeEntity(rs);

        assertThat(pns.getNewsletterId()).isEqualTo(1);
        assertThat(pns.getNewsletterTopicId()).isEqualTo(2);
        assertThat(pns.getSort()).isEqualTo(1); // TODO make dynamic
        assertThat(pns.getVersion()).isEqualTo(4);
        assertThat(pns.getCreated()).isEqualTo(CREATED);
        assertThat(pns.getLastModified()).isEqualTo(MODIFIED);
        assertThat(pns.getLastSynched()).isCloseTo("2016-12-09T06:02:13.000", 1000);

        verify(rs).getInt(PaperNewsletter.PAPER_NEWSLETTER.NEWSLETTER_ID.getName());
        verify(rs).getInt(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.NEWSLETTER_TOPIC_ID.getName());
        //verify(rs).getInt(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.SORT.getName());
        verify(rs).getInt(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.VERSION.getName());
        verify(rs).getTimestamp(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.CREATED.getName());
        verify(rs).getTimestamp(NewsletterTopicTr.NEWSLETTER_TOPIC_TR.LAST_MODIFIED.getName());
        verify(rs, times(3)).wasNull();

        verifyNoMoreInteractions(rs);
    }

}
