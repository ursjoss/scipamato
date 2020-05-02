package ch.difty.scipamato.core.sync.jobs.codeclass;

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

import ch.difty.scipamato.core.db.tables.CodeClass;
import ch.difty.scipamato.core.db.tables.CodeClassTr;
import ch.difty.scipamato.core.sync.jobs.SyncConfigTest;
import ch.difty.scipamato.publ.db.tables.records.CodeClassRecord;

@SpringBootTest
class CodeClassSyncConfigTest extends SyncConfigTest<CodeClassRecord> {

    @Autowired
    private CodeClassSyncConfig config;

    @Override
    protected Job getJob() {
        return config.syncCodeClassJob();
    }

    @Override
    protected String selectSql() {
        return config.selectSql();
    }

    @Override
    protected TableField<CodeClassRecord, Timestamp> lastSynchedField() {
        return config.lastSynchedField();
    }

    @Override
    protected DeleteConditionStep<CodeClassRecord> getPseudoFkDcs() {
        return config.getPseudoFkDcs();
    }

    @Override
    protected String expectedJobName() {
        return "syncCodeClassJob";
    }

    @Override
    protected String expectedSelectSql() {
        return
            "select \"public\".\"code_class\".\"id\", \"public\".\"code_class_tr\".\"lang_code\", \"public\".\"code_class_tr\".\"name\", \"public\".\"code_class_tr\".\"description\", "
            + "\"public\".\"code_class_tr\".\"version\", \"public\".\"code_class_tr\".\"created\", \"public\".\"code_class_tr\".\"last_modified\" "
            + "from \"public\".\"code_class\" join \"public\".\"code_class_tr\" on \"public\".\"code_class\".\"id\" = \"public\".\"code_class_tr\".\"code_class_id\"";
    }

    @Override
    protected TableField<CodeClassRecord, Timestamp> expectedLastSyncField() {
        return ch.difty.scipamato.publ.db.tables.CodeClass.CODE_CLASS.LAST_SYNCHED;
    }

    @Test
    void makingEntity() throws SQLException {
        ResultSet rs = Mockito.mock(ResultSet.class);
        when(rs.getInt(CodeClass.CODE_CLASS.ID.getName())).thenReturn(1);
        when(rs.getString(CodeClassTr.CODE_CLASS_TR.LANG_CODE.getName())).thenReturn("lc");
        when(rs.getString(CodeClassTr.CODE_CLASS_TR.NAME.getName())).thenReturn("n");
        when(rs.getString(CodeClassTr.CODE_CLASS_TR.DESCRIPTION.getName())).thenReturn("d");
        when(rs.getInt(CodeClassTr.CODE_CLASS_TR.VERSION.getName())).thenReturn(2);
        when(rs.getTimestamp(CodeClassTr.CODE_CLASS_TR.CREATED.getName())).thenReturn(CREATED);
        when(rs.getTimestamp(CodeClassTr.CODE_CLASS_TR.LAST_MODIFIED.getName())).thenReturn(MODIFIED);

        PublicCodeClass pcc = config.makeEntity(rs);

        pcc.getCodeClassId() shouldBeEqualTo 1;
        pcc.getLangCode() shouldBeEqualTo "lc";
        pcc.getName() shouldBeEqualTo "n";
        pcc.getDescription() shouldBeEqualTo "d";
        pcc.getVersion() shouldBeEqualTo 2;
        pcc.getCreated() shouldBeEqualTo CREATED;
        pcc.getLastModified() shouldBeEqualTo MODIFIED;
        assertThat(pcc.getLastSynched()).isCloseTo("2016-12-09T06:02:13.000", 1000);

        verify{ rs.getInt(CodeClass.CODE_CLASS.ID.getName()); }
        verify{ rs.getString(CodeClassTr.CODE_CLASS_TR.LANG_CODE.getName()); }
        verify{ rs.getString(CodeClassTr.CODE_CLASS_TR.NAME.getName()); }
        verify{ rs.getString(CodeClassTr.CODE_CLASS_TR.DESCRIPTION.getName()); }
        verify{ rs.getInt(CodeClassTr.CODE_CLASS_TR.VERSION.getName()); }
        verify{ rs.getTimestamp(CodeClassTr.CODE_CLASS_TR.CREATED.getName()); }
        verify{ rs.getTimestamp(CodeClassTr.CODE_CLASS_TR.LAST_MODIFIED.getName()); }
        verify(exactly=2) { rs.wasNull(); }

        confirmVerified(rs);
    }
}