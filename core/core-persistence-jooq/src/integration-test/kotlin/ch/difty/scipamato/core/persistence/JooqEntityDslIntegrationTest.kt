@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.core.persistence

import ch.difty.scipamato.common.persistence.JooqIntegrationTest
import ch.difty.scipamato.core.db.Tables.PAPER
import org.amshove.kluent.shouldHaveSize
import org.jooq.DSLContext
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author Lukas Eder
 * @author Thomas Darimont
 * @author Urs Joss
 */
@JooqIntegrationTest
internal open class JooqEntityDslIntegrationTest {

    @Autowired
    private lateinit var create: DSLContext

    @Test
    fun testPaperRecords() {
        val result = create.selectFrom(PAPER).orderBy(PAPER.ID).fetch()
        result shouldHaveSize RECORD_COUNT_PREPOPULATED
    }
}
