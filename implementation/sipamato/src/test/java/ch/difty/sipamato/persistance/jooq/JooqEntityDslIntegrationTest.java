package ch.difty.sipamato.persistance.jooq;

import static ch.difty.sipamato.db.h2.Tables.PAPER;
import static java.util.Arrays.asList;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import ch.difty.sipamato.db.h2.tables.records.PaperRecord;

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
        assertEquals(asList(1l, 2l, 3l, 4l), result.getValues(0));
    }

}
