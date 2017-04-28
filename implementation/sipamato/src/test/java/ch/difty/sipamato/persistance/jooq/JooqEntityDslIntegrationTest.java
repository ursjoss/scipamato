package ch.difty.sipamato.persistance.jooq;

import static ch.difty.sipamato.db.Tables.PAPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.difty.sipamato.db.tables.records.PaperRecord;

/**
 * @author Lukas Eder
 * @author Thomas Darimont
 * @author Urs Joss
 */
public class JooqEntityDslIntegrationTest extends JooqTransactionalIntegrationTest {

    @Autowired
    private DSLContext create;

    @Test
    public void testPaperRecords() {
        Result<PaperRecord> result = create.selectFrom(PAPER).orderBy(PAPER.ID).fetch();
        assertThat(result).hasSize(TestDbConstants.RECORD_COUNT_PREPOPULATED);
    }

}
