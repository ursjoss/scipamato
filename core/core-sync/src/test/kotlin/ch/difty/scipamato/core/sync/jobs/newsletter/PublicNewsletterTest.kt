package ch.difty.scipamato.core.sync.jobs.newsletter

import ch.difty.scipamato.core.sync.jobs.CREATED
import ch.difty.scipamato.core.sync.jobs.MODIFIED
import ch.difty.scipamato.core.sync.jobs.SYNCHED
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import java.sql.Date

internal class PublicNewsletterTest {

    @Test
    fun canSetGet() {
        val pn = PublicNewsletter
            .builder()
            .id(1)
            .issue("i")
            .issueDate(ISSUE_DATE)
            .version(3)
            .created(CREATED)
            .lastModified(MODIFIED)
            .lastSynched(SYNCHED)
            .build()

        pn.id shouldBeEqualTo 1
        pn.issue shouldBeEqualTo "i"
        pn.issueDate shouldBeEqualTo ISSUE_DATE
        pn.version shouldBeEqualTo 3
        pn.created shouldBeEqualTo CREATED
        pn.lastModified shouldBeEqualTo MODIFIED
        pn.lastSynched shouldBeEqualTo SYNCHED
    }

    companion object {
        private val ISSUE_DATE: Date = Date.valueOf("2018-06-14")
    }
}
