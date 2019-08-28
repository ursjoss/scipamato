package ch.difty.scipamato.core.sync.jobs.newstudypagelink;

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

import ch.difty.scipamato.core.db.tables.NewStudyPageLink;
import ch.difty.scipamato.core.sync.jobs.SyncConfigTest;
import ch.difty.scipamato.publ.db.tables.records.NewStudyPageLinkRecord;

@SpringBootTest
class NewStudyPageLinkSyncConfigTest extends SyncConfigTest<NewStudyPageLinkRecord> {

    @Autowired
    private NewStudyPageLinkSyncConfig config;

    @Override
    protected Job getJob() {
        return config.syncNewStudyPageLinkJob();
    }

    @Override
    protected String selectSql() {
        return config.selectSql();
    }

    @Override
    protected TableField<NewStudyPageLinkRecord, Timestamp> lastSynchedField() {
        return config.lastSynchedField();
    }

    @Override
    protected DeleteConditionStep<NewStudyPageLinkRecord> getPseudoFkDcs() {
        return config.getPseudoFkDcs();
    }

    @Override
    protected String expectedJobName() {
        return "syncNewStudyPageLinkJob";
    }

    @Override
    protected String expectedSelectSql() {
        return "select \"public\".\"new_study_page_link\".\"lang_code\", \"public\".\"new_study_page_link\".\"sort\", \"public\".\"new_study_page_link\".\"title\", \"public\".\"new_study_page_link\".\"url\" from \"public\".\"new_study_page_link\"";
    }

    @Override
    protected TableField<NewStudyPageLinkRecord, Timestamp> expectedLastSyncField() {
        return ch.difty.scipamato.publ.db.tables.NewStudyPageLink.NEW_STUDY_PAGE_LINK.LAST_SYNCHED;
    }

    @Test
    void makingEntity() throws SQLException {
        ResultSet rs = Mockito.mock(ResultSet.class);
        when(rs.getString(NewStudyPageLink.NEW_STUDY_PAGE_LINK.LANG_CODE.getName())).thenReturn("de");
        when(rs.getInt(NewStudyPageLink.NEW_STUDY_PAGE_LINK.SORT.getName())).thenReturn(1);
        when(rs.getString(NewStudyPageLink.NEW_STUDY_PAGE_LINK.TITLE.getName())).thenReturn("title");
        when(rs.getString(NewStudyPageLink.NEW_STUDY_PAGE_LINK.URL.getName())).thenReturn("url");

        PublicNewStudyPageLink pl = config.makeEntity(rs);

        assertThat(pl.getLangCode()).isEqualTo("de");
        assertThat(pl.getSort()).isEqualTo(1);
        assertThat(pl.getTitle()).isEqualTo("title");
        assertThat(pl.getUrl()).isEqualTo("url");
        assertThat(pl.getLastSynched()).isCloseTo("2016-12-09T06:02:13.000", 1000);

        verify(rs).getString(NewStudyPageLink.NEW_STUDY_PAGE_LINK.LANG_CODE.getName());
        verify(rs).getInt(NewStudyPageLink.NEW_STUDY_PAGE_LINK.SORT.getName());
        verify(rs).wasNull();
        verify(rs).getString(NewStudyPageLink.NEW_STUDY_PAGE_LINK.TITLE.getName());
        verify(rs).getString(NewStudyPageLink.NEW_STUDY_PAGE_LINK.URL.getName());

        verifyNoMoreInteractions(rs);
    }
}