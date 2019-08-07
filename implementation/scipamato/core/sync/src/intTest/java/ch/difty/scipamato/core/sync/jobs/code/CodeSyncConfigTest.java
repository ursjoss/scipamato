package ch.difty.scipamato.core.sync.jobs.code;

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

import ch.difty.scipamato.core.db.public_.tables.Code;
import ch.difty.scipamato.core.db.public_.tables.CodeTr;
import ch.difty.scipamato.core.sync.jobs.SyncConfigTest;
import ch.difty.scipamato.publ.db.public_.tables.records.CodeRecord;

@SpringBootTest
class CodeSyncConfigTest extends SyncConfigTest<CodeRecord> {

    @Autowired
    private CodeSyncConfig config;

    @Override
    protected Job getJob() {
        return config.syncCodeJob();
    }

    @Override
    protected String selectSql() {
        return config.selectSql();
    }

    @Override
    protected TableField<CodeRecord, Timestamp> lastSynchedField() {
        return config.lastSynchedField();
    }

    @Override
    protected DeleteConditionStep<CodeRecord> getPseudoFkDcs() {
        return config.getPseudoFkDcs();
    }

    @Override
    protected String expectedJobName() {
        return "syncCodeJob";
    }

    @Override
    protected String expectedSelectSql() {
        // @formatter:off
        return "(select \"public\".\"code\".\"code\", \"public\".\"code_tr\".\"lang_code\", \"public\".\"code\".\"code_class_id\", \"public\".\"code_tr\".\"name\", \"public\".\"code_tr\".\"comment\", "
                    + "\"public\".\"code\".\"sort\", \"public\".\"code_tr\".\"version\", \"public\".\"code_tr\".\"created\", \"public\".\"code_tr\".\"last_modified\" "
                + "from \"public\".\"code\" "
                + "join \"public\".\"code_tr\" on \"public\".\"code\".\"code\" = \"public\".\"code_tr\".\"code\" "
                + "where \"public\".\"code\".\"internal\" = false) "
                + "union all ("
                    + "select \"v\".\"c1\", \"v\".\"c2\", \"v\".\"c3\", \"v\".\"c4\", \"v\".\"c5\", \"v\".\"c6\", \"v\".\"c7\", \"v\".\"c8\", \"v\".\"c9\" "
                    + "from (values "
                        + "('5abc', 'de', 5, 'Experimentelle Studie', 'aggregated codes', 1, 1, timestamp '2016-12-09 06:02:13.0', timestamp '2016-12-09 06:02:13.0'), "
                        + "('5abc', 'en', 5, 'Experimental study', 'aggregated codes', 1, 1, timestamp '2016-12-09 06:02:13.0', timestamp '2016-12-09 06:02:13.0'), "
                        + "('5abc', 'fr', 5, 'Etude expérimentale', 'aggregated codes', 1, 1, timestamp '2016-12-09 06:02:13.0', timestamp '2016-12-09 06:02:13.0')"
                    + ") as \"v\"(\"c1\", \"c2\", \"c3\", \"c4\", \"c5\", \"c6\", \"c7\", \"c8\", \"c9\")"
                + ")";
        // @formatter:on
    }

    @Override
    protected TableField<CodeRecord, Timestamp> expectedLastSyncField() {
        return ch.difty.scipamato.publ.db.public_.tables.Code.CODE.LAST_SYNCHED;
    }

    @Override
    public String expectedPseudoFkSql() {
        return "delete from \"public\".\"code\" where \"public\".\"code\".\"code_class_id\" not in "
               + "(select distinct \"public\".\"code_class\".\"code_class_id\" from \"public\".\"code_class\")";
    }

    @Test
    void makingEntity() throws SQLException {
        ResultSet rs = Mockito.mock(ResultSet.class);
        when(rs.getString(Code.CODE.CODE_.getName())).thenReturn("c");
        when(rs.getString(CodeTr.CODE_TR.LANG_CODE.getName())).thenReturn("lc");
        when(rs.getInt(Code.CODE.CODE_CLASS_ID.getName())).thenReturn(1);
        when(rs.getString(CodeTr.CODE_TR.NAME.getName())).thenReturn("n");
        when(rs.getString(CodeTr.CODE_TR.COMMENT.getName())).thenReturn("comm");
        when(rs.getInt(Code.CODE.SORT.getName())).thenReturn(2);
        when(rs.getInt(CodeTr.CODE_TR.VERSION.getName())).thenReturn(3);
        when(rs.getTimestamp(CodeTr.CODE_TR.CREATED.getName())).thenReturn(CREATED);
        when(rs.getTimestamp(CodeTr.CODE_TR.LAST_MODIFIED.getName())).thenReturn(MODIFIED);

        PublicCode pc = config.makeEntity(rs);

        assertThat(pc.getCode()).isEqualTo("c");
        assertThat(pc.getLangCode()).isEqualTo("lc");
        assertThat(pc.getCodeClassId()).isEqualTo(1);
        assertThat(pc.getName()).isEqualTo("n");
        assertThat(pc.getComment()).isEqualTo("comm");
        assertThat(pc.getSort()).isEqualTo(2);
        assertThat(pc.getVersion()).isEqualTo(3);
        assertThat(pc.getCreated()).isEqualTo(CREATED);
        assertThat(pc.getLastModified()).isEqualTo(MODIFIED);
        assertThat(pc.getLastSynched()).isCloseTo("2016-12-09T06:02:13.000", 1000);

        verify(rs).getString(Code.CODE.CODE_.getName());
        verify(rs).getString(CodeTr.CODE_TR.LANG_CODE.getName());
        verify(rs).getInt(Code.CODE.CODE_CLASS_ID.getName());
        verify(rs).getString(CodeTr.CODE_TR.NAME.getName());
        verify(rs).getString(CodeTr.CODE_TR.COMMENT.getName());
        verify(rs).getInt(Code.CODE.SORT.getName());
        verify(rs).getInt(CodeTr.CODE_TR.VERSION.getName());
        verify(rs).getTimestamp(CodeTr.CODE_TR.CREATED.getName());
        verify(rs).getTimestamp(CodeTr.CODE_TR.LAST_MODIFIED.getName());
        verify(rs, times(3)).wasNull();

        verifyNoMoreInteractions(rs);
    }
}
