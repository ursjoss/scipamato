package ch.difty.scipamato.core.sync.jobs.language;

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

import ch.difty.scipamato.core.db.public_.tables.Language;
import ch.difty.scipamato.core.sync.jobs.SyncConfigTest;
import ch.difty.scipamato.publ.db.public_.tables.records.LanguageRecord;

@SpringBootTest
@RunWith(SpringRunner.class)
public class LanguageSyncConfigTest extends SyncConfigTest<LanguageRecord> {

    @Autowired
    private LanguageSyncConfig config;

    @Override
    protected Job getJob() {
        return config.syncLanguageJob();
    }

    @Override
    protected String selectSql() {
        return config.selectSql();
    }

    @Override
    protected DeleteConditionStep<LanguageRecord> purgeDeleteConditionStep() {
        return config.getPurgeDcs(Timestamp.valueOf(LocalDateTime.now()));
    }

    @Override
    protected String expectedJobName() {
        return "syncLanguageJob";
    }

    @Override
    protected String expectedSelectSql() {
        return "select \"public\".\"language\".\"code\" from \"public\".\"language\"";
    }

    @Override
    protected String expectedPurgeSql() {
        return "delete from \"public\".\"language\" where \"public\".\"language\".\"last_synched\" < cast(? as timestamp)";
    }

    @Test
    public void makingEntity() throws SQLException {
        ResultSet rs = Mockito.mock(ResultSet.class);
        when(rs.getString(Language.LANGUAGE.CODE.getName())).thenReturn("de");

        PublicLanguage pl = config.makeEntity(rs);

        assertThat(pl.getCode()).isEqualTo("de");
        assertThat(pl.getLastSynched()).isCloseTo("2016-12-09T06:02:13.000", 1000);

        verify(rs).getString(Language.LANGUAGE.CODE.getName());

        verifyNoMoreInteractions(rs);
    }
}