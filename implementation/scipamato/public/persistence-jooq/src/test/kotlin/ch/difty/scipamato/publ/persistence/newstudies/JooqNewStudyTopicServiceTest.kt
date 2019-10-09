package ch.difty.scipamato.publ.persistence.newstudies

import ch.difty.scipamato.common.NullArgumentException
import ch.difty.scipamato.publ.entity.NewStudyPageLink
import ch.difty.scipamato.publ.entity.NewStudyTopic
import ch.difty.scipamato.publ.entity.Newsletter
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import java.time.LocalDate

internal class JooqNewStudyTopicServiceTest {

    private val repoMock = mock<NewStudyRepository>()
    private val newStudyTopicDummy = NewStudyTopic(1, "title")
    private val service: JooqNewStudyTopicService = JooqNewStudyTopicService(repoMock)
    private var studyTopics = listOf(newStudyTopicDummy, newStudyTopicDummy)

    @Test
    fun findingMostRecentNewStudyTopics_withNullLanguage_throws() {
        Assertions.assertThrows(NullArgumentException::class.java) { service.findMostRecentNewStudyTopics(null) }
    }

    @Test
    fun findingMostRecentNewStudyTopics() {
        whenever(repoMock.findMostRecentNewsletterId()).thenReturn(java.util.Optional.of(NL_ID))
        whenever(repoMock.findNewStudyTopicsForNewsletter(NL_ID, "en")).thenReturn(studyTopics)

        assertThat(service.findMostRecentNewStudyTopics("en")).containsExactly(newStudyTopicDummy, newStudyTopicDummy)

        verify(repoMock).findMostRecentNewsletterId()
        verify(repoMock).findNewStudyTopicsForNewsletter(NL_ID, "en")
    }

    @Test
    fun findNewStudyTopicsForNewsletterIssue() {
        whenever(repoMock.findIdOfNewsletterWithIssue("2018/06")).thenReturn(java.util.Optional.of(NL_ID))
        whenever(repoMock.findNewStudyTopicsForNewsletter(NL_ID, "en")).thenReturn(studyTopics)

        assertThat(service.findNewStudyTopicsForNewsletterIssue("2018/06", "en")).containsExactly(newStudyTopicDummy, newStudyTopicDummy)

        verify(repoMock).findIdOfNewsletterWithIssue("2018/06")
        verify(repoMock).findNewStudyTopicsForNewsletter(NL_ID, "en")
    }

    @Test
    fun findingArchivedNewsletters_delegatesToRepo() {
        whenever(repoMock.findArchivedNewsletters(14, "de")).thenReturn(
            listOf(Newsletter(2, "2018/06", LocalDate.of(2018, 6, 10)),
                Newsletter(1, "2018/04", LocalDate.of(2018, 4, 10))))

        assertThat(service.findArchivedNewsletters(14, "de")).hasSize(2)

        verify(repoMock).findArchivedNewsletters(14, "de")
    }

    @Test
    fun findingNewStudyPageLinks_delegatesToRepo() {
        whenever(repoMock.findNewStudyPageLinks("de")).thenReturn(listOf(
            NewStudyPageLink("en", 1, "title1", "url1"),
            NewStudyPageLink("en", 2, "title2", "url2"))
        )

        assertThat(service.findNewStudyPageLinks("de")).hasSize(2)

        verify(repoMock).findNewStudyPageLinks("de")
    }

    companion object {
        private const val NL_ID = 17
    }
}
