package ch.difty.scipamato.core.sync.jobs.code;

import static org.assertj.core.api.Assertions.*;
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

import ch.difty.scipamato.core.sync.jobs.SyncConfigTest;
import ch.difty.scipamato.db.core.public_.tables.Code;
import ch.difty.scipamato.db.core.public_.tables.CodeTr;
import ch.difty.scipamato.db.public_.public_.tables.records.CodeRecord;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CodeSyncConfigTest extends SyncConfigTest<CodeRecord> {

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
    protected DeleteConditionStep<CodeRecord> purgeDeleteConditionStep() {
        return config.getPurgeDcs(Timestamp.valueOf(LocalDateTime.now()));
    }

    @Override
    protected String expectedJobName() {
        return "syncCodeJob";
    }

    @Override
    protected String expectedSelectSql() {
        return "select \"public\".\"code\".\"code\", \"public\".\"code_tr\".\"lang_code\", \"public\".\"code\".\"code_class_id\", "
                + "\"public\".\"code_tr\".\"name\", \"public\".\"code_tr\".\"comment\", \"public\".\"code\".\"sort\", \"public\".\"code_tr\".\"version\", "
                + "\"public\".\"code_tr\".\"created\", \"public\".\"code_tr\".\"last_modified\" from \"public\".\"code\" "
                + "join \"public\".\"code_tr\" on \"public\".\"code\".\"code\" = \"public\".\"code_tr\".\"code\" where \"public\".\"code\".\"internal\" = false";
    }

    @Override
    protected String expectedPurgeSql() {
        return "delete from \"public\".\"code\" where \"public\".\"code\".\"last_synched\" < cast(? as timestamp)";
    }

    @Test
    public void makingEntity() throws SQLException {
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
        assertThat(pc.getLastSynched()).isAfterOrEqualsTo(Timestamp.valueOf(LocalDateTime.now().minusSeconds(3)));

        verify(rs).getString(Code.CODE.CODE_.getName());
        verify(rs).getString(CodeTr.CODE_TR.LANG_CODE.getName());
        verify(rs).getInt(Code.CODE.CODE_CLASS_ID.getName());
        verify(rs).getString(CodeTr.CODE_TR.NAME.getName());
        verify(rs).getString(CodeTr.CODE_TR.COMMENT.getName());
        verify(rs).getInt(Code.CODE.SORT.getName());
        verify(rs).getInt(CodeTr.CODE_TR.VERSION.getName());
        verify(rs).getTimestamp(CodeTr.CODE_TR.CREATED.getName());
        verify(rs).getTimestamp(CodeTr.CODE_TR.LAST_MODIFIED.getName());

        verifyNoMoreInteractions(rs);
    }
}
