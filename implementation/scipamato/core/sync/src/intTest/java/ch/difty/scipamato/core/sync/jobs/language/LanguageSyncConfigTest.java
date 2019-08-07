package ch.difty.scipamato.core.sync.jobs.language;

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

import ch.difty.scipamato.core.db.tables.Language;
import ch.difty.scipamato.core.sync.jobs.SyncConfigTest;
import ch.difty.scipamato.publ.db.tables.records.LanguageRecord;

@SpringBootTest
class LanguageSyncConfigTest extends SyncConfigTest<LanguageRecord> {

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
    protected TableField<LanguageRecord, Timestamp> lastSynchedField() {
        return config.lastSynchedField();
    }

    @Override
    protected DeleteConditionStep<LanguageRecord> getPseudoFkDcs() {
        return config.getPseudoFkDcs();
    }

    @Override
    protected String expectedJobName() {
        return "syncLanguageJob";
    }

    @Override
    protected String expectedSelectSql() {
        return "select \"public\".\"language\".\"code\", \"public\".\"language\".\"main_language\" from \"public\".\"language\"";
    }

    @Override
    protected TableField<LanguageRecord, Timestamp> expectedLastSyncField() {
        return ch.difty.scipamato.publ.db.tables.Language.LANGUAGE.LAST_SYNCHED;
    }

    @Test
    void makingEntity() throws SQLException {
        ResultSet rs = Mockito.mock(ResultSet.class);
        when(rs.getString(Language.LANGUAGE.CODE.getName())).thenReturn("de");
        when(rs.getBoolean(Language.LANGUAGE.MAIN_LANGUAGE.getName())).thenReturn(true);

        PublicLanguage pl = config.makeEntity(rs);

        assertThat(pl.getCode()).isEqualTo("de");
        assertThat(pl.getMainLanguage()).isTrue();
        assertThat(pl.getLastSynched()).isCloseTo("2016-12-09T06:02:13.000", 1000);

        verify(rs).getString(Language.LANGUAGE.CODE.getName());
        verify(rs).getBoolean(Language.LANGUAGE.MAIN_LANGUAGE.getName());

        verifyNoMoreInteractions(rs);
    }
}