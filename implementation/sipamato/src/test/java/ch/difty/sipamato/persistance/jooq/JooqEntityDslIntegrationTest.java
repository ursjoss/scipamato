package ch.difty.sipamato.persistance.jooq;

import static ch.difty.sipamato.db.Tables.PAPER;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import ch.difty.sipamato.db.tables.records.PaperRecord;

/**
 * @author Lukas Eder
 * @author Thomas Darimont
 * @author Urs Joss
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles({ "DB_JOOQ" })
public class JooqEntityDslIntegrationTest {

    @Autowired
    private DSLContext create;

    @Test
    public void testPaperRecords() throws Exception {
        Result<PaperRecord> result = create.selectFrom(PAPER).orderBy(PAPER.ID).fetch();

        assertThat(result).hasSize(TestDbConstants.RECORD_COUNT_PREPOPULATED);
        assertEquals(asList(1l, 2l, 3l, 4l, 10l, 11l, 12l, 13l, 14l, 15l, 16l, 17l, 18l, 19l, 20l, 21l, 22l, 23l, 24l, 25l, 26l, 27l, 28l), result.getValues(0));
    }

}
