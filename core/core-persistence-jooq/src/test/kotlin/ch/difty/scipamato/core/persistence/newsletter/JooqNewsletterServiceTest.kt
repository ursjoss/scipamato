package ch.difty.scipamato.core.persistence.newsletter

import ch.difty.scipamato.common.entity.newsletter.PublicationStatus
import ch.difty.scipamato.common.persistence.paging.PaginationContext
import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.entity.newsletter.Newsletter
import ch.difty.scipamato.core.entity.newsletter.NewsletterFilter
import ch.difty.scipamato.core.persistence.AbstractServiceTest
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.assertj.core.api.AssertionsForClassTypes.fail
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt

@Suppress("UsePropertyAccessSyntax")
internal class JooqNewsletterServiceTest : AbstractServiceTest<Int, Newsletter, NewsletterRepository>() {

    private val repoMock = mock<NewsletterRepository>()
    private val filterMock = mock<NewsletterFilter>()
    private val paginationContextMock = mock<PaginationContext>()
    private val newsletterMock = mock<Newsletter>()
    private val newsletterWipMock = mock<Newsletter>()

    private val service = JooqNewsletterService(repoMock, userRepoMock)

    private val newsletters = listOf(newsletterMock, newsletterMock)

    override val repo = repoMock

    override val entity = newsletterMock

    public override fun specificTearDown() {
        verifyNoMoreInteractions(repoMock, filterMock, paginationContextMock, newsletterMock)
    }

    @Test
    fun findingById_withFoundEntity_returnsOptionalOfIt() {
        val id = 7
        whenever(repoMock.findById(id)).thenReturn(newsletterMock)
        auditFixture()

        val optNl = service.findById(id)
        assertThat(optNl.isPresent).isTrue()
        assertThat(optNl.get()).isEqualTo(newsletterMock)

        verify(repoMock).findById(id)

        verifyAudit(1)
    }

    @Test
    fun findingById_withNotFoundEntity_returnsOptionalEmpty() {
        val id = 7
        whenever(repoMock.findById(id)).thenReturn(null)
        assertThat(service.findById(id).isPresent).isFalse()
        verify(repoMock).findById(id)
    }

    @Test
    fun findingByFilter_delegatesToRepo() {
        whenever(repoMock.findPageByFilter(filterMock, paginationContextMock)).thenReturn(newsletters)
        auditFixture()
        assertThat(service.findPageByFilter(filterMock, paginationContextMock)).isEqualTo(newsletters)
        verify(repoMock).findPageByFilter(filterMock, paginationContextMock)
        verifyAudit(2)
    }

    @Test
    fun countingByFilter_delegatesToRepo() {
        whenever(repoMock.countByFilter(filterMock)).thenReturn(3)
        assertThat(service.countByFilter(filterMock)).isEqualTo(3)
        verify(repoMock).countByFilter(filterMock)
    }

    @Test
    fun savingOrUpdating_withUnsavedEntityAndOtherNewsletterInStatusWIP_throws() {
        whenever(newsletterMock.id).thenReturn(null)
        whenever(newsletterMock.publicationStatus).thenReturn(PublicationStatus.WIP)
        whenever(repoMock.newsletterInStatusWorkInProgress).thenReturn(java.util.Optional.of(newsletterWipMock))
        whenever(newsletterWipMock.id).thenReturn(1)

        try {
            service.saveOrUpdate(newsletterMock)
            fail("should have thrown exception")
        } catch (ex: Exception) {
            assertThat(ex)
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("newsletter.onlyOneInStatusWipAllowed")
        }

        verify(newsletterMock).publicationStatus
        verify(newsletterMock).id
        verify(repoMock).newsletterInStatusWorkInProgress
        verify(newsletterWipMock).id
    }

    @Test
    fun savingOrUpdating_withSavedEntity_butOtherNewsletterInWipStatus_throws() {
        whenever(newsletterMock.id).thenReturn(2)
        whenever(newsletterMock.publicationStatus).thenReturn(PublicationStatus.WIP)
        whenever(repoMock.newsletterInStatusWorkInProgress).thenReturn(java.util.Optional.of(newsletterWipMock))
        whenever(newsletterWipMock.id).thenReturn(1)

        try {
            service.saveOrUpdate(newsletterMock)
            fail("should have thrown exception")
        } catch (ex: Exception) {
            assertThat(ex)
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("newsletter.onlyOneInStatusWipAllowed")
        }

        verify(newsletterMock).publicationStatus
        verify(newsletterMock, times(2)).id
        verify(repoMock).newsletterInStatusWorkInProgress
        verify(newsletterWipMock).id
    }

    @Test
    fun savingOrUpdating_withSavedEntity_butOtherNewsletterInWipStatus() {
        val newsletterId = 1

        whenever(newsletterMock.id).thenReturn(newsletterId)
        whenever(newsletterMock.publicationStatus).thenReturn(PublicationStatus.WIP)
        whenever(repoMock.newsletterInStatusWorkInProgress).thenReturn(java.util.Optional.of(newsletterWipMock))
        whenever(newsletterWipMock.id).thenReturn(newsletterId)

        service.saveOrUpdate(newsletterMock)

        verify(newsletterMock).publicationStatus
        verify(newsletterMock, times(3)).id
        verify(repoMock).newsletterInStatusWorkInProgress
        verify(newsletterWipMock).id
        verify(repoMock).update(newsletterMock)
    }

    @Test
    fun savingOrUpdating_withPaperWithNullId_hasRepoAddThePaper() {
        whenever(newsletterMock.id).thenReturn(null)
        whenever(newsletterMock.publicationStatus).thenReturn(PublicationStatus.PUBLISHED)
        whenever(repoMock.add(newsletterMock)).thenReturn(newsletterMock)
        auditFixture()
        assertThat(service.saveOrUpdate(newsletterMock)).isEqualTo(newsletterMock)
        verify(repoMock).add(newsletterMock)
        verify(newsletterMock).id
        verify(newsletterMock).publicationStatus
        verifyAudit(1)
    }

    @Test
    fun savingOrUpdating_withPaperWithNonNullId_hasRepoUpdateThePaper() {
        whenever(newsletterMock.id).thenReturn(17)
        whenever(newsletterMock.publicationStatus).thenReturn(PublicationStatus.PUBLISHED)
        whenever(repoMock.update(newsletterMock)).thenReturn(newsletterMock)
        auditFixture()
        assertThat(service.saveOrUpdate(newsletterMock)).isEqualTo(newsletterMock)
        verify(repoMock).update(newsletterMock)
        verify(newsletterMock).id
        verify(newsletterMock).publicationStatus
        verifyAudit(1)
    }

    @Test
    fun savingOrUpdating_witNoWipOption_justSaves() {
        // hypothetical case
        whenever(newsletterMock.id).thenReturn(17)
        whenever(newsletterMock.publicationStatus).thenReturn(PublicationStatus.WIP)
        whenever(repoMock.newsletterInStatusWorkInProgress).thenReturn(java.util.Optional.empty())
        whenever(repoMock.update(newsletterMock)).thenReturn(newsletterMock)
        auditFixture()

        assertThat(service.saveOrUpdate(newsletterMock)).isEqualTo(newsletterMock)

        verify(repoMock).newsletterInStatusWorkInProgress
        verify(repoMock).update(newsletterMock)
        verify(newsletterMock).id
        verify(newsletterMock).publicationStatus
        verifyAudit(1)
    }

    @Test
    fun deleting_withNullEntity_doesNothing() {
        service.remove(null)
        verify(repoMock, never()).delete(anyInt(), anyInt())
    }

    @Test
    fun deleting_withEntityWithNullId_doesNothing() {
        whenever(newsletterMock.id).thenReturn(null)

        service.remove(newsletterMock)

        verify(newsletterMock).id
        verify(repoMock, never()).delete(anyInt(), anyInt())
    }

    @Test
    fun deleting_withEntityWithNormalId_delegatesToRepo() {
        whenever(newsletterMock.id).thenReturn(3)
        whenever(newsletterMock.version).thenReturn(17)

        service.remove(newsletterMock)

        verify(newsletterMock, times(2)).id
        verify(newsletterMock, times(1)).version
        verify(repoMock, times(1)).delete(3, 17)
    }

    @Test
    fun canCreateNewNewsletter_withNoWipNewsletters_isAllowed() {
        whenever(repoMock.newsletterInStatusWorkInProgress).thenReturn(java.util.Optional.empty())
        assertThat(service.canCreateNewsletterInProgress()).isTrue()
        verify(repoMock).newsletterInStatusWorkInProgress
    }

    @Test
    fun canCreateNewNewsletter_withOneWipNewsletters_isNotAllowed() {
        whenever(repoMock.newsletterInStatusWorkInProgress).thenReturn(java.util.Optional.of(Newsletter()))
        assertThat(service.canCreateNewsletterInProgress()).isFalse()
        verify(repoMock).newsletterInStatusWorkInProgress
    }

    @Test
    fun mergingPaperIntoNewsletter_withNoWipNewsletterPresent() {
        val paperId: Long = 5
        val newsletterTopicId = 10
        val newsletterId = 1
        val langCode = "en"

        whenever(repoMock.newsletterInStatusWorkInProgress).thenReturn(java.util.Optional.empty())

        service.mergePaperIntoWipNewsletter(paperId, newsletterTopicId)

        verify(repoMock).newsletterInStatusWorkInProgress
        verify(repoMock, never()).mergePaperIntoNewsletter(newsletterId, paperId, newsletterTopicId, langCode)
    }

    @Test
    fun mergingPaperIntoNewsletter_withWipNewsletterPresent_canMerge() {
        val paperId: Long = 5
        val newsletterTopicId = 10
        val newsletterId = 1
        val langCode = "en"

        val wip = Newsletter()
        wip.id = newsletterId

        val nl = Paper.NewsletterLink(1, "link", 2, 3, "topic", "headline")
        val nlo = java.util.Optional.of(nl)

        whenever(repoMock.newsletterInStatusWorkInProgress).thenReturn(java.util.Optional.of(wip))
        whenever(repoMock.mergePaperIntoNewsletter(newsletterId, paperId, newsletterTopicId, langCode)).thenReturn(nlo)

        service.mergePaperIntoWipNewsletter(paperId, newsletterTopicId)

        verify(repoMock).newsletterInStatusWorkInProgress
        verify(repoMock).mergePaperIntoNewsletter(newsletterId, paperId, newsletterTopicId, langCode)
    }

    @Test
    fun mergingPaperIntoNewsletter_withNoWipNewsletterPresent_cannotMerge() {
        val paperId: Long = 5
        val newsletterTopicId = 10

        whenever(repoMock.newsletterInStatusWorkInProgress).thenReturn(java.util.Optional.empty())
        service.mergePaperIntoWipNewsletter(paperId, newsletterTopicId)
        verify(repoMock).newsletterInStatusWorkInProgress
    }

    @Test
    fun mergingPaperIntoNewsletter2_withNoWipNewsletterPresent_cannotMerge() {
        val paperId: Long = 5
        whenever(repoMock.newsletterInStatusWorkInProgress).thenReturn(java.util.Optional.empty())
        service.mergePaperIntoWipNewsletter(paperId)
        verify(repoMock).newsletterInStatusWorkInProgress
    }

    @Test
    fun removingPaperFromNewsletter_withWipNewsletterPresentAndRemoveSucceeding_canRemove() {
        val paperId: Long = 5
        val newsletterId = 1
        val wip = Newsletter()
        wip.id = newsletterId

        whenever(repoMock.newsletterInStatusWorkInProgress).thenReturn(java.util.Optional.of(wip))
        whenever(repoMock.removePaperFromNewsletter(newsletterId, paperId)).thenReturn(1)

        assertThat(service.removePaperFromWipNewsletter(paperId)).isTrue()

        verify(repoMock).newsletterInStatusWorkInProgress
        verify(repoMock).removePaperFromNewsletter(newsletterId, paperId)
    }

    @Test
    fun removingPaperFromNewsletter_withWipNewsletterPresentAndRemoveNotSucceeding_cannotRemove() {
        val paperId: Long = 5
        val newsletterId = 1
        val wip = Newsletter()
        wip.id = newsletterId

        whenever(repoMock.newsletterInStatusWorkInProgress).thenReturn(java.util.Optional.of(wip))
        whenever(repoMock.removePaperFromNewsletter(newsletterId, paperId)).thenReturn(0)

        assertThat(service.removePaperFromWipNewsletter(paperId)).isFalse()

        verify(repoMock).newsletterInStatusWorkInProgress
        verify(repoMock).removePaperFromNewsletter(newsletterId, paperId)
    }

    @Test
    fun removingPaperFromNewsletter_withNoWipNewsletterPresent_cannotRemove() {
        val paperId: Long = 5
        whenever(repoMock.newsletterInStatusWorkInProgress).thenReturn(java.util.Optional.empty())
        assertThat(service.removePaperFromWipNewsletter(paperId)).isFalse()
        verify(repoMock).newsletterInStatusWorkInProgress
    }
}
