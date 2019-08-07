package ch.difty.scipamato.core.sync.jobs.keyword;

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

import ch.difty.scipamato.core.db.public_.tables.Keyword;
import ch.difty.scipamato.core.db.public_.tables.KeywordTr;
import ch.difty.scipamato.core.sync.jobs.SyncConfigTest;
import ch.difty.scipamato.publ.db.public_.tables.records.KeywordRecord;

@SpringBootTest
class KeywordSyncConfigTest extends SyncConfigTest<KeywordRecord> {

    @Autowired
    private KeywordSyncConfig config;

    @Override
    protected Job getJob() {
        return config.syncKeywordJob();
    }

    @Override
    protected String selectSql() {
        return config.selectSql();
    }

    @Override
    protected TableField<KeywordRecord, Timestamp> lastSynchedField() {
        return config.lastSynchedField();
    }

    @Override
    protected DeleteConditionStep<KeywordRecord> getPseudoFkDcs() {
        return config.getPseudoFkDcs();
    }

    @Override
    protected String expectedJobName() {
        return "syncKeywordJob";
    }

    @Override
    protected String expectedSelectSql() {
        return
            "select \"public\".\"keyword_tr\".\"id\", \"public\".\"keyword\".\"id\" as \"KeywordId\", \"public\".\"keyword_tr\".\"lang_code\", "
            + "\"public\".\"keyword_tr\".\"name\", \"public\".\"keyword_tr\".\"version\", \"public\".\"keyword_tr\".\"created\", "
            + "\"public\".\"keyword_tr\".\"last_modified\", \"public\".\"keyword\".\"search_override\" "
            + "from \"public\".\"keyword\" join \"public\".\"keyword_tr\" "
            + "on \"public\".\"keyword\".\"id\" = \"public\".\"keyword_tr\".\"keyword_id\"";
    }

    @Override
    protected TableField<KeywordRecord, Timestamp> expectedLastSyncField() {
        return ch.difty.scipamato.publ.db.public_.tables.Keyword.KEYWORD.LAST_SYNCHED;
    }

    @Test
    void makingEntity() throws SQLException {
        ResultSet rs = Mockito.mock(ResultSet.class);
        when(rs.getInt(KeywordTr.KEYWORD_TR.ID.getName())).thenReturn(1);
        when(rs.getInt("KeywordId")).thenReturn(2);
        when(rs.getString(Keyword.KEYWORD.SEARCH_OVERRIDE.getName())).thenReturn("so");
        when(rs.getString(KeywordTr.KEYWORD_TR.LANG_CODE.getName())).thenReturn("lc");
        when(rs.getString(KeywordTr.KEYWORD_TR.NAME.getName())).thenReturn("n");
        when(rs.getInt(KeywordTr.KEYWORD_TR.VERSION.getName())).thenReturn(3);
        when(rs.getTimestamp(KeywordTr.KEYWORD_TR.CREATED.getName())).thenReturn(CREATED);
        when(rs.getTimestamp(KeywordTr.KEYWORD_TR.LAST_MODIFIED.getName())).thenReturn(MODIFIED);

        PublicKeyword pc = config.makeEntity(rs);

        assertThat(pc.getId()).isEqualTo(1);
        assertThat(pc.getKeywordId()).isEqualTo(2);
        assertThat(pc.getLangCode()).isEqualTo("lc");
        assertThat(pc.getName()).isEqualTo("n");
        assertThat(pc.getVersion()).isEqualTo(3);
        assertThat(pc.getCreated()).isEqualTo(CREATED);
        assertThat(pc.getLastModified()).isEqualTo(MODIFIED);
        assertThat(pc.getLastSynched()).isCloseTo("2016-12-09T06:02:13.000", 1000);
        assertThat(pc.getSearchOverride()).isEqualTo("so");

        verify(rs).getInt(KeywordTr.KEYWORD_TR.ID.getName());
        verify(rs).getInt("KeywordId");
        verify(rs).getString(KeywordTr.KEYWORD_TR.LANG_CODE.getName());
        verify(rs).getString(KeywordTr.KEYWORD_TR.NAME.getName());
        verify(rs).getInt(KeywordTr.KEYWORD_TR.VERSION.getName());
        verify(rs).getTimestamp(KeywordTr.KEYWORD_TR.CREATED.getName());
        verify(rs).getTimestamp(KeywordTr.KEYWORD_TR.LAST_MODIFIED.getName());
        verify(rs).getString(Keyword.KEYWORD.SEARCH_OVERRIDE.getName());
        verify(rs, times(2)).wasNull();

        verifyNoMoreInteractions(rs);
    }
}
