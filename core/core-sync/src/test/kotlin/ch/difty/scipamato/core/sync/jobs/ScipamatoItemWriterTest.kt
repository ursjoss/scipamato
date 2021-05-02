@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.core.sync.jobs

import ch.difty.scipamato.core.sync.PublicLanguage
import io.mockk.mockk
import org.amshove.kluent.shouldBeEqualTo
import org.jooq.DSLContext
import org.junit.jupiter.api.Test
import java.sql.Timestamp
import java.time.Instant

internal class ScipamatoItemWriterTest {

    private var tracker = 0

    private val dslContext = mockk<DSLContext>()

    private val l1 = PublicLanguage(
        code = "2",
        lastSynched = Timestamp.from(Instant.now()),
        mainLanguage = true,
    )
    private val l2 = PublicLanguage(
        code = "3",
        lastSynched = Timestamp.from(Instant.now()),
        mainLanguage = true,
    )

    private val papers = mutableListOf(l1)

    private var writer = object : ScipamatoItemWriter<PublicLanguage>(dslContext, "topic") {
        override fun executeUpdate(l: PublicLanguage): Int {
            tracker += l.code.toInt()
            return l.code.toInt()
        }
    }

    @Test
    fun writingOnePaper() {
        writer.write(papers)
        tracker shouldBeEqualTo 2
    }

    @Test
    fun writingTwoPapers() {
        papers.add(l2)
        writer.write(papers)
        tracker shouldBeEqualTo 5
    }
}
