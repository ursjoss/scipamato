package ch.difty.sipamato.persistance.jooq;

import static ch.difty.sipamato.db.Tables.PAPER;
import static org.assertj.core.api.Assertions.assertThat;

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
    }

}
