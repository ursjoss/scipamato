package ch.difty.scipamato.publ.persistence.newstudies

import ch.difty.scipamato.publ.entity.NewStudyPageLink
import ch.difty.scipamato.publ.entity.NewStudyTopic
import ch.difty.scipamato.publ.entity.Newsletter
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class JooqNewStudyTopicServiceTest {

    private val repoMock = mockk<NewStudyRepository>()
    private val newStudyTopicDummy = NewStudyTopic(1, "title")
    private val service: JooqNewStudyTopicService = JooqNewStudyTopicService(repoMock)
    private var studyTopics = listOf(newStudyTopicDummy, newStudyTopicDummy)

    @Test
    fun findingMostRecentNewStudyTopics() {
        every { repoMock.findMostRecentNewsletterId() } returns java.util.Optional.of(NL_ID)
        every { repoMock.findNewStudyTopicsForNewsletter(NL_ID, "en") } returns studyTopics

        service.findMostRecentNewStudyTopics("en") shouldContainAll listOf(newStudyTopicDummy, newStudyTopicDummy)

        verify { repoMock.findMostRecentNewsletterId() }
        verify { repoMock.findNewStudyTopicsForNewsletter(NL_ID, "en") }
    }

    @Test
    fun findNewStudyTopicsForNewsletterIssue() {
        every { repoMock.findIdOfNewsletterWithIssue("2018/06") } returns java.util.Optional.of(NL_ID)
        every { repoMock.findNewStudyTopicsForNewsletter(NL_ID, "en") } returns studyTopics

        service.findNewStudyTopicsForNewsletterIssue("2018/06", "en") shouldContainSame
            listOf(newStudyTopicDummy, newStudyTopicDummy)

        verify { repoMock.findIdOfNewsletterWithIssue("2018/06") }
        verify { repoMock.findNewStudyTopicsForNewsletter(NL_ID, "en") }
    }

    @Test
    fun findingArchivedNewsletters_delegatesToRepo() {
        every { repoMock.findArchivedNewsletters(14, "de") } returns
            listOf(Newsletter(2, "2018/06", LocalDate.of(2018, 6, 10)),
                Newsletter(1, "2018/04", LocalDate.of(2018, 4, 10)))

        service.findArchivedNewsletters(14, "de") shouldHaveSize 2

        verify { repoMock.findArchivedNewsletters(14, "de") }
    }

    @Test
    fun findingNewStudyPageLinks_delegatesToRepo() {
        every { repoMock.findNewStudyPageLinks("de") } returns listOf(
            NewStudyPageLink("en", 1, "title1", "url1"),
            NewStudyPageLink("en", 2, "title2", "url2")
        )

        service.findNewStudyPageLinks("de") shouldHaveSize 2

        verify { repoMock.findNewStudyPageLinks("de") }
    }

    companion object {
        private const val NL_ID = 17
    }
}
