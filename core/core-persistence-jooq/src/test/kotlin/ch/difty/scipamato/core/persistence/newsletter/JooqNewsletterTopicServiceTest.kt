@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.core.persistence.newsletter

import ch.difty.scipamato.common.persistence.paging.PaginationContext
import ch.difty.scipamato.core.entity.newsletter.NewsletterNewsletterTopic
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicFilter
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

internal class JooqNewsletterTopicServiceTest {

    private val repo = mockk<NewsletterTopicRepository>(relaxed = true)
    private val filterMock = mockk<NewsletterTopicFilter>()
    private val paginationContextMock = mockk<PaginationContext>()
    private val entity = mockk<NewsletterTopic>()
    private val topicDefinitionMock = mockk<NewsletterTopicDefinition>()
    private val persistedTopicDefinitionMock = mockk<NewsletterTopicDefinition>()

    private val service = JooqNewsletterTopicService(repo)

    private val topics = listOf(entity, entity)
    private val topicDefinitions = listOf(topicDefinitionMock, topicDefinitionMock)

    @AfterEach
    fun specificTearDown() {
        confirmVerified(repo, filterMock, paginationContextMock, entity, topicDefinitionMock)
    }

    @Test
    fun findingAll_delegatesToRepo() {
        val langCode = "en"
        every { repo.findAll(langCode) } returns topics
        service.findAll(langCode) shouldBeEqualTo topics
        verify { repo.findAll(langCode) }
    }

    @Test
    fun newUnpersistedNewsletterTopicDefinition_delegatesToRepo() {
        every { repo.newUnpersistedNewsletterTopicDefinition() } returns topicDefinitionMock
        service.newUnpersistedNewsletterTopicDefinition() shouldBeEqualTo topicDefinitionMock
        verify { topicDefinitionMock == topicDefinitionMock }
        verify { repo.newUnpersistedNewsletterTopicDefinition() }
    }

    @Test
    fun findingPageOfNewsletterTopicDefinitions_delegatesToRepo() {
        every { repo.findPageOfNewsletterTopicDefinitions(filterMock, paginationContextMock) } returns topicDefinitions
        service.findPageOfNewsletterTopicDefinitions(filterMock, paginationContextMock) shouldBeEqualTo topicDefinitions
        verify { repo.findPageOfNewsletterTopicDefinitions(filterMock, paginationContextMock) }
    }

    @Test
    fun findingPageOfEntityDefinitions_delegatesToRepo() {
        every { repo.findPageOfNewsletterTopicDefinitions(filterMock, paginationContextMock) } returns topicDefinitions
        service.findPageOfEntityDefinitions(filterMock, paginationContextMock).asSequence() shouldContainSame topicDefinitions.asSequence()
        verify { topicDefinitionMock == topicDefinitionMock }
        verify { repo.findPageOfNewsletterTopicDefinitions(filterMock, paginationContextMock) }
    }

    @Test
    fun countingNewsletterTopics_delegatesToRepo() {
        every { repo.countByFilter(filterMock) } returns 3
        service.countByFilter(filterMock) shouldBeEqualTo 3
        verify { repo.countByFilter(filterMock) }
    }

    @Test
    fun insertingNewsletterTopicDefinition_delegatesToRepo() {
        every { repo.insert(topicDefinitionMock) } returns persistedTopicDefinitionMock
        service.insert(topicDefinitionMock) shouldBeEqualTo persistedTopicDefinitionMock
        verify { repo.insert(topicDefinitionMock) }
    }

    @Test
    fun updatingNewsletterTopicDefinition_delegatesToRepo() {
        every { repo.update(topicDefinitionMock) } returns persistedTopicDefinitionMock
        service.update(topicDefinitionMock) shouldBeEqualTo persistedTopicDefinitionMock
        verify { repo.update(topicDefinitionMock) }
    }

    @Test
    fun savingOrUpdatingNewsletterTopicDefinition_withNullId_callsInsert() {
        every { topicDefinitionMock.id } returns null
        every { repo.insert(topicDefinitionMock) } returns persistedTopicDefinitionMock
        service.saveOrUpdate(topicDefinitionMock) shouldBeEqualTo persistedTopicDefinitionMock
        verify { topicDefinitionMock.id }
        verify { repo.insert(topicDefinitionMock) }
    }

    @Test
    fun savingOrUpdatingNewsletterTopicDefinition_withNonNullId_callsUpdate() {
        every { topicDefinitionMock.id } returns 1
        every { repo.update(topicDefinitionMock) } returns persistedTopicDefinitionMock
        service.saveOrUpdate(topicDefinitionMock) shouldBeEqualTo persistedTopicDefinitionMock
        verify { topicDefinitionMock.id }
        verify { repo.update(topicDefinitionMock) }
    }

    @Test
    fun deletingNewsletterTopicDefinition_delegatesToRepo() {
        val id = 11
        val version = 12
        every { repo.delete(id, version) } returns persistedTopicDefinitionMock
        service.delete(id, version) shouldBeEqualTo persistedTopicDefinitionMock
        verify { repo.delete(id, version) }
    }

    @Test
    fun gettingSortedNewsletterTopicsForNewsletter_withNoAssignedTopics_isEmpty() {
        val newsletterId = 12

        every { repo.findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId) } returns emptyList()
        every { repo.findAllSortedNewsletterTopicsForNewsletterWithId(newsletterId) } returns emptyList()

        service.getSortedNewsletterTopicsForNewsletter(newsletterId).shouldBeEmpty()

        verify { repo.removeObsoleteNewsletterTopicsFromSort(newsletterId) }
        verify { repo.findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId) }
        verify { repo.findAllSortedNewsletterTopicsForNewsletterWithId(newsletterId) }
    }

    @Test
    fun gettingSortedNewsletterTopicsForNewsletter_withNoPersistedTopics_resortsAllUnpersisted() {
        val newsletterId = 12
        val persisted = listOf<NewsletterNewsletterTopic>()
        val all = listOf(
            NewsletterNewsletterTopic(newsletterId, 1, Integer.MAX_VALUE, "topic1"),
            NewsletterNewsletterTopic(newsletterId, 2, Integer.MAX_VALUE, "topic2"))

        every { repo.findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId) } returns persisted
        every { repo.findAllSortedNewsletterTopicsForNewsletterWithId(newsletterId) } returns all

        val topics = service.getSortedNewsletterTopicsForNewsletter(newsletterId)

        topics shouldHaveSize 2
        topics.map { it.newsletterTopicId } shouldContainAll listOf(1, 2)
        topics.map { it.sort } shouldContainAll listOf(0, 1)
        topics.map { it.title } shouldContainAll listOf("topic1", "topic2")

        verify { repo.removeObsoleteNewsletterTopicsFromSort(newsletterId) }
        verify { repo.findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId) }
        verify { repo.findAllSortedNewsletterTopicsForNewsletterWithId(newsletterId) }
    }

    @Test
    fun gettingSortedNewsletterTopicsForNewsletter_withAllPersistedTopics_returnsThoseOnly() {
        val newsletterId = 12
        val persisted = listOf(
            NewsletterNewsletterTopic(newsletterId, 1, 0, "topic1"),
            NewsletterNewsletterTopic(newsletterId, 2, 1, "topic2"))
        val all = ArrayList<NewsletterNewsletterTopic>(persisted)

        every { repo.findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId) } returns persisted
        every { repo.findAllSortedNewsletterTopicsForNewsletterWithId(newsletterId) } returns all

        val topics = service.getSortedNewsletterTopicsForNewsletter(newsletterId)

        topics shouldHaveSize 2
        topics.map { it.newsletterTopicId } shouldContainAll listOf(1, 2)
        topics.map { it.sort } shouldContainAll listOf(0, 1)
        topics.map { it.title } shouldContainAll listOf("topic1", "topic2")

        verify { repo.removeObsoleteNewsletterTopicsFromSort(newsletterId) }
        verify { repo.findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId) }
        verify { repo.findAllSortedNewsletterTopicsForNewsletterWithId(newsletterId) }
    }

    @Test
    fun gettingSortedNewsletterTopicsForNewsletter_withPersistedAndUnpersistedTopics_returnsMergedLists() {
        val newsletterId = 12
        val persisted = listOf(
            NewsletterNewsletterTopic(newsletterId, 1, 0, "topic1"),
            NewsletterNewsletterTopic(newsletterId, 2, 1, "topic2"))
        val all = listOf(
            NewsletterNewsletterTopic(newsletterId, 1, Integer.MAX_VALUE, "topic1"),
            NewsletterNewsletterTopic(newsletterId, 2, Integer.MAX_VALUE, "topic2"),
            NewsletterNewsletterTopic(newsletterId, 3, Integer.MAX_VALUE, "topic3"))

        every { repo.findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId) } returns persisted
        every { repo.findAllSortedNewsletterTopicsForNewsletterWithId(newsletterId) } returns all

        val topics = service.getSortedNewsletterTopicsForNewsletter(newsletterId)

        topics shouldHaveSize 3
        topics.map { it.newsletterTopicId } shouldContainAll listOf(1, 2, 3)
        topics.map { it.sort } shouldContainAll listOf(0, 1, 2)
        topics.map { it.title } shouldContainAll listOf("topic1", "topic2", "topic3")

        verify { repo.removeObsoleteNewsletterTopicsFromSort(newsletterId) }
        verify { repo.findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId) }
        verify { repo.findAllSortedNewsletterTopicsForNewsletterWithId(newsletterId) }
    }

    @Test
    fun savingSortedNewsletterTopics_withTopics_delegatesSaveToRepo() {
        val newsletterId = 1
        val sortedTopics = listOf(
            NewsletterNewsletterTopic(newsletterId, 1, 0, "topic1"),
            NewsletterNewsletterTopic(newsletterId, 2, 1, "topic2"))
        service.saveSortedNewsletterTopics(newsletterId, sortedTopics)

        verify { repo.saveSortedNewsletterTopics(newsletterId, sortedTopics) }
    }

    @Test
    fun savingSortedNewsletterTopics_withNoTopics_skipsSaving() {
        service.saveSortedNewsletterTopics(1, emptyList())
    }
}
