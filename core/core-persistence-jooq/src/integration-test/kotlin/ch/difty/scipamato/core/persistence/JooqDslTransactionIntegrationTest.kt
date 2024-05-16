package ch.difty.scipamato.core.persistence

import ch.difty.scipamato.core.db.Tables.PAPER
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeTrue
import org.jooq.DSLContext
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.support.DefaultTransactionDefinition
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.concurrent.atomic.AtomicBoolean

@Suppress("MagicNumber")
@JooqTest
@Testcontainers
internal open class JooqDslTransactionIntegrationTest {

    @Autowired
    private lateinit var dsl: DSLContext

    @Autowired
    private lateinit var txMgr: DataSourceTransactionManager

    @Test
    @Disabled
    fun testExplicitTransactions() {
        var rollback = false

        val tx = txMgr.getTransaction(DefaultTransactionDefinition())
        try {
            // This is a "bug". The same book is created twice, resulting in a constraint violation exception
            repeat(2) {
                dsl.insertInto(PAPER)
                    .set(PAPER.ID, MAX_ID_PREPOPULATED + 1)
                    .set(PAPER.NUMBER, 1000L)
                    .set(PAPER.AUTHORS, "authors")
                    .set(PAPER.FIRST_AUTHOR, "firstAuthor")
                    .set(PAPER.FIRST_AUTHOR_OVERRIDDEN, false)
                    .set(PAPER.TITLE, "title")
                    .set(PAPER.LOCATION, "location")
                    .set(PAPER.GOALS, "goals")
                    .execute()
            }
            fail { "should have thrown DataAccessException" }
        } catch (e: DataAccessException) {
            // Upon the constraint violation, we explicitly roll back the transaction.
            txMgr.rollback(tx)
            rollback = true
        }

        dsl.fetchCount(PAPER) shouldBeEqualTo RECORD_COUNT_PREPOPULATED
        rollback.shouldBeTrue()
    }

    @Test
    fun jOOQTransactionsSimple() {
        var rollback = false

        try {
            dsl.transaction { _ ->

                // This is a "bug". The same book is created twice, resulting in a
                // constraint violation exception
                repeat(2) {
                    dsl.insertInto(PAPER)
                        .set(PAPER.ID, MAX_ID_PREPOPULATED + 1)
                        .set(PAPER.AUTHORS, "authors")
                        .set(PAPER.FIRST_AUTHOR, "firstAuthor")
                        .set(PAPER.FIRST_AUTHOR_OVERRIDDEN, false)
                        .set(PAPER.TITLE, "title")
                        .set(PAPER.LOCATION, "location")
                        .set(PAPER.GOALS, "goals")
                        .execute()
                }
                fail { "Should have thrown DataAccessException" }
            }
        } catch (e: DataAccessException) {
            // Upon the constraint violation, the transaction must already have been rolled back
            rollback = true
        }

        dsl.fetchCount(PAPER) shouldBeEqualTo RECORD_COUNT_PREPOPULATED
        rollback.shouldBeTrue()
    }

    @Test
    fun jOOQTransactionsNested() {
        val rollback1 = AtomicBoolean(false)
        val rollback2 = AtomicBoolean(false)

        try {
            // If using Spring transactions, we don't need the c1 reference
            dsl.transaction { _ ->

                // The first insertion will work
                dsl.insertInto(PAPER)
                    .set(PAPER.ID, MAX_ID_PREPOPULATED + 1)
                    .set(PAPER.NUMBER, 1000L)
                    .set(PAPER.AUTHORS, "authors")
                    .set(PAPER.FIRST_AUTHOR, "firstAuthor")
                    .set(PAPER.FIRST_AUTHOR_OVERRIDDEN, false)
                    .set(PAPER.TITLE, "title2")
                    .set(PAPER.LOCATION, "location")
                    .set(PAPER.GOALS, "goals")
                    .execute()

                dsl.fetchCount(PAPER) shouldBeEqualTo RECORD_COUNT_PREPOPULATED + 1

                try {
                    // Nest transactions using Spring. This should create a savepoint, right here
                    // If using Spring transactions, we don't need the c2 reference
                    dsl.transaction { _ ->

                        // The second insertion shouldn't work
                        repeat(2) {
                            dsl.insertInto(PAPER)
                                .set(PAPER.ID, 3L)
                                .set(PAPER.NUMBER, 1001L)
                                .set(PAPER.AUTHORS, "authors")
                                .set(PAPER.FIRST_AUTHOR, "firstAuthor")
                                .set(PAPER.FIRST_AUTHOR_OVERRIDDEN, false)
                                .set(PAPER.TITLE, "title3")
                                .set(PAPER.LOCATION, "location")
                                .set(PAPER.GOALS, "goals")
                                .execute()
                        }
                        fail { "Should have thrown DataAccessException" }
                    }
                } catch (e: DataAccessException) {
                    rollback1.set(true)
                }

                // We should've rolled back to the savepoint
                dsl.fetchCount(PAPER) shouldBeEqualTo RECORD_COUNT_PREPOPULATED + 1

                throw org.jooq.exception.DataAccessException("Rollback")
            }
        } catch (e: org.jooq.exception.DataAccessException) {
            // Upon the constraint violation, the transaction must already have been rolled back
            e.message shouldBeEqualTo "Rollback"
            rollback2.set(true)
        }

        dsl.fetchCount(PAPER) shouldBeEqualTo RECORD_COUNT_PREPOPULATED
        rollback2.get().shouldBeTrue()
        rollback2.get().shouldBeTrue()
    }
}
