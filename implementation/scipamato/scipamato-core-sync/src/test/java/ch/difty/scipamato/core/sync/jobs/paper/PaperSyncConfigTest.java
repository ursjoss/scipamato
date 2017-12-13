package ch.difty.scipamato.core.sync.jobs.paper;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.function.Supplier;

import org.jooq.DeleteConditionStep;
import org.jooq.SQLDialect;
import org.jooq.tools.jdbc.MockArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import ch.difty.scipamato.core.db.public_.tables.Paper;
import ch.difty.scipamato.core.sync.code.CodeAggregator;
import ch.difty.scipamato.core.sync.jobs.SyncConfigTest;
import ch.difty.scipamato.public_.db.public_.tables.records.PaperRecord;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PaperSyncConfigTest extends SyncConfigTest<PaperRecord> {

    private ResultSet rs = mock(ResultSet.class);

    @MockBean
    private CodeAggregator codeAggregator;

    @Autowired
    private PaperSyncConfig config;

    @Override
    protected Job getJob() {
        return config.syncPaperJob();
    }

    @Override
    protected String selectSql() {
        return config.selectSql();
    }

    @Override
    protected DeleteConditionStep<PaperRecord> purgeDeleteConditionStep() {
        return config.getPurgeDcs(Timestamp.valueOf(LocalDateTime.now()));
    }

    @Override
    protected String expectedJobName() {
        return "syncPaperJob";
    }

    @Override
    protected String expectedSelectSql() {
        return "select \"public\".\"paper\".\"id\", \"public\".\"paper\".\"number\", \"public\".\"paper\".\"pm_id\", \"public\".\"paper\".\"authors\", "
                + "\"public\".\"paper\".\"title\", \"public\".\"paper\".\"location\", \"public\".\"paper\".\"publication_year\", \"public\".\"paper\".\"goals\", "
                + "\"public\".\"paper\".\"methods\", \"public\".\"paper\".\"population\", \"public\".\"paper\".\"result\", \"public\".\"paper\".\"comment\", "
                + "\"public\".\"paper\".\"version\", \"public\".\"paper\".\"created\", \"public\".\"paper\".\"last_modified\", array_agg(\"public\".\"paper_code\".\"code\") as \"codes\" "
                + "from \"public\".\"paper\" join \"public\".\"paper_code\" on \"public\".\"paper\".\"id\" = \"public\".\"paper_code\".\"paper_id\" "
                + "join \"public\".\"code\" on \"public\".\"paper_code\".\"code\" = \"public\".\"code\".\"code\" "
                + "group by \"public\".\"paper\".\"id\", \"public\".\"paper\".\"number\", \"public\".\"paper\".\"pm_id\", \"public\".\"paper\".\"authors\", "
                + "\"public\".\"paper\".\"title\", \"public\".\"paper\".\"location\", \"public\".\"paper\".\"publication_year\", \"public\".\"paper\".\"goals\", "
                + "\"public\".\"paper\".\"methods\", \"public\".\"paper\".\"population\", \"public\".\"paper\".\"result\", \"public\".\"paper\".\"comment\", "
                + "\"public\".\"paper\".\"version\", \"public\".\"paper\".\"created\", \"public\".\"paper\".\"last_modified\"";
    }

    @Override
    protected String expectedPurgeSql() {
        return "delete from \"public\".\"paper\" where \"public\".\"paper\".\"last_synched\" < cast(? as timestamp)";
    }

    @Test
    public void makingEntity() throws SQLException {
        when(rs.getLong(Paper.PAPER.ID.getName())).thenReturn(1l);
        when(rs.getLong(Paper.PAPER.NUMBER.getName())).thenReturn(2l);
        when(rs.getInt(Paper.PAPER.PM_ID.getName())).thenReturn(3);
        when(rs.getString(Paper.PAPER.AUTHORS.getName())).thenReturn("a");
        when(rs.getString(Paper.PAPER.TITLE.getName())).thenReturn("t");
        when(rs.getString(Paper.PAPER.LOCATION.getName())).thenReturn("l");
        when(rs.getInt(Paper.PAPER.PUBLICATION_YEAR.getName())).thenReturn(2017);
        when(rs.getString(Paper.PAPER.GOALS.getName())).thenReturn("g");
        when(rs.getString(Paper.PAPER.METHODS.getName())).thenReturn("m");
        when(rs.getString(Paper.PAPER.POPULATION.getName())).thenReturn("p");
        when(rs.getString(Paper.PAPER.RESULT.getName())).thenReturn("r");
        when(rs.getString(Paper.PAPER.COMMENT.getName())).thenReturn("c");
        when(rs.getArray("codes")).thenReturn(new MockArray<String>(SQLDialect.POSTGRES, new String[] { "1A", "2B" }, String[].class));
        when(rs.getInt(Paper.PAPER.VERSION.getName())).thenReturn(4);
        when(rs.getTimestamp(Paper.PAPER.CREATED.getName())).thenReturn(CREATED);
        when(rs.getTimestamp(Paper.PAPER.LAST_MODIFIED.getName())).thenReturn(MODIFIED);

        when(codeAggregator.getCodesPopulation()).thenReturn(new Short[] { 1, 2 });
        when(codeAggregator.getCodesStudyDesign()).thenReturn(new Short[] { 3, 4 });
        when(codeAggregator.getAggregatedCodes()).thenReturn(new String[] { "1A", "2B" });

        PublicPaper pp = config.makeEntity(rs);

        assertThat(pp.getId()).isEqualTo(1l);
        assertThat(pp.getNumber()).isEqualTo(2l);
        assertThat(pp.getPmId()).isEqualTo(3);
        assertThat(pp.getAuthors()).isEqualTo("a");
        assertThat(pp.getTitle()).isEqualTo("t");
        assertThat(pp.getLocation()).isEqualTo("l");
        assertThat(pp.getPublicationYear()).isEqualTo(2017);
        assertThat(pp.getGoals()).isEqualTo("g");
        assertThat(pp.getMethods()).isEqualTo("m");
        assertThat(pp.getPopulation()).isEqualTo("p");
        assertThat(pp.getResult()).isEqualTo("r");
        assertThat(pp.getComment()).isEqualTo("c");
        assertThat(pp.getCodes()).containsExactly("1A", "2B");
        assertThat(pp.getCodesPopulation()).containsExactly((short) 1, (short) 2);
        assertThat(pp.getCodesStudyDesign()).containsExactly((short) 3, (short) 4);
        assertThat(pp.getVersion()).isEqualTo(4);
        assertThat(pp.getCreated()).isEqualTo(CREATED);
        assertThat(pp.getLastModified()).isEqualTo(MODIFIED);
        assertThat(pp.getLastSynched()).isCloseTo("2016-12-09T06:02:13.000", 1000);

        verify(rs).getLong(Paper.PAPER.ID.getName());
        verify(rs).getLong(Paper.PAPER.NUMBER.getName());
        verify(rs).getInt(Paper.PAPER.PM_ID.getName());
        verify(rs).getString(Paper.PAPER.AUTHORS.getName());
        verify(rs).getString(Paper.PAPER.TITLE.getName());
        verify(rs).getString(Paper.PAPER.LOCATION.getName());
        verify(rs).getInt(Paper.PAPER.PUBLICATION_YEAR.getName());
        verify(rs).getString(Paper.PAPER.GOALS.getName());
        verify(rs).getString(Paper.PAPER.METHODS.getName());
        verify(rs).getString(Paper.PAPER.POPULATION.getName());
        verify(rs).getString(Paper.PAPER.RESULT.getName());
        verify(rs).getString(Paper.PAPER.COMMENT.getName());
        verify(rs).getArray("codes");
        verify(rs).getInt(Paper.PAPER.VERSION.getName());
        verify(rs).getTimestamp(Paper.PAPER.CREATED.getName());
        verify(rs).getTimestamp(Paper.PAPER.LAST_MODIFIED.getName());
        verify(rs, times(5)).wasNull();

        verify(codeAggregator).load(new String[] { "1A", "2B" });
        verify(codeAggregator).getCodesPopulation();
        verify(codeAggregator).getCodesStudyDesign();
        verify(codeAggregator).getAggregatedCodes();
        verifyNoMoreInteractions(rs);
    }

    @Test
    public void makingEntity_withNullValueInPM_ID() throws SQLException {
        final String fieldName = Paper.PAPER.PM_ID.getName();
        validateNullableIntColumn(() -> {
            try {
                return rs.getInt(fieldName);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
        verify(rs).getInt(fieldName);
    }

    @Test
    public void makingEntity_withNullValueInPublicationYear() throws SQLException {
        final String fieldName = Paper.PAPER.PUBLICATION_YEAR.getName();
        validateNullableIntColumn(() -> {
            try {
                return rs.getInt(fieldName);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
        verify(rs).getInt(fieldName);
    }

    @Test
    public void makingEntity_withNullNumber() throws SQLException {
        final String fieldName = Paper.PAPER.NUMBER.getName();
        validateNullableLongColumn(() -> {
            try {
                return rs.getLong(fieldName);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
        verify(rs).getLong(fieldName);
    }

    private void validateNullableIntColumn(Supplier<Integer> suppl) throws SQLException {
        when(suppl.get()).thenReturn(0);
        validateNullableNumberColumn(suppl);
    }

    private void validateNullableLongColumn(Supplier<Long> suppl) throws SQLException {
        when(suppl.get()).thenReturn(0l);
        validateNullableNumberColumn(suppl);
    }

    private void validateNullableNumberColumn(Supplier<? extends Number> suppl) throws SQLException {
        when(rs.wasNull()).thenReturn(true);
        when(rs.getArray("codes")).thenReturn(new MockArray<String>(SQLDialect.POSTGRES, new String[] { "1A", "2B" }, String[].class));

        PublicPaper pp = config.makeEntity(rs);

        assertThat(pp.getNumber()).isNull();
        verify(rs, times(5)).wasNull();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void assertInternalCodesAreSet() {
        verify(codeAggregator).setInternalCodes(anyList());
    }

}
