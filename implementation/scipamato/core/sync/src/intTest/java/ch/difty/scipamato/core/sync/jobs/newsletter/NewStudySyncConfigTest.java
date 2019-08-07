package ch.difty.scipamato.core.sync.jobs.newsletter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.jooq.DeleteConditionStep;
import org.jooq.TableField;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ch.difty.scipamato.core.db.public_.tables.Paper;
import ch.difty.scipamato.core.db.public_.tables.PaperNewsletter;
import ch.difty.scipamato.core.sync.jobs.SyncConfigTest;
import ch.difty.scipamato.publ.db.public_.tables.NewStudy;
import ch.difty.scipamato.publ.db.public_.tables.records.NewStudyRecord;

@SuppressWarnings("SpellCheckingInspection")
@SpringBootTest
class NewStudySyncConfigTest extends SyncConfigTest<NewStudyRecord> {

    @Autowired
    private NewStudySyncConfig config;

    @Override
    protected Job getJob() {
        return config.syncNewStudyJob();
    }

    @Override
    protected String selectSql() {
        return config.selectSql();
    }

    @Override
    protected TableField<NewStudyRecord, Timestamp> lastSynchedField() {
        return config.lastSynchedField();
    }

    @Override
    protected DeleteConditionStep<NewStudyRecord> getPseudoFkDcs() {
        return config.getPseudoFkDcs();
    }

    @Override
    protected String expectedJobName() {
        return "syncNewStudyJob";
    }

    @Override
    protected String expectedSelectSql() {
        // @formatter:off
        return
            "select \"public\".\"paper_newsletter\".\"newsletter_id\", \"public\".\"paper_newsletter\".\"paper_id\", "
            + "\"public\".\"paper_newsletter\".\"newsletter_topic_id\", \"public\".\"paper\".\"publication_year\", "
            + "\"public\".\"paper\".\"number\", \"public\".\"paper\".\"first_author\", \"public\".\"paper\".\"authors\", "
            + "\"public\".\"paper_newsletter\".\"headline\", \"public\".\"paper\".\"goals\", "
            + "\"public\".\"paper_newsletter\".\"version\", \"public\".\"paper_newsletter\".\"created\", "
            + "\"public\".\"paper_newsletter\".\"last_modified\" "
            + "from \"public\".\"paper_newsletter\" "
            + "join \"public\".\"paper\" on \"public\".\"paper_newsletter\".\"paper_id\" = \"public\".\"paper\".\"id\" "
            + "join \"public\".\"newsletter\" on \"public\".\"paper_newsletter\".\"newsletter_id\" = \"public\".\"newsletter\".\"id\" "
            + "where \"public\".\"newsletter\".\"publication_status\" = 1";
        // @formatter:on
    }

    @Override
    protected TableField<NewStudyRecord, Timestamp> expectedLastSyncField() {
        return NewStudy.NEW_STUDY.LAST_SYNCHED;
    }

    @Override
    public String expectedPseudoFkSql() {
        return "delete from \"public\".\"new_study\" where \"public\".\"new_study\".\"newsletter_topic_id\" not in "
               + "(select distinct \"public\".\"newsletter_topic\".\"id\" from \"public\".\"newsletter_topic\")";
    }

    @Test
    void makingEntityWithMultipleAuthors() throws SQLException {
        makingEntityWithAuthors("Yano E, Nishii S, Yokoyama Y.", "Yano et al.");
    }

    @Test
    void makingEntityWithSingleAuthor() throws SQLException {
        makingEntityWithAuthors("Yano E.", "Yano");
    }

    private void makingEntityWithAuthors(final String authors, final String expectedAuthors) throws SQLException {
        ResultSet rs = Mockito.mock(ResultSet.class);
        when(rs.getInt(PaperNewsletter.PAPER_NEWSLETTER.NEWSLETTER_ID.getName())).thenReturn(1);
        when(rs.getInt(PaperNewsletter.PAPER_NEWSLETTER.NEWSLETTER_TOPIC_ID.getName())).thenReturn(2);
        // when(rs.getInt(PaperNewsletter.PAPER_NEWSLETTER.SORT.getName())).thenReturn(3);
        when(rs.getLong(Paper.PAPER.NUMBER.getName())).thenReturn(4L);
        when(rs.getInt(Paper.PAPER.PUBLICATION_YEAR.getName())).thenReturn(5);
        when(rs.getString(Paper.PAPER.AUTHORS.getName())).thenReturn(authors);
        when(rs.getString(Paper.PAPER.FIRST_AUTHOR.getName())).thenReturn("Yano");
        when(rs.getString(PaperNewsletter.PAPER_NEWSLETTER.HEADLINE.getName())).thenReturn("hl");
        when(rs.getString(Paper.PAPER.GOALS.getName())).thenReturn("goals");
        when(rs.getInt(PaperNewsletter.PAPER_NEWSLETTER.VERSION.getName())).thenReturn(6);
        when(rs.getTimestamp(PaperNewsletter.PAPER_NEWSLETTER.CREATED.getName())).thenReturn(CREATED);
        when(rs.getTimestamp(PaperNewsletter.PAPER_NEWSLETTER.LAST_MODIFIED.getName())).thenReturn(MODIFIED);

        PublicNewStudy pns = config.makeEntity(rs);

        assertThat(pns.getNewsletterId()).isEqualTo(1);
        assertThat(pns.getNewsletterTopicId()).isEqualTo(2);
        assertThat(pns.getSort()).isEqualTo(1); // TODO make dynamic
        assertThat(pns.getPaperNumber()).isEqualTo(4L);
        assertThat(pns.getYear()).isEqualTo(5);
        assertThat(pns.getAuthors()).isEqualTo(expectedAuthors);
        assertThat(pns.getHeadline()).isEqualTo("hl");
        assertThat(pns.getDescription()).isEqualTo("goals");
        assertThat(pns.getVersion()).isEqualTo(6);
        assertThat(pns.getCreated()).isEqualTo(CREATED);
        assertThat(pns.getLastModified()).isEqualTo(MODIFIED);
        assertThat(pns.getLastSynched()).isCloseTo("2016-12-09T06:02:13.000", 1000);

        verify(rs).getInt(PaperNewsletter.PAPER_NEWSLETTER.NEWSLETTER_ID.getName());
        verify(rs).getInt(PaperNewsletter.PAPER_NEWSLETTER.NEWSLETTER_TOPIC_ID.getName());
        //verify(rs).getInt(PaperNewsletter.PAPER_NEWSLETTER.SORT.getName());
        verify(rs).getLong(Paper.PAPER.NUMBER.getName());
        verify(rs).getInt(Paper.PAPER.PUBLICATION_YEAR.getName());
        verify(rs).getString(Paper.PAPER.AUTHORS.getName());
        verify(rs).getString(Paper.PAPER.FIRST_AUTHOR.getName());
        verify(rs).getString(PaperNewsletter.PAPER_NEWSLETTER.HEADLINE.getName());
        verify(rs).getString(Paper.PAPER.GOALS.getName());
        verify(rs).getInt(PaperNewsletter.PAPER_NEWSLETTER.VERSION.getName());
        verify(rs).getTimestamp(PaperNewsletter.PAPER_NEWSLETTER.CREATED.getName());
        verify(rs).getTimestamp(PaperNewsletter.PAPER_NEWSLETTER.LAST_MODIFIED.getName());
        verify(rs, times(5)).wasNull();

        verifyNoMoreInteractions(rs);
    }

}
