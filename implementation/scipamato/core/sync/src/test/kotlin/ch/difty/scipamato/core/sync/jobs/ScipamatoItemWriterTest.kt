package ch.difty.scipamato.core.sync.jobs

import ch.difty.scipamato.core.sync.jobs.paper.PublicPaper
import com.nhaarman.mockitokotlin2.mock
import org.assertj.core.api.Assertions.assertThat
import org.jooq.DSLContext
import org.junit.jupiter.api.Test

internal class ScipamatoItemWriterTest {

    private var tracker = 0

    private val dslContext = mock<DSLContext>()


    private val p1 = PublicPaper.builder().pmId(1).build()
    private val p2 = PublicPaper.builder().pmId(10).build()

    private val papers = mutableListOf<PublicPaper>(p1)

    private var writer = object : ScipamatoItemWriter<PublicPaper>(dslContext, "topic") {
        override fun executeUpdate(pp: PublicPaper): Int {
            tracker += pp.pmId
            return pp.pmId
        }
    }


    @Test
    fun writingOnePaper() {
        writer.write(papers)
        assertThat(tracker).isEqualTo(1)
    }

    @Test
    fun writingTwoPapers() {
        papers.add(p2)
        writer.write(papers)
        assertThat(tracker).isEqualTo(11)
    }
}