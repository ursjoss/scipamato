package ch.difty.scipamato.core.persistence;

import static ch.difty.scipamato.core.db.Tables.PAPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import ch.difty.scipamato.core.db.tables.records.PaperRecord;

/**
 * @author Lukas Eder
 * @author Thomas Darimont
 * @author Urs Joss
 */
@JooqTest
@Testcontainers
class JooqEntityDslIntegrationTest {

    @Autowired
    private DSLContext create;

    @Test
    void testPaperRecords() {
        Result<PaperRecord> result = create
            .selectFrom(PAPER)
            .orderBy(PAPER.ID)
            .fetch();
        assertThat(result).hasSize(TestDbConstants.RECORD_COUNT_PREPOPULATED);
    }

}
