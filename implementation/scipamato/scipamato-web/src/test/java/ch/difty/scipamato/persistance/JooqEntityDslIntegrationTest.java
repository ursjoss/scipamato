package ch.difty.scipamato.persistance;

import static ch.difty.scipamato.db.Tables.*;
import static org.assertj.core.api.Assertions.*;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.difty.scipamato.db.tables.records.PaperRecord;
import ch.difty.scipamato.persistence.TestDbConstants;

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
