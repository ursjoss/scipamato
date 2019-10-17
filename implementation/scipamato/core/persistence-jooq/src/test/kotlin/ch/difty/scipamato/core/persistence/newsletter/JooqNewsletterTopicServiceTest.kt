package ch.difty.scipamato.core.persistence.newsletter

import ch.difty.scipamato.common.persistence.paging.PaginationContext
import ch.difty.scipamato.core.entity.newsletter.NewsletterNewsletterTopic
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicFilter
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.util.Lists
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions

internal class JooqNewsletterTopicServiceTest {

    private val repo = mock<NewsletterTopicRepository>()
    private val filterMock = mock<NewsletterTopicFilter>()
    private val paginationContextMock = mock<PaginationContext>()
    private val entity = mock<NewsletterTopic>()
    private val topicDefinitionMock = mock<NewsletterTopicDefinition>()
    private val persistedTopicDefinitionMock = mock<NewsletterTopicDefinition>()

    private val service = JooqNewsletterTopicService(repo)

    private val topics = listOf(entity, entity)
    private val topicDefinitions = listOf(topicDefinitionMock, topicDefinitionMock)

    @AfterEach
    fun specificTearDown() {
        verifyNoMoreInteractions(repo, filterMock, paginationContextMock, entity, topicDefinitionMock)
    }

    @Test
    fun findingAll_delegatesToRepo() {
        val langCode = "en"
        whenever(repo.findAll(langCode)).thenReturn(topics)
        assertThat(service.findAll(langCode)).isEqualTo(topics)
        verify(repo).findAll(langCode)
    }

    @Test
    fun newUnpersistedNewsletterTopicDefinition_delegatesToRepo() {
        whenever(repo.newUnpersistedNewsletterTopicDefinition()).thenReturn(topicDefinitionMock)
        assertThat(service.newUnpersistedNewsletterTopicDefinition()).isEqualTo(topicDefinitionMock)
        verify(repo).newUnpersistedNewsletterTopicDefinition()
    }

    @Test
    fun findingPageOfNewsletterTopicDefinitions_delegatesToRepo() {
        whenever(repo.findPageOfNewsletterTopicDefinitions(filterMock, paginationContextMock))
            .thenReturn(topicDefinitions)
        assertThat(service.findPageOfNewsletterTopicDefinitions(filterMock, paginationContextMock))
            .isEqualTo(topicDefinitions)
        verify(repo).findPageOfNewsletterTopicDefinitions(filterMock, paginationContextMock)
    }

    @Test
    fun findingPageOfEntityDefinitions_delegatesToRepo() {
        whenever(repo.findPageOfNewsletterTopicDefinitions(filterMock, paginationContextMock))
            .thenReturn(topicDefinitions)
        assertThat(service.findPageOfEntityDefinitions(filterMock, paginationContextMock))
            .toIterable().hasSameElementsAs(topicDefinitions)
        verify(repo).findPageOfNewsletterTopicDefinitions(filterMock, paginationContextMock)
    }

    @Test
    fun countingNewsletterTopics_delegatesToRepo() {
        whenever(repo.countByFilter(filterMock)).thenReturn(3)
        assertThat(service.countByFilter(filterMock)).isEqualTo(3)
        verify(repo).countByFilter(filterMock)
    }

    @Test
    fun insertingNewsletterTopicDefinition_delegatesToRepo() {
        whenever(repo.insert(topicDefinitionMock)).thenReturn(persistedTopicDefinitionMock)
        assertThat(service.insert(topicDefinitionMock)).isEqualTo(persistedTopicDefinitionMock)
        verify(repo).insert(topicDefinitionMock)
    }

    @Test
    fun updatingNewsletterTopicDefinition_delegatesToRepo() {
        whenever(repo.update(topicDefinitionMock)).thenReturn(persistedTopicDefinitionMock)
        assertThat(service.update(topicDefinitionMock)).isEqualTo(persistedTopicDefinitionMock)
        verify(repo).update(topicDefinitionMock)
    }

    @Test
    fun savingOrUpdatingNewsletterTopicDefinition_withNullId_callsInsert() {
        whenever(topicDefinitionMock.id).thenReturn(null)
        whenever(repo.insert(topicDefinitionMock)).thenReturn(persistedTopicDefinitionMock)
        assertThat(service.saveOrUpdate(topicDefinitionMock)).isEqualTo(persistedTopicDefinitionMock)
        verify(topicDefinitionMock).id
        verify(repo).insert(topicDefinitionMock)
    }

    @Test
    fun savingOrUpdatingNewsletterTopicDefinition_withNonNullId_callsUpdate() {
        whenever(topicDefinitionMock.id).thenReturn(1)
        whenever(repo.update(topicDefinitionMock)).thenReturn(persistedTopicDefinitionMock)
        assertThat(service.saveOrUpdate(topicDefinitionMock)).isEqualTo(persistedTopicDefinitionMock)
        verify(topicDefinitionMock).id
        verify(repo).update(topicDefinitionMock)
    }

    @Test
    fun deletingNewsletterTopicDefinition_delegatesToRepo() {
        val id = 11
        val version = 12
        whenever(repo.delete(id, version)).thenReturn(persistedTopicDefinitionMock)
        assertThat(service.delete(id, version)).isEqualTo(persistedTopicDefinitionMock)
        verify(repo).delete(id, version)
    }

    @Test
    fun gettingSortedNewsletterTopicsForNewsletter_withNoAssignedTopics_isEmpty() {
        val newsletterId = 12

        whenever(repo.findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId)).thenReturn(emptyList())
        whenever(repo.findAllSortedNewsletterTopicsForNewsletterWithId(newsletterId)).thenReturn(emptyList())

        assertThat(service.getSortedNewsletterTopicsForNewsletter(newsletterId)).isEmpty()

        verify(repo).removeObsoleteNewsletterTopicsFromSort(newsletterId)
        verify(repo).findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId)
        verify(repo).findAllSortedNewsletterTopicsForNewsletterWithId(newsletterId)
    }

    @Test
    fun gettingSortedNewsletterTopicsForNewsletter_withNoPersistedTopics_resortsAllUnpersisted() {
        val newsletterId = 12
        val persisted = Lists.emptyList<NewsletterNewsletterTopic>()
        val all = listOf(
            NewsletterNewsletterTopic(newsletterId, 1, Integer.MAX_VALUE, "topic1"),
            NewsletterNewsletterTopic(newsletterId, 2, Integer.MAX_VALUE, "topic2"))

        whenever(repo.findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId)).thenReturn(persisted)
        whenever(repo.findAllSortedNewsletterTopicsForNewsletterWithId(newsletterId)).thenReturn(all)

        val topics = service.getSortedNewsletterTopicsForNewsletter(newsletterId)

        assertThat(topics).hasSize(2)
        assertThat(topics).extracting("newsletterTopicId").containsExactly(1, 2)
        assertThat(topics).extracting("sort").containsExactly(0, 1)
        assertThat(topics).extracting("title").containsExactly("topic1", "topic2")

        verify(repo).removeObsoleteNewsletterTopicsFromSort(newsletterId)
        verify(repo).findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId)
        verify(repo).findAllSortedNewsletterTopicsForNewsletterWithId(newsletterId)
    }

    @Test
    fun gettingSortedNewsletterTopicsForNewsletter_withAllPersistedTopics_returnsThoseOnly() {
        val newsletterId = 12
        val persisted = listOf(
            NewsletterNewsletterTopic(newsletterId, 1, 0, "topic1"),
            NewsletterNewsletterTopic(newsletterId, 2, 1, "topic2"))
        val all = ArrayList<NewsletterNewsletterTopic>(persisted)

        whenever(repo.findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId)).thenReturn(persisted)
        whenever(repo.findAllSortedNewsletterTopicsForNewsletterWithId(newsletterId)).thenReturn(all)

        val topics = service.getSortedNewsletterTopicsForNewsletter(newsletterId)

        assertThat(topics).hasSize(2)
        assertThat(topics).extracting("newsletterTopicId").containsExactly(1, 2)
        assertThat(topics).extracting("sort").containsExactly(0, 1)
        assertThat(topics).extracting("title").containsExactly("topic1", "topic2")

        verify(repo).removeObsoleteNewsletterTopicsFromSort(newsletterId)
        verify(repo).findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId)
        verify(repo).findAllSortedNewsletterTopicsForNewsletterWithId(newsletterId)
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

        whenever(repo.findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId)).thenReturn(persisted)
        whenever(repo.findAllSortedNewsletterTopicsForNewsletterWithId(newsletterId)).thenReturn(all)

        val topics = service.getSortedNewsletterTopicsForNewsletter(newsletterId)

        assertThat(topics).hasSize(3)
        assertThat(topics).extracting("newsletterTopicId").containsExactly(1, 2, 3)
        assertThat(topics).extracting("sort").containsExactly(0, 1, 2)
        assertThat(topics).extracting("title").containsExactly("topic1", "topic2", "topic3")

        verify(repo).removeObsoleteNewsletterTopicsFromSort(newsletterId)
        verify(repo).findPersistedSortedNewsletterTopicsForNewsletterWithId(newsletterId)
        verify(repo).findAllSortedNewsletterTopicsForNewsletterWithId(newsletterId)
    }

    @Test
    fun savingSortedNewsletterTopics_withTopics_delegatesSaveToRepo() {
        val newsletterId = 1
        val sortedTopics = listOf(
            NewsletterNewsletterTopic(newsletterId, 1, 0, "topic1"),
            NewsletterNewsletterTopic(newsletterId, 2, 1, "topic2"))
        service.saveSortedNewsletterTopics(newsletterId, sortedTopics)

        verify<NewsletterTopicRepository>(repo).saveSortedNewsletterTopics(newsletterId, sortedTopics)
    }

    @Test
    fun savingSortedNewsletterTopics_withNoTopics_skipsSaving() {
        service.saveSortedNewsletterTopics(1, Lists.emptyList())
    }
}
