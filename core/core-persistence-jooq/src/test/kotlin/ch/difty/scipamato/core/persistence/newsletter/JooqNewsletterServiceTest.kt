package ch.difty.scipamato.core.persistence.newsletter

import ch.difty.scipamato.common.entity.newsletter.PublicationStatus
import ch.difty.scipamato.common.persistence.paging.PaginationContext
import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.entity.newsletter.Newsletter
import ch.difty.scipamato.core.entity.newsletter.NewsletterFilter
import ch.difty.scipamato.core.persistence.AbstractServiceTest
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.junit.jupiter.api.Test

@Suppress("UsePropertyAccessSyntax")
internal class JooqNewsletterServiceTest : AbstractServiceTest<Int, Newsletter, NewsletterRepository>() {

    private val repoMock = mockk<NewsletterRepository>(relaxed = true) {
        every { delete(any(), any()) } returns newsletterMock
        every { update(any()) } returns newsletterMock
    }
    private val filterMock = mockk<NewsletterFilter>()
    private val paginationContextMock = mockk<PaginationContext>()
    private val newsletterMock = mockk<Newsletter>(relaxed = true)
    private val newsletterWipMock = mockk<Newsletter>()

    private val service = JooqNewsletterService(repoMock, userRepoMock)

    private val newsletters = listOf(newsletterMock, newsletterMock)

    override val repo = repoMock

    override val entity = newsletterMock

    public override fun specificTearDown() {
        confirmVerified(repoMock, filterMock, paginationContextMock, newsletterMock)
    }

    @Test
    fun findingById_withFoundEntity_returnsOptionalOfIt() {
        val id = 7
        every { repoMock.findById(id) } returns newsletterMock
        auditFixture()

        val optNl = service.findById(id)
        optNl.isPresent.shouldBeTrue()
        optNl.get() shouldBeEqualTo newsletterMock

        verify { repoMock.findById(id) }
        verify { entity == entity }

        verifyAudit(1)
    }

    @Test
    fun findingById_withNotFoundEntity_returnsOptionalEmpty() {
        val id = 7
        every { repoMock.findById(id) } returns null
        service.findById(id).isPresent.shouldBeFalse()
        verify { repoMock.findById(id) }
    }

    @Test
    fun findingByFilter_delegatesToRepo() {
        every { repoMock.findPageByFilter(filterMock, paginationContextMock) } returns newsletters
        auditFixture()
        service.findPageByFilter(filterMock, paginationContextMock) shouldBeEqualTo newsletters
        verify { repoMock.findPageByFilter(filterMock, paginationContextMock) }
        verifyAudit(2)
    }

    @Test
    fun countingByFilter_delegatesToRepo() {
        every { repoMock.countByFilter(filterMock) } returns 3
        service.countByFilter(filterMock) shouldBeEqualTo 3
        verify { repoMock.countByFilter(filterMock) }
    }

    @Test
    fun savingOrUpdating_withUnsavedEntityAndOtherNewsletterInStatusWIP_throws() {
        every { newsletterMock.id } returns null
        every { newsletterMock.publicationStatus } returns PublicationStatus.WIP
        every { repoMock.newsletterInStatusWorkInProgress } returns java.util.Optional.of(newsletterWipMock)
        every { newsletterWipMock.id } returns 1

        invoking { service.saveOrUpdate(newsletterMock) } shouldThrow IllegalArgumentException::class withMessage
            "newsletter.onlyOneInStatusWipAllowed"

        verify { newsletterMock.publicationStatus }
        verify { newsletterMock.id }
        verify { repoMock.newsletterInStatusWorkInProgress }
        verify { newsletterWipMock.id }
    }

    @Test
    fun savingOrUpdating_withSavedEntity_butOtherNewsletterInWipStatus_throws() {
        every { newsletterMock.id } returns 2
        every { newsletterMock.publicationStatus } returns PublicationStatus.WIP
        every { repoMock.newsletterInStatusWorkInProgress } returns java.util.Optional.of(newsletterWipMock)
        every { newsletterWipMock.id } returns 1

        invoking { service.saveOrUpdate(newsletterMock) } shouldThrow
            java.lang.IllegalArgumentException::class withMessage "newsletter.onlyOneInStatusWipAllowed"

        verify { newsletterMock.publicationStatus }
        verify(exactly = 2) { newsletterMock.id }
        verify { repoMock.newsletterInStatusWorkInProgress }
        verify { newsletterWipMock.id }
    }

    @Test
    fun savingOrUpdating_withSavedEntity_butOtherNewsletterInWipStatus() {
        val newsletterId = 1

        every { newsletterMock.id } returns newsletterId
        every { newsletterMock.publicationStatus } returns PublicationStatus.WIP
        every { repoMock.newsletterInStatusWorkInProgress } returns java.util.Optional.of(newsletterWipMock)
        every { newsletterWipMock.id } returns newsletterId

        service.saveOrUpdate(newsletterMock)

        verify { newsletterMock.publicationStatus }
        verify(exactly = 3) { newsletterMock.id }
        verify { repoMock.newsletterInStatusWorkInProgress }
        verify { newsletterWipMock.id }
        verify { repoMock.update(newsletterMock) }
    }

    @Test
    fun savingOrUpdating_withPaperWithNullId_hasRepoAddThePaper() {
        every { newsletterMock.id } returns null
        every { newsletterMock.publicationStatus } returns PublicationStatus.PUBLISHED
        every { repoMock.add(newsletterMock) } returns newsletterMock
        auditFixture()
        service.saveOrUpdate(newsletterMock) shouldBeEqualTo newsletterMock
        verify { repoMock.add(newsletterMock) }
        verify { newsletterMock.id }
        verify { newsletterMock.publicationStatus }
        verify { newsletterMock == newsletterMock }
        verifyAudit(1)
    }

    @Test
    fun savingOrUpdating_withPaperWithNonNullId_hasRepoUpdateThePaper() {
        every { newsletterMock.id } returns 17
        every { newsletterMock.publicationStatus } returns PublicationStatus.PUBLISHED
        every { repoMock.update(newsletterMock) } returns newsletterMock
        auditFixture()
        service.saveOrUpdate(newsletterMock) shouldBeEqualTo newsletterMock
        verify { repoMock.update(newsletterMock) }
        verify { newsletterMock.id }
        verify { newsletterMock.publicationStatus }
        verify { newsletterMock == newsletterMock }
        verifyAudit(1)
    }

    @Test
    fun savingOrUpdating_witNoWipOption_justSaves() {
        // hypothetical case
        every { newsletterMock.id } returns 17
        every { newsletterMock.publicationStatus } returns PublicationStatus.WIP
        every { repoMock.newsletterInStatusWorkInProgress } returns java.util.Optional.empty()
        every { repoMock.update(newsletterMock) } returns newsletterMock
        auditFixture()

        service.saveOrUpdate(newsletterMock) shouldBeEqualTo newsletterMock

        verify { repoMock.newsletterInStatusWorkInProgress }
        verify { repoMock.update(newsletterMock) }
        verify { newsletterMock.id }
        verify { newsletterMock == newsletterMock }
        verify { newsletterMock.publicationStatus }
        verifyAudit(1)
    }

    @Test
    fun deleting_withNullEntity_doesNothing() {
        service.remove(null)
        verify(exactly = 0) { repoMock.delete(any(), any()) }
    }

    @Test
    fun deleting_withEntityWithNullId_doesNothing() {
        every { newsletterMock.id } returns null

        service.remove(newsletterMock)

        verify { newsletterMock.id }
        verify(exactly = 0) { repoMock.delete(any(), any()) }
    }

    @Test
    fun deleting_withEntityWithNormalId_delegatesToRepo() {
        every { newsletterMock.id } returns 3
        every { newsletterMock.version } returns 17

        service.remove(newsletterMock)

        verify(exactly = 2) { newsletterMock.id }
        verify(exactly = 1) { newsletterMock.version }
        verify(exactly = 1) { repoMock.delete(3, 17) }
    }

    @Test
    fun canCreateNewNewsletter_withNoWipNewsletters_isAllowed() {
        every { repoMock.newsletterInStatusWorkInProgress } returns java.util.Optional.empty()
        service.canCreateNewsletterInProgress().shouldBeTrue()
        verify { repoMock.newsletterInStatusWorkInProgress }
    }

    @Test
    fun canCreateNewNewsletter_withOneWipNewsletters_isNotAllowed() {
        every { repoMock.newsletterInStatusWorkInProgress } returns java.util.Optional.of(Newsletter())
        service.canCreateNewsletterInProgress().shouldBeFalse()
        verify { repoMock.newsletterInStatusWorkInProgress }
    }

    @Test
    fun mergingPaperIntoNewsletter_withNoWipNewsletterPresent() {
        val paperId: Long = 5
        val newsletterTopicId = 10
        val newsletterId = 1
        val langCode = "en"

        every { repoMock.newsletterInStatusWorkInProgress } returns java.util.Optional.empty()

        service.mergePaperIntoWipNewsletter(paperId, newsletterTopicId)

        verify { repoMock.newsletterInStatusWorkInProgress }
        verify(exactly = 0) { repoMock.mergePaperIntoNewsletter(newsletterId, paperId, newsletterTopicId, langCode) }
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

        every { repoMock.newsletterInStatusWorkInProgress } returns java.util.Optional.of(wip)
        every { repoMock.mergePaperIntoNewsletter(newsletterId, paperId, newsletterTopicId, langCode) } returns nlo

        service.mergePaperIntoWipNewsletter(paperId, newsletterTopicId)

        verify { repoMock.newsletterInStatusWorkInProgress }
        verify { repoMock.mergePaperIntoNewsletter(newsletterId, paperId, newsletterTopicId, langCode) }
    }

    @Test
    fun mergingPaperIntoNewsletter_withNoWipNewsletterPresent_cannotMerge() {
        val paperId: Long = 5
        val newsletterTopicId = 10

        every { repoMock.newsletterInStatusWorkInProgress } returns java.util.Optional.empty()
        service.mergePaperIntoWipNewsletter(paperId, newsletterTopicId)
        verify { repoMock.newsletterInStatusWorkInProgress }
    }

    @Test
    fun mergingPaperIntoNewsletter2_withNoWipNewsletterPresent_cannotMerge() {
        val paperId: Long = 5
        every { repoMock.newsletterInStatusWorkInProgress } returns java.util.Optional.empty()
        service.mergePaperIntoWipNewsletter(paperId)
        verify { repoMock.newsletterInStatusWorkInProgress }
    }

    @Test
    fun removingPaperFromNewsletter_withWipNewsletterPresentAndRemoveSucceeding_canRemove() {
        val paperId: Long = 5
        val newsletterId = 1
        val wip = Newsletter()
        wip.id = newsletterId

        every { repoMock.newsletterInStatusWorkInProgress } returns java.util.Optional.of(wip)
        every { repoMock.removePaperFromNewsletter(newsletterId, paperId) } returns 1

        service.removePaperFromWipNewsletter(paperId).shouldBeTrue()

        verify { repoMock.newsletterInStatusWorkInProgress }
        verify { repoMock.removePaperFromNewsletter(newsletterId, paperId) }
    }

    @Test
    fun removingPaperFromNewsletter_withWipNewsletterPresentAndRemoveNotSucceeding_cannotRemove() {
        val paperId: Long = 5
        val newsletterId = 1
        val wip = Newsletter()
        wip.id = newsletterId

        every { repoMock.newsletterInStatusWorkInProgress } returns java.util.Optional.of(wip)
        every { repoMock.removePaperFromNewsletter(newsletterId, paperId) } returns 0

        service.removePaperFromWipNewsletter(paperId).shouldBeFalse()

        verify { repoMock.newsletterInStatusWorkInProgress }
        verify { repoMock.removePaperFromNewsletter(newsletterId, paperId) }
    }

    @Test
    fun removingPaperFromNewsletter_withNoWipNewsletterPresent_cannotRemove() {
        val paperId: Long = 5
        every { repoMock.newsletterInStatusWorkInProgress } returns java.util.Optional.empty()
        service.removePaperFromWipNewsletter(paperId).shouldBeFalse()
        verify { repoMock.newsletterInStatusWorkInProgress }
    }
}
