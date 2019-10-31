package ch.difty.scipamato.core.sync.jobs.newsletter

import ch.difty.scipamato.core.sync.jobs.CREATED
import ch.difty.scipamato.core.sync.jobs.MODIFIED
import ch.difty.scipamato.core.sync.jobs.SYNCHED
import org.assertj.core.api.Assertions.assertThat
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

        assertThat(pn.id).isEqualTo(1)
        assertThat(pn.issue).isEqualTo("i")
        assertThat(pn.issueDate).isEqualTo(ISSUE_DATE)
        assertThat(pn.version).isEqualTo(3)
        assertThat(pn.created).isEqualTo(CREATED)
        assertThat(pn.lastModified).isEqualTo(MODIFIED)
        assertThat(pn.lastSynched).isEqualTo(SYNCHED)
    }

    companion object {
        private val ISSUE_DATE: Date = Date.valueOf("2018-06-14")
    }
}
