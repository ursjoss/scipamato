package ch.difty.sipamato.persistance.jooq.repo;

import static ch.difty.sipamato.db.h2.Tables.PAPER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import ch.difty.sipamato.SipamatoApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SipamatoApplication.class)
public class JooqPaperRepoTransactionIntegrationTest {

    @Autowired
    DSLContext dsl;
    @Autowired
    DataSourceTransactionManager txMgr;
    @Autowired
    JooqPaperRepo books;

    @After
    public void teardown() {

        // Delete all books that were created in any test
        dsl.delete(PAPER).where(PAPER.ID.gt(4)).execute();
    }

    @Test
    public void testExplicitTransactions() {
        boolean rollback = false;

        TransactionStatus tx = txMgr.getTransaction(new DefaultTransactionDefinition());
        try {

            // This is a "bug". The same book is created twice, resulting in a
            // constraint violation exception
            for (int i = 0; i < 2; i++)
                // @formatter:off
                dsl.insertInto(PAPER)
                    .set(PAPER.ID, i)
                    .set(PAPER.AUTHORS, "authors")
                    .set(PAPER.FIRST_AUTHOR, "firstAuthor")
                    .set(PAPER.FIRST_AUTHOR_OVERRIDDEN, false)
                    .set(PAPER.TITLE, "title")
                    .set(PAPER.LOCATION, "location")
                    .set(PAPER.GOALS, "goals")
                .execute();
                // @formatter:off
            Assert.fail();
        }

        // Upon the constraint violation, we explicitly roll back the transaction.
        catch (DataAccessException e) {
            txMgr.rollback(tx);
            rollback = true;
        }

        assertEquals(1, dsl.fetchCount(PAPER));
        assertTrue(rollback);
    }

    @Test
    public void testDeclarativeTransactions() {
        boolean rollback = false;

        try {
            books.create(5, "authors", "firstAuthor", false, "title", "location", "goals");
            Assert.fail();
        } catch (DataAccessException ignore) {
            rollback = true;
        }

        assertEquals(1, dsl.fetchCount(PAPER));
        assertTrue(rollback);
    }

    @Test
    public void testjOOQTransactionsSimple() {
        boolean rollback = false;

        try {
            dsl.transaction(c -> {

                // This is a "bug". The same book is created twice, resulting in a
                // constraint violation exception
                for (int i = 0; i < 2; i++)
                    // @formatter:off
                    dsl.insertInto(PAPER)
                        .set(PAPER.ID, i)
                        .set(PAPER.AUTHORS, "authors")
                        .set(PAPER.FIRST_AUTHOR, "firstAuthor")
                        .set(PAPER.FIRST_AUTHOR_OVERRIDDEN, false)
                        .set(PAPER.TITLE, "title")
                        .set(PAPER.LOCATION, "location")
                        .set(PAPER.GOALS, "goals")
                    .execute();
                    // @formatter:off

                Assert.fail();
            });
        }

        // Upon the constraint violation, the transaction must already have been rolled back
        catch (DataAccessException e) {
            rollback = true;
        }

        assertEquals(1, dsl.fetchCount(PAPER));
        assertTrue(rollback);
    }

    @Test
    public void testjOOQTransactionsNested() {
        AtomicBoolean rollback1 = new AtomicBoolean(false);
        AtomicBoolean rollback2 = new AtomicBoolean(false);

        try {

            // If using Spring transactions, we don't need the c1 reference
            dsl.transaction(c1 -> {

                // The first insertion will work
                // @formatter:off
                dsl.insertInto(PAPER)
                    .set(PAPER.ID, 2)
                    .set(PAPER.AUTHORS, "authors")
                    .set(PAPER.FIRST_AUTHOR, "firstAuthor")
                    .set(PAPER.FIRST_AUTHOR_OVERRIDDEN, false)
                    .set(PAPER.TITLE, "title2")
                    .set(PAPER.LOCATION, "location")
                    .set(PAPER.GOALS, "goals")
                .execute();
                // @formatter:off

                assertEquals(2, dsl.fetchCount(PAPER));

                try {

                    // Nest transactions using Spring. This should create a savepoint, right here
                    // If using Spring transactions, we don't need the c2 reference
                    dsl.transaction(c2 -> {

                        // The second insertion shouldn't work
                        for (int i = 0; i < 2; i++)
                            // @formatter:off
                            dsl.insertInto(PAPER)
                                .set(PAPER.ID, 3)
                                .set(PAPER.AUTHORS, "authors")
                                .set(PAPER.FIRST_AUTHOR, "firstAuthor")
                                .set(PAPER.FIRST_AUTHOR_OVERRIDDEN, false)
                                .set(PAPER.TITLE, "title3")
                                .set(PAPER.LOCATION, "location")
                                .set(PAPER.GOALS, "goals")
                            .execute();
                            // @formatter:off

                        Assert.fail();
                    });
                }

                catch (DataAccessException e) {
                    rollback1.set(true);
                }

                // We should've rolled back to the savepoint
                assertEquals(2, dsl.fetchCount(PAPER));

                throw new org.jooq.exception.DataAccessException("Rollback");
            });
        }

        // Upon the constraint violation, the transaction must already have been rolled back
        catch (org.jooq.exception.DataAccessException e) {
            assertEquals("Rollback", e.getMessage());
            rollback2.set(true);
        }

        assertEquals(1, dsl.fetchCount(PAPER));
        assertTrue(rollback2.get());
        assertTrue(rollback2.get());
    }

}
